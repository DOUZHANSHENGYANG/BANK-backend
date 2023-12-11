package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.constants.BankConstant;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.po.Bankcard;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import xyz.douzhan.bank.mapper.BankPhoneBankRefMapper;
import xyz.douzhan.bank.service.BankPhoneBankRefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.CommonBizUtils;
import xyz.douzhan.bank.vo.AccountVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
public class BankPhoneBankRefServiceImpl extends ServiceImpl<BankPhoneBankRefMapper, BankPhoneBankRef> implements BankPhoneBankRefService {

    @Override
    public String getAsset(Long id) {
        //校验非空
        if (id==null){
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //查询名下银行账户
        List<BankPhoneBankRef> bankPhoneBankRefList = getAccounts(id);
        //汇总账户资产
        BigDecimal asset=new BigDecimal(0);
        List<Double> balanceList = bankPhoneBankRefList.stream().map(bankPhoneBankRef -> {
            Account account = Db.lambdaQuery(Account.class)
                    .eq(Account::getId, bankPhoneBankRef.getAccountId())
                    .select(Account::getBalance).one();
            if (account == null) {
                throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
            }
            return account.getBalance();
        }).collect(Collectors.toList());
        for (Double balance: balanceList) {
            asset=asset.add(new BigDecimal(balance));
        }
        return asset.toPlainString();
    }



    @Override
    public List<AccountVO> getAccount(Long id) {
        //校验非空
        CommonBizUtils.assertArgsNotNull(id);
        //查询名下账户信息
        List<BankPhoneBankRef> bankPhoneBankRefList = this.baseMapper.selectList(new LambdaQueryWrapper<BankPhoneBankRef>().eq(BankPhoneBankRef::getPhoneAccountId, id));
        if (CollUtil.isEmpty(bankPhoneBankRefList)){
            throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
        }

        ArrayList<AccountVO> accountVOS = new ArrayList<>();
        for (BankPhoneBankRef bankPhoneBankRef : bankPhoneBankRefList) {
            //查询银行账户
            Account account = Db.lambdaQuery(Account.class).eq(Account::getId, bankPhoneBankRef.getAccountId()).one();
            if (account == null) {
                throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
            }
            //查询账户对应的银行卡号
            Bankcard bankcard = Db.lambdaQuery(Bankcard.class).eq(Bankcard::getAccountId, bankPhoneBankRef.getAccountId()).select(Bankcard::getNumber).one();
            if (bankcard == null) {
                throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
            }
            //组装VO
            AccountVO accountVO = new AccountVO();
            //复制
            BeanUtils.copyProperties(account,accountVO);
            //设置账户id
            accountVO.setAccountId(account.getId());
            //设置默认签约账户
            accountVO.setContractedAccount(xyz.douzhan.bank.constants.BankConstant.CONTRACTED_ACCOUNTS);
            //设置默认账户
            if (bankPhoneBankRef.getDefaultAccount() != null) {
                accountVO.setIsDefault(xyz.douzhan.bank.constants.BankConstant.DEFAULT_ACCOUNT);
            }
            //动态设置类型和类型名称
            accountVO.setType(account.getType());
            if (account.getType()== xyz.douzhan.bank.constants.BankConstant.FIRST_ACCOUNT) {
                accountVO.setTypeName(xyz.douzhan.bank.constants.BankConstant.DEBIT_CARD);
            } else if (account.getType()== xyz.douzhan.bank.constants.BankConstant.SECOND_ACCOUNT) {
                accountVO.setTypeName(xyz.douzhan.bank.constants.BankConstant.ELECTRONIC_ACCOUNTS);
            } else {
                accountVO.setTypeName(xyz.douzhan.bank.constants.BankConstant.ELECTRONIC_ACCOUNTS);
            }
            accountVO.setBankcardNum(bankcard.getNumber());
            //添加vo
            accountVOS.add(accountVO);
        }
        return accountVOS;
    }

    @Override
    @Transactional
    public void bindDefaultCard(Long newDefaultAccountId, Long phoneAccountId) {
        //非空判断
        CommonBizUtils.assertArgsNotNull(newDefaultAccountId,phoneAccountId);
        //查询名下账户
        LambdaQueryWrapper<BankPhoneBankRef> condition = new LambdaQueryWrapper<BankPhoneBankRef>()
                .eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId)
                .select(BankPhoneBankRef::getAccountId, BankPhoneBankRef::getDefaultAccount);
        List<BankPhoneBankRef> bankPhoneBankRefs = this.baseMapper.selectList(condition);
        CommonBizUtils.assertCollNotNull(bankPhoneBankRefs);
        //找出当前绑定的accountId
        Long oldDefaultAccountId = bankPhoneBankRefs.stream()
                .filter(bankPhoneBankRef -> StrUtil.equals(bankPhoneBankRef.getDefaultAccount(), xyz.douzhan.bank.constants.BankConstant.IS_DEFAULT_ACCOUNT))
                .map(BankPhoneBankRef::getAccountId)
                .toList()
                .get(0);
        //判断新旧是否一致
        if(newDefaultAccountId==oldDefaultAccountId){
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //修改默认绑定账户
        BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();
        bankPhoneBankRef.setDefaultAccount(BankConstant.IS_NOT_DEFAULT_ACCOUNT);
        this.baseMapper.update(bankPhoneBankRef,new LambdaQueryWrapper<BankPhoneBankRef>().eq(BankPhoneBankRef::getAccountId,oldDefaultAccountId));
        //绑定新账户
        bankPhoneBankRef.setDefaultAccount(xyz.douzhan.bank.constants.BankConstant.IS_DEFAULT_ACCOUNT);
        this.baseMapper.update(bankPhoneBankRef,new LambdaQueryWrapper<BankPhoneBankRef>().eq(BankPhoneBankRef::getAccountId,newDefaultAccountId));
    }

    /**
     * 查询绑定的名下银行账户
     * @param id
     * @return
     */
    @NotNull
    private List<BankPhoneBankRef> getAccounts(Long id) {
        List<BankPhoneBankRef> bankPhoneBankRefList = this.baseMapper
                .selectList(new LambdaQueryWrapper<BankPhoneBankRef>()
                        .eq(BankPhoneBankRef::getPhoneAccountId, id).select(BankPhoneBankRef::getAccountId));
        if (CollUtil.isEmpty(bankPhoneBankRefList)){
            throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
        }
        return bankPhoneBankRefList;
    }


}
