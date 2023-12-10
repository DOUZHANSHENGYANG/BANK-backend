package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.constants.BankConstants;
import xyz.douzhan.bank.constants.BizExceptionConstants;
import xyz.douzhan.bank.enums.AccountStatus;
import xyz.douzhan.bank.enums.AccountType;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.mapper.AccountMapper;
import xyz.douzhan.bank.po.BankCard;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.BizUtils;
import xyz.douzhan.bank.vo.AccountVO;

import java.util.ArrayList;
import java.util.HashMap;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Override
    public AccountVO getAccountInfo(Long id) {
        //非空判断
        BizUtils.assertArgsNotNull(id);
        //根据id查询账户信息
        Account account = this.baseMapper.selectById(id);
        //非空判断
        BizUtils.assertArgsNotNull(account);
        //复制
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account, accountVO);
        accountVO.setAccountId(account.getId());
        return accountVO;
    }

    @Override
    public JSONObject getAccountNumber(Long id, Integer type) {
        //非空判断
        BizUtils.assertArgsNotNull(id, type);
        //参数合法性校验
        if (type < 0 || type > 2) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }

        //查询账户
        List<BankPhoneBankRef> phoneBankRefs = Db.lambdaQuery(BankPhoneBankRef.class)
                .eq(BankPhoneBankRef::getPhoneAccountId, id)
                .select(BankPhoneBankRef::getAccountId, BankPhoneBankRef::getType)
                .list();
        if (CollUtil.isEmpty(phoneBankRefs)) {
            throw new BizException(BizExceptionConstants.UNEXPECTED_BUSINESS_ANOMALIES);
        }


        ArrayList<Object> firstList = new ArrayList<>();
        ArrayList<Object> secondList = new ArrayList<>();
        ArrayList<Object> thirdList = new ArrayList<>();
        for (BankPhoneBankRef phoneBankRef : phoneBankRefs) {
            //查询卡号
            BankCard bankcard = Db.lambdaQuery(BankCard.class).eq(BankCard::getAccountId, phoneBankRef.getAccountId()).select(BankCard::getNumber).one();
            if (bankcard == null) {
                throw new BizException(BizExceptionConstants.UNEXPECTED_BUSINESS_ANOMALIES);
            }

            //查询手机号
            Account account = Db.lambdaQuery(Account.class).eq(Account::getId, phoneBankRef.getAccountId()).select(Account::getPhoneNumber).one();
            if (account == null) {
                throw new BizException(BizExceptionConstants.UNEXPECTED_BUSINESS_ANOMALIES);
            }

            HashMap<String, Object> accountInfoMap = new HashMap<>();
            accountInfoMap.put("accountId",phoneBankRef.getAccountId());
            accountInfoMap.put("phoneNumber",account.getPhoneNumber());
            accountInfoMap.put("cardNumber",bankcard.getNumber());
            if ((type == 1||type==0)&& phoneBankRef.getType()== AccountType.FIRST){//查I类账户
                firstList.add(accountInfoMap);
                continue;
            }
            if ((type == 2||type==0)&& phoneBankRef.getType()== AccountType.SECOND) {//查II类账户
                secondList.add(accountInfoMap);
                continue;
            }

            if ((type == 3||type==0)&& phoneBankRef.getType()== AccountType.THIRD) {//查III类账户
                thirdList.add(accountInfoMap);
            }
        }

        JSONObject result = new JSONObject();
        if ( firstList.size()>0){
            result.put("firstAccount",firstList);
        }
       if (secondList.size()>0){
           result.put("secondAccount",secondList);
       }
        if (thirdList.size()>0){
            result.put("thirdAccount",thirdList);
        }
        return result;
    }

    @Override
    @Transactional
    public void createAccount(Account account, Long phoneAccountId) {
        //参数非空校验
        BizUtils.assertArgsNotNull(phoneAccountId);
        //获取用户信息id
        PhoneAccount phoneAccount = Db.lambdaQuery(PhoneAccount.class).eq(PhoneAccount::getId, phoneAccountId).select(PhoneAccount::getUserInfoId).one();
        BizUtils.assertArgsNotNull(phoneAccountId);
        //查询绑定账户信息
        Account bindAccount = baseMapper.selectById(account.getAccountId());
        BizUtils.assertArgsNotNull(phoneAccountId);
        //设置账户信息
        account.setStatus(AccountStatus.NOT_ACTIVATED);
        account.setIdentifier(BankConstants.ACCOUNT_IDENTIFIER);
        account.setBalance(BankConstants.DEFAULT_BALANCE);
        account.setUserInfoId(phoneAccount.getUserInfoId());
        account.setInstitutionCode(bindAccount.getInstitutionCode());
        account.setBankAddress(bindAccount.getBankAddress());
        account.setBankName(bindAccount.getBankName());
        //新建账户
        baseMapper.insert(account);
        //建立手机账户与新账户关联
        BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();
        bankPhoneBankRef.setAccountId(account.getId());
        bankPhoneBankRef.setPhoneAccountId(phoneAccountId);
        bankPhoneBankRef.setType(account.getType());
        Db.save(bankPhoneBankRef);
    }
}
