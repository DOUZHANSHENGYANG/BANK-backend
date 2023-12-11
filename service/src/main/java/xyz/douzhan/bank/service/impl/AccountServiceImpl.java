package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.constants.AuthConstant;
import xyz.douzhan.bank.constants.BankConstant;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.enums.AccountStatus;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.mapper.AccountMapper;
import xyz.douzhan.bank.po.Bankcard;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.CommonBizUtils;
import xyz.douzhan.bank.vo.AccountVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        CommonBizUtils.assertArgsNotNull(id);
        //根据id查询账户信息
        Account account = this.baseMapper.selectById(id);
        //非空判断
        CommonBizUtils.assertArgsNotNull(account);
        //复制
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account, accountVO);
        accountVO.setAccountId(account.getId());
        return accountVO;
    }

    @Override
    public JSONObject getAccountNumber(Long id, Integer type) {
        //非空判断
        CommonBizUtils.assertArgsNotNull(id, type);
        //参数合法性校验
        if (type < 0 || type > 2) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }

        //查询账户
        List<BankPhoneBankRef> phoneBankRefs = Db.lambdaQuery(BankPhoneBankRef.class)
                .eq(BankPhoneBankRef::getPhoneAccountId, id)
                .select(BankPhoneBankRef::getAccountId, BankPhoneBankRef::getType)
                .list();
        if (CollUtil.isEmpty(phoneBankRefs)) {
            throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
        }


        ArrayList<Object> firstList = new ArrayList<>();
        ArrayList<Object> secondList = new ArrayList<>();
        ArrayList<Object> thirdList = new ArrayList<>();
        for (BankPhoneBankRef phoneBankRef : phoneBankRefs) {
            //查询卡号
            Bankcard bankcard = Db.lambdaQuery(Bankcard.class).eq(Bankcard::getAccountId, phoneBankRef.getAccountId()).select(Bankcard::getNumber).one();
            if (bankcard == null) {
                throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
            }

            //查询手机号
            Account account = Db.lambdaQuery(Account.class).eq(Account::getId, phoneBankRef.getAccountId()).select(Account::getPhoneNumber).one();
            if (account == null) {
                throw new BizException(BizExceptionConstant.UNEXPECTED_BUSINESS_ANOMALIES);
            }

            HashMap<String, Object> accountInfoMap = new HashMap<>();
            accountInfoMap.put("accountId",phoneBankRef.getAccountId());
            accountInfoMap.put("phoneNumber",account.getPhoneNumber());
            accountInfoMap.put("cardNumber",bankcard.getNumber());
            if ((type == 1||type==0)&& phoneBankRef.getType()== xyz.douzhan.bank.constants.BankConstant.FIRST_ACCOUNT){//查I类账户
                firstList.add(accountInfoMap);
                continue;
            }
            if ((type == 2||type==0)&& phoneBankRef.getType()== xyz.douzhan.bank.constants.BankConstant.SECOND_ACCOUNT) {//查II类账户
                secondList.add(accountInfoMap);
                continue;
            }

            if ((type == 3||type==0)&& phoneBankRef.getType()== xyz.douzhan.bank.constants.BankConstant.THIRD_ACCOUNT) {//查III类账户
                thirdList.add(accountInfoMap);
            }
        }

        JSONObject result = new JSONObject();
        if (!firstList.isEmpty()){
            result.put("firstAccount",firstList);
        }
       if (!secondList.isEmpty()){
           result.put("secondAccount",secondList);
       }
        if (!thirdList.isEmpty()){
            result.put("thirdAccount",thirdList);
        }
        return result;
    }

    @Override
    @Transactional
    public void createAccount(Account account, Long phoneAccountId) {
        //参数非空校验
        CommonBizUtils.assertArgsNotNull(phoneAccountId);
        //获取用户信息id
        PhoneAccount phoneAccount = Db.lambdaQuery(PhoneAccount.class).eq(PhoneAccount::getId, phoneAccountId).select(PhoneAccount::getUserInfoId).one();
        CommonBizUtils.assertArgsNotNull(phoneAccountId);
        //查询绑定账户信息
        Account bindAccount = baseMapper.selectById(account.getAccountId());
        CommonBizUtils.assertArgsNotNull(phoneAccountId);
        //设置账户信息
        account.setStatus(AccountStatus.NOT_ACTIVATED);
        account.setIdentifier(BankConstant.ACCOUNT_IDENTIFIER);
        account.setBalance(xyz.douzhan.bank.constants.BankConstant.DEFAULT_BALANCE);
        account.setUserInfoId(phoneAccount.getUserInfoId());
        account.setPassword(account.getPassword());
        account.setType(account.getType());
        account.setPurpose(account.getPurpose());
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

    @Override
    public void updateStatus(Long accountId, Integer status) {
        //参数校验
        CommonBizUtils.assertArgsNotNull(accountId,status);
        //校验状态参数合法
        List<AccountStatus> accountStatusList = Arrays.stream(AccountStatus.values()).filter(accountStatus -> accountStatus.getValue() == status).collect(Collectors.toList());
        if (CollUtil.isEmpty(accountStatusList)){
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }

        //修改状态
        Account account = new Account();
        account.setStatus(accountStatusList.get(0));
        this.baseMapper.updateById(account);
    }

    @Override
    public void comparePayPwd(Long id, String payPwd) {
        //查询密码进行比较
        PhoneAccount phoneAccount = Db.lambdaQuery(PhoneAccount.class).eq(PhoneAccount::getId, id).select(PhoneAccount::getPayPWD).one();
        CommonBizUtils.assertArgsNotNull(phoneAccount);
        if (!StrUtil.equals(payPwd,phoneAccount.getPayPWD())){
            throw new AuthenticationException(AuthConstant.INVALID_PAY_PWD);
        }
    }
}
