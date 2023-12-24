package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import xyz.douzhan.bank.enums.AccountType;
import xyz.douzhan.bank.enums.BankcardStatus;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.mapper.BankCardMapper;
import xyz.douzhan.bank.po.*;
import xyz.douzhan.bank.properties.BizProperties;
import xyz.douzhan.bank.service.BankCardService;
import xyz.douzhan.bank.service.BankInfoService;
import xyz.douzhan.bank.service.BankPhoneBankRefService;
import xyz.douzhan.bank.service.LimitService;
import xyz.douzhan.bank.utils.CommonBizUtil;
import xyz.douzhan.bank.utils.CypherUtil;
import xyz.douzhan.bank.utils.DomainBizUtil;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
@RequiredArgsConstructor
public class BankCardServiceImpl extends ServiceImpl<BankCardMapper, Bankcard> implements BankCardService {

    private final BizProperties bizProperties;
    private final BankInfoService bankInfoService;
    private final BankPhoneBankRefService bankPhoneBankRefService;
    private final LimitService limitService;

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
        if (bankcardStatus == null) {
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
        PhoneAccount phoneAccount = Db.lambdaQuery(PhoneAccount.class)
                .eq(PhoneAccount::getId, phoneAccountId)
                .select(PhoneAccount::getUserInfoId).one();
        if (phoneAccount == null) {
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }
        Long userInfoId = phoneAccount.getUserInfoId();

        List<Bankcard> bankcards = baseMapper.selectList(Wrappers.lambdaQuery(Bankcard.class).eq(Bankcard::getUserId, userInfoId).eq(Bankcard::getType, bankcardInfo.getType()));
        if (bankcards.size() > 5) {
            throw new BizException("II III类账户创建分别不能超过5个");
        }
        //创建银行卡账户
        Bankcard bankcard = new Bankcard();
        BeanUtils.copyProperties(bankcardInfo, bankcard);
        bankcard.setPhoneNumber(CypherUtil.encrypt(bankcard.getPhoneNumber()));
        bankcard.setPassword(CypherUtil.encrypt(bankcard.getPassword()));
        bankcard.setUserId(userInfoId);
        //设置状态
        bankcard.setStatus(BankcardStatus.NOT_ACTIVATED);
        //默认余额
        bankcard.setBalance(BizConstant.DEFAULT_BALANCE);
        //获取开户行信息
        BankInfo bankInfo = bankInfoService.getOne(Wrappers.lambdaQuery(BankInfo.class)
                .eq(BankInfo::getInstitutionCode, bizProperties.getInstitutionCode()));
        if (bankInfo == null) {
            throw new BizException(BizExceptionConstant.INVALID_INSTITUTION_CODE);
        }
        bankcard.setBankInfoId(bankInfo.getId());
        bankcard.setMedium(BizConstant.HAS_NOT_ENTITY);
        bankcard.setVersion(0);
        bankcard.setDeleted(0);
        baseMapper.insert(bankcard);
        // 生成卡号
        String cardNum = genCardNum(bankcard.getId(), bankcard.getType());
        //设置卡号
        bankcard.setNumber(CypherUtil.encrypt(cardNum));
        baseMapper.updateById(bankcard);


        //建立银行卡账户和手机账户联系
        BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();
        bankPhoneBankRef.setAccountId(bankcard.getId());
        bankPhoneBankRef.setPhoneAccountId(phoneAccountId);
        bankPhoneBankRef.setPayPWD(CypherUtil.encrypt(bankcard.getPassword()));
        bankPhoneBankRef.setType(bankcard.getType());
        bankPhoneBankRef.setDefaultAccount(BizConstant.IS_NOT_DEFAULT_ACCOUNT);
        bankPhoneBankRefService.save(bankPhoneBankRef);

        // 设置 交易限额
        Limit limit = new Limit();
        limit.setBankcardId(bankcard.getId());
        limit.setType(bankcard.getType());
        limitService.save(limit);
    }

