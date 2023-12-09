package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import xyz.douzhan.bank.constants.BankConstants;
import xyz.douzhan.bank.constants.BizExceptionConstants;
import xyz.douzhan.bank.enums.AccountType;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.po.BankCard;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import xyz.douzhan.bank.mapper.BankPhoneBankRefMapper;
import xyz.douzhan.bank.service.BankPhoneBankRefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.BizUtils;
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
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //查询名下银行账户
        List<BankPhoneBankRef> bankPhoneBankRefList = getAccounts(id);
        //汇总账户资产
        BigDecimal asset=new BigDecimal(0);
        List<String> balanceList = bankPhoneBankRefList.stream().map(bankPhoneBankRef -> {
            Account account = Db.lambdaQuery(Account.class)
                    .eq(Account::getId, bankPhoneBankRef.getAccountId())
                    .select(Account::getBalance).one();
            if (account == null) {
                throw new BizException(BizExceptionConstants.UNEXPECTED_BUSINESS_ANOMALIES);
            }
            return account.getBalance();
        }).collect(Collectors.toList());
        for (String balance: balanceList) {
            asset=asset.add(new BigDecimal(balance));
        }
        return asset.toPlainString();
    }



    @Override
    public List<AccountVO> getAccount(Long id) {
        //校验非空
        BizUtils.assertArgsNotNull(id);
        //查询名下账户信息
        List<BankPhoneBankRef> bankPhoneBankRefList = this.baseMapper.selectList(new LambdaQueryWrapper<BankPhoneBankRef>().eq(BankPhoneBankRef::getPhoneAccountId, id));
        if (CollUtil.isEmpty(bankPhoneBankRefList)){
            throw new BizException(BizExceptionConstants.UNEXPECTED_BUSINESS_ANOMALIES);
        }

        ArrayList<AccountVO> accountVOS = new ArrayList<>();
        for (BankPhoneBankRef bankPhoneBankRef : bankPhoneBankRefList) {
            //查询银行账户
            Account account = Db.lambdaQuery(Account.class).eq(Account::getId, bankPhoneBankRef.getAccountId()).one();
            if (account == null) {
                throw new BizException(BizExceptionConstants.UNEXPECTED_BUSINESS_ANOMALIES);
            }
            //查询账户对应的银行卡号
            BankCard bankcard = Db.lambdaQuery(BankCard.class).eq(BankCard::getAccountId, bankPhoneBankRef.getAccountId()).select(BankCard::getNumber).one();
            if (bankcard == null) {
                throw new BizException(BizExceptionConstants.UNEXPECTED_BUSINESS_ANOMALIES);
            }
            //组装VO
            AccountVO accountVO = new AccountVO();
            //复制
            BeanUtils.copyProperties(account,accountVO);
            //设置账户id
            accountVO.setAccountId(account.getId());
            //设置默认签约账户
            accountVO.setContractedAccount(BankConstants.CONTRACTED_ACCOUNTS);
            //设置默认账户
            if (bankPhoneBankRef.getDefaultAccount() != null) {
                accountVO.setIsDefault(BankConstants.DEFAULT_ACCOUNT);
            }
            //动态设置类型和类型名称
            accountVO.setType(account.getType());
            if (account.getType()== AccountType.FIRST) {
                accountVO.setTypeName(BankConstants.DEBIT_CARD);
            } else if (account.getType()== AccountType.SECOND) {
                accountVO.setTypeName(BankConstants.ELECTRONIC_ACCOUNTS);
            } else {
                accountVO.setTypeName(BankConstants.ELECTRONIC_ACCOUNTS);
            }
            accountVO.setBankcardNum(bankcard.getNumber());
            //添加vo
            accountVOS.add(accountVO);
        }
        return accountVOS;
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
            throw new BizException(BizExceptionConstants.UNEXPECTED_BUSINESS_ANOMALIES);
        }
        return bankPhoneBankRefList;
    }


}
