package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.constants.BizConstant;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.AliasDTO;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.mapper.BankPhoneBankRefMapper;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import xyz.douzhan.bank.po.Bankcard;
import xyz.douzhan.bank.service.BankPhoneBankRefService;
import xyz.douzhan.bank.utils.CypherUtil;

import java.util.List;
import java.util.stream.Stream;

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
public class BankPhoneBankRefServiceImpl extends ServiceImpl<BankPhoneBankRefMapper, BankPhoneBankRef> implements BankPhoneBankRefService {


    /**
     * 查询总资产
     *
     * @return
     */
    @Override
    public Long getAsset() {
        Long phoneAccountId = UserContext.getContext();
        //查询绑定的银行账户
        List<BankPhoneBankRef> bankPhoneBankRefList = baseMapper.selectList(
                Wrappers.lambdaQuery(BankPhoneBankRef.class)
                        .eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId)
                        .select(BankPhoneBankRef::getAccountId));
        if (CollUtil.isEmpty(bankPhoneBankRefList)){
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }
        long sum = 0L;

        return bankPhoneBankRefList.stream()
                .map(bankPhoneBankRef -> Db.lambdaQuery(Bankcard.class).eq(Bankcard::getId, bankPhoneBankRef.getAccountId())
                        .select(Bankcard::getBalance).one().getBalance())
                .reduce(sum, Long::sum);
    }

    /**
     * 绑定默认卡
     *
     * @param oldBankcardId
     * @param newBankcardId
     */
    @Override
    @Transactional
    public void bindDefaultCard(Long oldBankcardId, Long newBankcardId) {
        BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();

        //修改旧的
        bankPhoneBankRef.setDefaultAccount(BizConstant.IS_NOT_DEFAULT_ACCOUNT);
        baseMapper.update(bankPhoneBankRef,
                Wrappers.lambdaUpdate(BankPhoneBankRef.class).eq(BankPhoneBankRef::getAccountId, oldBankcardId));
        bankPhoneBankRef.setPhoneAccountId(newBankcardId);
        //修改新的
        bankPhoneBankRef.setDefaultAccount(BizConstant.IS_DEFAULT_ACCOUNT);
        baseMapper.update(bankPhoneBankRef,
                Wrappers.lambdaUpdate(BankPhoneBankRef.class).eq(BankPhoneBankRef::getAccountId, newBankcardId));
    }

    /**
     * 设别名
     *
     * @param aliasDTO
     */
    @Override
    public void setAlias(AliasDTO aliasDTO) {
        BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();
        BeanUtils.copyProperties(aliasDTO,bankPhoneBankRef);
        baseMapper.update(bankPhoneBankRef,Wrappers.lambdaUpdate(bankPhoneBankRef).eq(BankPhoneBankRef::getAccountId,bankPhoneBankRef.getAccountId()));
    }

    /**
     * 比较手机银行交易密码
     *
     * @param bankcardId
     * @param payPwd
     */
    @Override
    public void comparePayPwd(Long bankcardId, String payPwd) {
        BankPhoneBankRef bankPhoneBankRef = baseMapper.selectOne(
                Wrappers.lambdaQuery(BankPhoneBankRef.class)
                        .eq(BankPhoneBankRef::getAccountId, bankcardId)
                        .select(BankPhoneBankRef::getPayPWD));
        if (bankPhoneBankRef == null) {
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }
        if (!StrUtil.equals(CypherUtil.encryptSM4(payPwd), bankPhoneBankRef.getPayPWD())) {
            throw new BizException(BizExceptionConstant.PAY_PWD_ERROR);
        }
    }

    /**
     * 查询已绑定的银行卡账户id  在根据id集合传银行卡账户信息
     *
     * @return
     */
    @Override
    public List<Long> getBankcardIds() {
        Long phoneAccountId = UserContext.getContext();
        List<BankPhoneBankRef> bankPhoneBankRefs = baseMapper.selectList(
                Wrappers.lambdaQuery(BankPhoneBankRef.class)
                        .eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId)
                        .select(BankPhoneBankRef::getAccountId));
        if (CollUtil.isEmpty(bankPhoneBankRefs)) {
            throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
        }
        return bankPhoneBankRefs.stream().map(BankPhoneBankRef::getAccountId).toList();
    }

//    public ResponseResult getBankcardIdByPhoneAccountId(Long phoneAccountId) {
//        List<BankPhoneBankRef> bankPhoneBankRefs = baseMapper.selectList(
//                Wrappers.lambdaQuery(BankPhoneBankRef.class)
//                        .eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId)
//                        .select(BankPhoneBankRef::getAccountId)
//        );
//        if (CollUtil.isEmpty(bankPhoneBankRefs)) {
//            return ResponseResult.error();
//        }
//        Stream<Long> ids = bankPhoneBankRefs.stream().map(BankPhoneBankRef::getAccountId);
//        return ResponseResult.success(ids);
//    }

}
