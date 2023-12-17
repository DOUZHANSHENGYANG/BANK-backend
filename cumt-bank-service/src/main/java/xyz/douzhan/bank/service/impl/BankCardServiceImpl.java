package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.constants.BizConstant;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.dto.BankCardVO;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.enums.AccountType;
import xyz.douzhan.bank.enums.BankcardStatus;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.mapper.BankCardMapper;
import xyz.douzhan.bank.po.BankInfo;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import xyz.douzhan.bank.po.Bankcard;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.properties.BizProperties;
import xyz.douzhan.bank.service.BankCardService;
import xyz.douzhan.bank.service.BankInfoService;
import xyz.douzhan.bank.service.BankPhoneBankRefService;
import xyz.douzhan.bank.service.PhoneAccountService;
import xyz.douzhan.bank.utils.CommonBizUtil;
import xyz.douzhan.bank.utils.DomainBizUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
@RequiredArgsConstructor
public class BankCardServiceImpl extends ServiceImpl<BankCardMapper, Bankcard> implements BankCardService {
    private final BizProperties bizProperties;
//    private final PhoneAccountService phoneAccountService;
    private  final BankInfoService bankInfoService;
    private final BankPhoneBankRefService bankPhoneBankRefService;
    private static final String PHONE_NUMBER="phoneNumber";
    private static final String BANKCARD_ID="bankcardId";
    private static final String CARD_NUMBER="cardNumber";
    /**
     * 更新银行卡状态
     *
     * @param bankcardId
     * @param status
     */
    @Override
    public void updateStatus(Long bankcardId, Integer status) {
        // 检查状态合法性
        BankcardStatus bankcardStatus = DomainBizUtil.hasStatus(status);
        if (bankcardStatus==null){
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_STATUS);
        }
        //更新状态
        Bankcard bankcard = new Bankcard();
        bankcard.setId(bankcardId);
        bankcard.setStatus(bankcardStatus);
        baseMapper.updateById(bankcard);
    }

    /**
     * 开通银行卡账户
     *
     * @param bankcardInfo
     * @param phoneAccountId
     */
    @Override
    @Transactional
    public void createBankcardAccount(Bankcard bankcardInfo, Long phoneAccountId) {
        //查询手机账户获取用户id
        PhoneAccount phoneAccount = Db.lambdaQuery(PhoneAccount.class).eq(PhoneAccount::getId, phoneAccountId).select(PhoneAccount::getUserInfoId).one();
        if (phoneAccount==null){
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }

        //创建银行卡账户
        Long userInfoId = phoneAccount.getUserInfoId();
        Bankcard bankcard = new Bankcard();
        BeanUtils.copyProperties(bankcardInfo,bankcard);
        bankcard.setUserId(userInfoId);
        //设置状态
        bankcard.setStatus(BankcardStatus.NOT_ACTIVATED);
        //默认余额
        bankcard.setBalance(BizConstant.DEFAULT_BALANCE);
        //获取开户行信息
        BankInfo bankInfo = bankInfoService.getOne(Wrappers.lambdaQuery(BankInfo.class).eq(BankInfo::getInstitutionCode, bizProperties.getInstitutionCode()));
        if (bankInfo==null){
            throw new BizException(BizExceptionConstant.INVALID_INSTITUTION_CODE);
        }
        bankcard.setBankInfoId(bankInfo.getId());
        bankcard.setMedium(BizConstant.HAS_NOT_ENTITY);

        baseMapper.insert(bankcard);

        String cardNum = genCardNum(bankcard.getId(), bankcard.getType());
        //存入minio文件系统，需要再取出来
        HashMap<String, String> map = CommonBizUtil.getTruncateAndHashPart(cardNum);
        bankcard.setHash(map.get(BizConstant.HASH_NUM));
        bankcard.setNumber(map.get(BizConstant.TRUNCATE_REMAIN_NUM));

        baseMapper.updateById(bankcard);
        //建立银行卡账户和手机账户联系
        BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();
        bankPhoneBankRef.setAccountId(bankcard.getId());
        bankPhoneBankRef.setPhoneAccountId(phoneAccountId);
        bankPhoneBankRef.setPayPWD(bankcard.getPassword());
        bankPhoneBankRef.setType(bankcard.getType());
        bankPhoneBankRefService.save(bankPhoneBankRef);
    }

    /**
     * 根据手机账户id查询卡号和手机号
     *
     * @param phoneAccountId
     * @param type
     * @return
     */
    @Override
    public JSONObject getCardNumAndPhoneNumber(Long phoneAccountId, Integer type) {
        //参数合法性校验
        if (type < 0 || type > 2) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }

        //查询账户
        List<BankPhoneBankRef> phoneBankRefs = bankPhoneBankRefService
                .list(Wrappers.lambdaQuery(BankPhoneBankRef.class)
                        .eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId)
                        .select(BankPhoneBankRef::getAccountId,BankPhoneBankRef::getType));

        if (CollUtil.isEmpty(phoneBankRefs)) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }


        ArrayList<Object> firstList = new ArrayList<>();
        ArrayList<Object> secondList = new ArrayList<>();
        ArrayList<Object> thirdList = new ArrayList<>();
        for (BankPhoneBankRef phoneBankRef : phoneBankRefs) {
            //查询卡号和手机号
            Bankcard bankcard = baseMapper.selectOne(Wrappers.lambdaQuery(Bankcard.class)
                    .eq(Bankcard::getId, phoneBankRef.getAccountId())
                    .select(Bankcard::getNumber, Bankcard::getPhoneNumber));

            HashMap<String, Object> accountInfoMap = new HashMap<>();
            accountInfoMap.put(BANKCARD_ID,phoneBankRef.getAccountId());
            accountInfoMap.put(PHONE_NUMBER,bankcard.getPhoneNumber());
            accountInfoMap.put(CARD_NUMBER,bankcard.getNumber());

            if ((type == 1||type==0)&& phoneBankRef.getType()== AccountType.FIRST.getValue()){//查I类账户
                firstList.add(accountInfoMap);
                continue;
            }
            if ((type == 2||type==0)&& phoneBankRef.getType()== AccountType.SECOND.getValue()) {//查II类账户
                secondList.add(accountInfoMap);
                continue;
            }

            if ((type == 3||type==0)&& phoneBankRef.getType()== AccountType.THIRD.getValue()) {//查III类账户
                thirdList.add(accountInfoMap);
            }
        }

        JSONObject result = new JSONObject();
        if (!firstList.isEmpty()){
            result.put(BizConstant.FIRST_ACCOUNT,firstList);
        }
        if (!secondList.isEmpty()){
            result.put(BizConstant.FIRST_ACCOUNT,secondList);
        }
        if (!thirdList.isEmpty()){
            result.put(BizConstant.FIRST_ACCOUNT,thirdList);
        }
        return result;
    }

    /**
     * 根据手机号查询是否有本行I类账户
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public ResponseResult getFirstAccount(String phoneNumber) {
        Bankcard bankcard = baseMapper.selectOne(Wrappers.lambdaQuery(Bankcard.class)
                .eq(Bankcard::getPhoneNumber, phoneNumber)
                .eq(Bankcard::getType, AccountType.FIRST.getValue())
                .select(Bankcard::getId));
        if (bankcard == null) {
            return ResponseResult.error().message(BizConstant.REFUSE_TO_REGISTER_A_RESPONSE);
        }
        return ResponseResult.success(bankcard.getBankcardId());
    }

    /**
     * 根据银行卡id集合查询账户信息
     *
     * @param bankcardIds
     * @return
     */
    @Override
    public List<BankCardVO> getByIds(List<Long> bankcardIds) {
        List<Bankcard> bankcards = baseMapper.selectBatchIds(bankcardIds);
        if (CollUtil.isEmpty(bankcards)){
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }
        List<BankCardVO> bankCardVOS = bankcards.stream().map(bankcard -> {
            BankCardVO bankCardVO = new BankCardVO();
            BeanUtils.copyProperties(bankcard, bankCardVO);
            String bankName = bankInfoService.getById(bankcard.getBankInfoId()).getName();
            bankCardVO.setBankName(bankName);
            return bankCardVO;
        }).toList();

        return bankCardVOS;
    }

    /**
     * 根据银行卡id查询完整卡号
     *
     * @param bankcardId
     * @return
     */
    @Override
    public ResponseResult getBankcardNumber(Long bankcardId) {

        // TODO 待完成
        return null;
    }

    /**
     * 根据数据库id生成卡号
     * @param id
     */
    private String genCardNum(Long id,Integer type) {
        StringBuffer stringBuffer = new StringBuffer();
        String part = stringBuffer
                .append(bizProperties.getIIN().get(0))//IIN
                .append(String.format("%02d", type))//bizType
                .append(bizProperties.getRegionCode())//regionCode
                .append(String.format("%08d", id))//sequenceNumber
                .toString();
        return CommonBizUtil.genVerifyBitByLuhn(part)+part;
    }
}
