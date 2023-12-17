package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.constants.BizConstant;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.constants.RedisConstant;
import xyz.douzhan.bank.constants.TransferConstant;
import xyz.douzhan.bank.dto.DigitalReceiptDTO;
import xyz.douzhan.bank.dto.result.PageResponseResult;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.mapper.TransferMapper;
import xyz.douzhan.bank.po.Bankcard;
import xyz.douzhan.bank.po.TransactionHistory;
import xyz.douzhan.bank.po.Transfer;
import xyz.douzhan.bank.properties.BizProperties;
import xyz.douzhan.bank.service.BankCardService;
import xyz.douzhan.bank.service.TransactionHistoryService;
import xyz.douzhan.bank.service.TransferService;
import xyz.douzhan.bank.utils.DomainBizUtil;
import xyz.douzhan.bank.utils.RedisUtil;
import xyz.douzhan.bank.vo.SupportBankVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private final BizProperties bizProperties;
    private final TransactionHistoryService transactionHistoryService;
    private final BankCardService bankCardService;

    @Override
    public Long createOrder(Transfer transfer) {
        //获取类型
        Integer type = transfer.getType();

        if (TransferConstant.INTRA_BANK.compareTo(type) == 0) {
            //截取SWIFT Code前四位,判断行内还是行外转账
            //转账人银行
            String transferorBank = transfer.getTransferorBankSwiftCode().substring(0, 4);
            //收款人银行
            String transfereeBank = transfer.getTransfereeBankSwiftCode().substring(0, 4);
            //行内转账
            if (StrUtil.equals(transfereeBank, transferorBank)) {
                //生成转账订单信息
                //设置备注
                if (transfer.getRemark() == null) {
                    transfer.setRemark(TransferConstant.DEFAULT_REMARK);
                }
                //设置手续费
                transfer.setPremium(TransferConstant.DEFAULT_PREMIUM);
                //设置订单状态
                transfer.setStatus(TransferConstant.ONGOING_STATUS);
                //生成订单号 0-231226-00000-0001 0表示交易类型 231266表示年月日 00000表示秒 最后两位表示订单号
                String redisOrderKey = RedisConstant.TRANSACTION_ORDER_NUM_REDIS_PREFIX + transfer.getTransferorAccountId();
                String newOrderNum = null;
                //TODO 限额处理
                while (true) {
                    Object orderRedisNum = RedisUtil.get(redisOrderKey);
                    if (orderRedisNum == null) {//这一秒还没有生成过订单
                        //存入缓存
                        RedisUtil.setWithExpire(redisOrderKey, 0, 1, TimeUnit.SECONDS);
                        //生成订单号
                        newOrderNum = DomainBizUtil.genOrderNum(transfer.getType(), transfer.getTransferorBankSwiftCode(), transfer.getTransfereeBankSwiftCode(), 0);
                        //乐观锁
                        if ((Integer) RedisUtil.get(redisOrderKey) == 0) {
                            break;
                        }
                    } else {
                        //生成订单号
                        newOrderNum = DomainBizUtil.genOrderNum(transfer.getType(), transfer.getTransferorBankSwiftCode(), transfer.getTransfereeBankSwiftCode(), (Integer) orderRedisNum);
                        //乐观锁
                        if (RedisUtil.get(redisOrderKey) == orderRedisNum) {
                            RedisUtil.set(redisOrderKey, (Integer) orderRedisNum + 1);
                            break;
                        }
                    }
                }
                //设置订单号
                transfer.setOrderNum(newOrderNum);
                //存入数据库
                baseMapper.insert(transfer);
                return transfer.getId();
            } else {//TODO 跨行转账
                throw new BizException(BizExceptionConstant.NOT_SUPPORT_THE_BIZ_CURRENTLY);
            }
        }
        throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
    }

    /**
     * 完成订单
     *
     * @param orderId
     * @param status
     */
    @Override
    @Transactional
    public void completeOrder(Long orderId, Integer status, Long money) {
        //校验状态
        if (!Objects.equals(status, TransferConstant.FAILED_FINISH) || !status.equals(TransferConstant.SUCCESS_FINISH)) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //查询订单
        Transfer transfer = baseMapper.selectOne(new LambdaQueryWrapper<Transfer>()
                .eq(Transfer::getId, orderId)
                .eq(Transfer::getStatus, TransferConstant.ONGOING_STATUS));
        if (transfer == null) {
            throw new BizException(BizExceptionConstant.ORDER_STATUS_UPDATE_FAILED);
        }
        //转账
        Long transfereeAccountId = transfer.getTransfereeAccountId();
        Long transferorAccountId = transfer.getTransferorAccountId();

        Bankcard transferorAccount = bankCardService.getOne(
                new LambdaQueryWrapper<Bankcard>()
                        .eq(Bankcard::getId, transferorAccountId)
                        .select(Bankcard::getId, Bankcard::getBalance, Bankcard::getNumber));
        Long transferorAccountBalance = transferorAccount.getBalance();
        if (transferorAccountBalance < TransferConstant.ZERO_BALANCE) {
            throw new BizException(BizExceptionConstant.ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        Bankcard transfereeAccount = bankCardService.getOne(
                new LambdaQueryWrapper<Bankcard>()
                        .eq(Bankcard::getId, transfereeAccountId)
                        .select(Bankcard::getId, Bankcard::getBalance, Bankcard::getNumber));
        Long transfereeAccountBalance = transfereeAccount.getBalance();

        transferorAccountBalance = transferorAccountBalance - money;
        transfereeAccountBalance = transfereeAccountBalance + money;

        transferorAccount.setBalance(transferorAccountBalance);
        transfereeAccount.setBalance(transfereeAccountBalance);

        bankCardService.updateById(transferorAccount);
        bankCardService.updateById(transfereeAccount);
        //更新订单状态
        transfer.setStatus(status);
        baseMapper.updateById(transfer);
        //新增交易记录
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .orderNum(transfer.getOrderNum())
                .type(transfer.getType())
                .result(transfer.getStatus())
                .transferorName(transfer.getTransferorName())
                .transferorAccountNum(transferorAccount.getNumber())
                .transfereeName(transfer.getTransfereeName())
                .transferorAccountNum(transfereeAccount.getNumber())
                .transfereeBankName(BizConstant.BANK_NAME)
                .channel(BizConstant.PHONE_BANK_CHANNEL)
                .remark(transfer.getRemark())
                .premium(transfer.getPremium())
                .build();
        transactionHistoryService.save(transactionHistory);
    }

    /**
     * 获得支持的银行SWIFT Code和名字
     *
     * @return
     */
    @Override
    public List<SupportBankVO> getSupportBank() {
        //添加本行的银行名字和代码
        ArrayList<SupportBankVO> supportBankVOS = new ArrayList<>();
        supportBankVOS.add(new SupportBankVO()
                .bankName(bizProperties.getBankName())
                .swiftCode(bizProperties.getSwiftCode()));
        return supportBankVOS;
    }

    /**
     * 获取电子回单
     *
     * @param receiptDTO
     * @return
     */
    @Override
    public PageResponseResult getHistoryRecord(DigitalReceiptDTO receiptDTO) {
        Page<Transfer> page = new Page<>(receiptDTO.getStartPage(), receiptDTO.getPageNum());
        LambdaQueryWrapper<Transfer> lqw = Wrappers.lambdaQuery(Transfer.class)
                .eq(Transfer::getTransferorAccountId, receiptDTO.getBankcardId())
                .eq(Transfer::getType, receiptDTO.getTransferType())
                .eq(receiptDTO.getChannelType() != null,
                        Transfer::getChannel,
                        receiptDTO.getChannelType())
                .between(
                        receiptDTO.getStartTime() != null && receiptDTO.getEndTime() != null,
                        Transfer::getCreateTime,
                        receiptDTO.getStartTime(),
                        receiptDTO.getEndTime())
                .orderByDesc(Transfer::getCreateTime);
//这里返回回总条数和页数
        baseMapper.selectList(page, lqw);
        if (CollUtil.isEmpty(page.getRecords())){
            throw new BizException(BizExceptionConstant.TRANSFER_RECORD_SELECT_FAILED);
        }
        return PageResponseResult.success(page.getRecords(),page.getTotal(),page.getCurrent(),page.getSize());
    }

    /**
     * 验证电子回单
     *
     * @param orderNum
     * @param name
     * @return
     */
    @Override
    public Boolean validateHistoryRecord(String orderNum, String name) {
        Transfer transfer = baseMapper.selectOne(Wrappers.lambdaQuery(Transfer.class).eq(Transfer::getOrderNum, orderNum).eq(Transfer::getTransfereeName, name));
        if (transfer==null){
            return false;
        }
        return true;
    }


}
