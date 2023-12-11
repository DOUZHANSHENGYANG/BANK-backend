package xyz.douzhan.bank.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.constants.BankConstant;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.constants.TransactionConstant;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.po.*;
import xyz.douzhan.bank.mapper.TransferMapper;
import xyz.douzhan.bank.properties.BankProperties;
import xyz.douzhan.bank.service.AccountService;
import xyz.douzhan.bank.service.TransactionHistoryService;
import xyz.douzhan.bank.service.TransferService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.*;
import xyz.douzhan.bank.vo.SupportBankVO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
public class TransferServiceImpl extends ServiceImpl<TransferMapper, Transfer> implements TransferService {
    private final BankProperties bankProperties;
    private final TransactionHistoryService transactionHistoryService;
    private final AccountService accountService;

    @Override
    public Long createTransfer(Transfer transfer, Long phoneAccountId) {
        //参数非空校验
        CommonBizUtils.assertArgsNotNull(phoneAccountId);
        //获取类型
        Integer type = transfer.getType();

        if (type == TransactionConstant.INTER_BANK) {
            //截取swift code前四位本行标识 判断行内还是行外转账
            //转账人银行
            String transferorBank = transfer.getTransferorBankSwiftCode().substring(0, 4);
            //收款人银行
            String transfereeBank = transfer.getTransfereeBankSwiftCode().substring(0, 4);
            //行内转账
            if (StrUtil.equals(transfereeBank,transferorBank)) {
                //查询收款人是否在本行有账户
                assetHasAccount(transfer.getTransfereeAccountId(), transfer.getTransfereeName());
                //查询转账人是否在本行有账户
                assetHasAccount(transfer.getTransferorAccountId(), transfer.getTransferorName());

                //生成转账订单信息
                transfer.setMoney(String.valueOf(BankConstant.DEFAULT_BALANCE));
                //设置订单状态
                transfer.setState(0);
                //生成订单号 0-231226-00000-0001 0表示交易类型 231266表示年月日 00000表示秒 最后两位表示订单号
                String redisOrderKey = xyz.douzhan.bank.constants.BankConstant.TRANSACTION_ORDER_NUM_REDIS_PREFIX + transfer.getTransferorAccountId();
                Object orderRedisNum = RedisUtils.get(redisOrderKey);

                if (orderRedisNum==null){//这一秒还没有订单
                     AtomicInteger newNum=new AtomicInteger(0);
                     String newOrderNum = DomainBizUtils.genOrderNum(transfer.getType(), transfer.getTransferorBankSwiftCode(), transfer.getTransfereeBankSwiftCode(), newNum.get());
                     transfer.setOrderNum(newOrderNum);
                    //存入缓存
                     RedisUtils.setWithExpire(redisOrderKey,newNum, 1,TimeUnit.SECONDS);
                }else {//这一秒有过订单
                     AtomicInteger oldOrderNum= (AtomicInteger) orderRedisNum;
                     Integer newNum = oldOrderNum.incrementAndGet();
                     String newOrderNum = DomainBizUtils.genOrderNum(transfer.getType(), transfer.getTransferorBankSwiftCode(), transfer.getTransfereeBankSwiftCode(), newNum);
                     transfer.setOrderNum(newOrderNum);
                    //存入缓存
                     RedisUtils.set(redisOrderKey,oldOrderNum);
                }
                //存入数据库
                baseMapper.insert(transfer);
                return transfer.getId();
            }else {
                //TODO 跨行转账
            }
        }
        throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
    }

    @Override
    @Transactional
    public void complete(Long id,Integer status,Double money) {
        //校验状态
        if (status!= TransactionConstant.FAILED_FINISH||status!= TransactionConstant.SUCCESS_FINISH){
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_NOT_NULL);
        }
        //查询订单
        Transfer transfer = getBaseMapper().selectOne(new LambdaQueryWrapper<Transfer>().eq(Transfer::getId,id).eq(Transfer::getState,0));
        CommonBizUtils.assertArgsNotNull(transfer);
        //转账
        Long transfereeAccountId = transfer.getTransfereeAccountId();
        Long transferorAccountId = transfer.getTransferorAccountId();