    /**
     * 根据手机号查询是否有本行I类账户
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public Long getFirstAccount(String phoneNumber) {
        Bankcard bankcard = baseMapper.selectOne(Wrappers.lambdaQuery(Bankcard.class)
                .eq(Bankcard::getPhoneNumber, CypherUtil.encrypt(phoneNumber))
                .eq(Bankcard::getType, AccountType.FIRST.getValue())
                .select(Bankcard::getId));
        if (bankcard == null) {
            throw new AuthenticationException(BizConstant.REFUSE_TO_REGISTER_A_RESPONSE);
        }
        return bankcard.getId();
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
        if (CollUtil.isEmpty(bankcards)) {
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }
        return bankcards.stream().map(bankcard -> {
            BankCardVO bankCardVO = new BankCardVO();
            BeanUtils.copyProperties(bankcard, bankCardVO);
            BankPhoneBankRef phoneBankRef = bankPhoneBankRefService.getOne(Wrappers.lambdaQuery(BankPhoneBankRef.class).eq(BankPhoneBankRef::getAccountId,bankcard.getId()).select(BankPhoneBankRef::getDefaultAccount, BankPhoneBankRef::getAlias));
            bankCardVO.setAlias(phoneBankRef.getAlias());
            bankCardVO.setDefaultAccount(phoneBankRef.getDefaultAccount());
            String bankName = bankInfoService.getById(bankcard.getBankInfoId()).getName();
            bankCardVO.setBankName(bankName);
            bankCardVO.setNumber(CypherUtil.decrypt(bankcard.getNumber()));
            return bankCardVO;
        }).toList();
    }

    /**
     * 根据银行卡id查询完整卡号
     *
     * @param bankcardId
     * @return
     */
    @Override
    public String getBankcardNumber(Long bankcardId) {
        Bankcard bankcard = baseMapper.selectOne(Wrappers.lambdaQuery(Bankcard.class).eq(Bankcard::getId, bankcardId).select(Bankcard::getNumber));
        if (bankcard == null) {
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }
        return CypherUtil.decrypt(bankcard.getNumber());
    }

    /**
     * 注销银行卡账户
     *
     * @param bankcardId
     */
    @Override
    @Transactional
    public void deleteAccount(Long bankcardId) {
        Bankcard bankcard = baseMapper.selectById(bankcardId);
        if (bankcard == null) {
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }

        boolean condition2 = bankcard.getMedium().compareTo(BizConstant.HAS_NOT_ENTITY) == 0 &&
                (bankcard.getType().compareTo(AccountType.SECOND.getValue()) == 0 ||
                        bankcard.getType().compareTo(AccountType.THIRD.getValue()) == 0);
        boolean condition3 = (bankcard.getMedium().compareTo(BizConstant.HAS_ENTITY) == 0) &&
                (bankcard.getBalance().compareTo(BizConstant.DEFAULT_BALANCE) == 0) &&
                (bankcard.getType().compareTo(AccountType.SECOND.getValue()) == 0);

        if (condition2) {// 无实体的电子账户可以销户
            Bankcard bindBankcard = baseMapper.selectById(bankcard.getBankcardId());
            long balance = bindBankcard.getBalance() + bankcard.getBalance();
            bankcard.setBalance(balance);
            baseMapper.updateById(bankcard);// 提现
            bankPhoneBankRefService.remove(Wrappers.lambdaQuery(BankPhoneBankRef.class).eq(BankPhoneBankRef::getAccountId, bankcard.getId())); //删除关联
            baseMapper.deleteById(bankcardId); // 删除账户
            limitService.remove(Wrappers.lambdaQuery(Limit.class).eq(Limit::getBankcardId,bankcard.getId()));// 删除限额
        } else if (condition3) { // 有实体但无存款的二类账户可以销户
            baseMapper.deleteById(bankcardId); // 删除账户
            bankPhoneBankRefService.remove(Wrappers.lambdaQuery(BankPhoneBankRef.class).eq(BankPhoneBankRef::getAccountId, bankcard.getId()));//删除关联
            limitService.remove(Wrappers.lambdaQuery(Limit.class).eq(Limit::getBankcardId,bankcard.getId()));// 删除限额
        } else {
            throw new BizException(BizExceptionConstant.INVALID_LOGOUT_CONDITION);
        }

    }

    /**
     * 根据数据库id生成卡号
     *
     * @param id
     */
    private String genCardNum(Long id, Integer type) {
        String part = new StringBuffer()
                .append(bizProperties.getIIN().get(0))//IIN
                .append(String.format("%02d", type))//bizType
                .append(bizProperties.getRegionCode())//regionCode
                .append(String.format("%07d", id))//sequenceNumber
                .toString();
        return part + CommonBizUtil.genVerifyBitByLuhn(part);
    }
}