         Account transferorAccount= accountService.getOne(new LambdaQueryWrapper<Account>().eq(Account::getAccountId,transferorAccountId).select(Account::getAccountId,Account::getBalance));
         double transferorAccountBalance=transferorAccount.getBalance();
        if (transferorAccountBalance<money){
            throw new BizException(BizExceptionConstant.ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        Account transfereeAccount=accountService.getOne(new LambdaQueryWrapper<Account>().eq(Account::getAccountId,transfereeAccountId).select(Account::getAccountId,Account::getBalance));
        double transfereeAccountBalance=transfereeAccount.getBalance();

        transferorAccountBalance= transferorAccountBalance - money;
        transfereeAccountBalance = transfereeAccountBalance + money;

        transferorAccount.setBalance(transferorAccountBalance);
        transfereeAccount.setBalance(transfereeAccountBalance);

        accountService.updateById(transferorAccount);
        accountService.updateById(transfereeAccount);
        //更新订单状态
        transfer.setState(status);
        baseMapper.updateById(transfer);
        //插入交易记录
        Bankcard transferorBankcard = Db.lambdaQuery(Bankcard.class).eq(Bankcard::getAccountId, transferorAccountId).select(Bankcard::getNumber).one();
        Bankcard transfereeBankcard = Db.lambdaQuery(Bankcard.class).eq(Bankcard::getAccountId, transfereeAccountId).select(Bankcard::getNumber).one();
        String remark=TransactionConstant.DEFAULT_REMARK;
        if (transfer.getRemark()!=null){
             remark=remark+":"+transfer.getRemark();
        }
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .transferNum(transfer.getOrderNum())
                .type(transfer.getType())
                .result(transfer.getState())
                .transferorName(transfer.getTransferorName())
                .transfereeName(transfer.getTransfereeName())
                .transfereeAccount(transfereeBankcard.getNumber())
                .transferorAccount(transferorBankcard.getNumber())
                .transfereeBankName(xyz.douzhan.bank.constants.BankConstant.BANK_NAME)
                .channel(xyz.douzhan.bank.constants.BankConstant.PHONE_BANK_CHANNEL)
                .remark(remark)
                .premium(BankConstant.DEFAULT_PREMIUM)
                .build();

        transactionHistoryService.save(transactionHistory);
    }

    @Override
    public List<SupportBankVO> getSupportBank() {
        SupportBankVO supportBankVO = new SupportBankVO();
        //添加本行的银行名字和代码
        ArrayList<SupportBankVO> supportBankVOS = new ArrayList<>();
        supportBankVOS.add(new SupportBankVO()
                .bankName(bankProperties.getBANK_NAME())
                .swiftCode(bankProperties.getBANK_SWIFT_CODE()));
        return supportBankVOS;
    }

    /**
     * 判断交易双方是否在本行有账户
     * @param accountId
     * @param name
     */
    public void assetHasAccount(Long accountId,String name){
        Account account = Db.lambdaQuery(Account.class).eq(Account::getId,accountId ).select(Account::getUserInfoId,Account::getStatus).one();
        UserInfo transfereeUserInfo = Db.lambdaQuery(UserInfo.class).eq(account.getUserInfoId() != null, UserInfo::getId, account.getUserInfoId()).select(UserInfo::getName, UserInfo::getPinYin).one();
        assertAccountNotNull(account);
        assertUserInfoValid(transfereeUserInfo,name);
    }

    private void assertAccountNotNull(Account account){
        if (account==null){
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_NUM);
        }
    }
    private void assertUserInfoValid(UserInfo userInfo,String name){
        if (userInfo==null){
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_NUM);
        }
        if (StrUtil.equals(userInfo.getName(),name)||StrUtil.equals(userInfo.getPinYin(),name)){
            return;
        }
            throw new BizException(BizExceptionConstant.INVALID_NAME);
    }
}
