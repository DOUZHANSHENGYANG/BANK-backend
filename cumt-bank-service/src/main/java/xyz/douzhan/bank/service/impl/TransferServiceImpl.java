package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.properties.BizProperties;
import xyz.douzhan.bank.service.AsyncService;
import xyz.douzhan.bank.service.BankCardService;
import xyz.douzhan.bank.service.TransactionHistoryService;
import xyz.douzhan.bank.service.TransferService;
import xyz.douzhan.bank.utils.DomainBizUtil;
import xyz.douzhan.bank.utils.QRCodeUtil;
import xyz.douzhan.bank.utils.RedisUtil;
import xyz.douzhan.bank.vo.SupportBankVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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

        if (TransferConstant.INTRA_BANK.compareTo(type) == 0) {//行内转账
            Bankcard bankcard = bankCardService.getOne(Wrappers.lambdaQuery(Bankcard.class).eq(Bankcard::getNumber, transfer.getTransfereeCardNum()).select(Bankcard::getId));
            UserInfo userInfo = Db.lambdaQuery(UserInfo.class).eq(UserInfo::getName, transfer.getTransfereeName()).select(UserInfo::getId).one();
            if (bankcard==null||userInfo==null){
                throw new BizException(BizExceptionConstant.INVALID_TRANSFEREE_INFO);
            }
            //设置转账订单信息
            setTransferInfo(transfer);
            // 新增订单
            baseMapper.insert(transfer);
            return transfer.getId();
        } else if (TransferConstant.INTER_BANK.compareTo(type) == 0) {//TODO 跨行转账
            throw new BizException(BizExceptionConstant.NOT_SUPPORT_THE_BIZ_CURRENTLY);
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
    public void completeOrder(Long orderId, Integer status, Integer money) {
        //校验状态
        if (!(Objects.equals(status, TransferConstant.FAILED_FINISH) || status.equals(TransferConstant.SUCCESS_FINISH))) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //查询订单
        Transfer transfer = baseMapper.selectOne(new LambdaQueryWrapper<Transfer>()
                .eq(Transfer::getId, orderId)
                .eq(Transfer::getStatus, TransferConstant.ONGOING_STATUS));
        if (transfer == null||transfer.getStatus()!=TransferConstant.ONGOING_STATUS) {
            throw new BizException(BizExceptionConstant.ORDER_STATUS_UPDATE_FAILED);
        }
        //转账
        transfer.setMoney(money);
        doTransfer(status, money, transfer);
        //新增交易记录
        insertHistory(transfer);
    }

    private void doTransfer(Integer status, Integer money, Transfer transfer) {
        String transferorCardNum = transfer.getTransferorCardNum();
        String transfereeCardNum = transfer.getTransfereeCardNum();

        Bankcard transferorAccount = bankCardService.getOne(
                new LambdaQueryWrapper<Bankcard>()
                        .eq(Bankcard::getNumber, transferorCardNum)
                        .select(Bankcard::getId, Bankcard::getBalance));
        Long transferorAccountBalance = transferorAccount.getBalance();
        if (transferorAccountBalance < TransferConstant.ZERO_BALANCE) {
            throw new BizException(BizExceptionConstant.ACCOUNT_BALANCE_NOT_ENOUGH);
        }

        Bankcard transfereeAccount = bankCardService.getOne(
                new LambdaQueryWrapper<Bankcard>()
                        .eq(Bankcard::getNumber, transfereeCardNum)
                        .select(Bankcard::getId, Bankcard::getBalance));
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
    }

    /**
     * 新增交易记录
     *
     * @param transfer
     */

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

//    /**
//     * 获取电子回单
//     *
//     * @param receiptDTO
//     * @return
//     */
//    @Override
//    public PageResponseResult getHistoryRecord(DigitalReceiptDTO receiptDTO) {
//        Page<Transfer> page = new Page<>(receiptDTO.getStartPage(), receiptDTO.getPageNum());
//        LambdaQueryWrapper<Transfer> lqw = Wrappers.lambdaQuery(Transfer.class)
//                .eq(Transfer::getTransferorCardNum, receiptDTO.getCardNum())
//                .eq(Transfer::getType, receiptDTO.getTransferType())
//                .eq(receiptDTO.getChannelType() != null,
//                        Transfer::getChannel,
//                        receiptDTO.getChannelType())
//                .between(
//                        receiptDTO.getStartTime() != null && receiptDTO.getEndTime() != null,
//                        Transfer::getCreateTime,
//                        receiptDTO.getStartTime(),
//                        receiptDTO.getEndTime())
//                .orderByDesc(Transfer::getCreateTime);
////这里返回回总条数和页数
//        baseMapper.selectList(page, lqw);
//        if (CollUtil.isEmpty(page.getRecords())) {
//            throw new BizException(BizExceptionConstant.TRANSFER_RECORD_SELECT_FAILED);
//        }
//        return PageResponseResult.success(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
//    }
//
//    /**
//     * 验证电子回单
//     *
//     * @param orderNum
//     * @param name
//     * @return
//     */
//    @Override
//    public Boolean validateHistoryRecord(String orderNum, String name) {
//        Transfer transfer = baseMapper.selectOne(
//                Wrappers.lambdaQuery(Transfer.class)
//                        .eq(Transfer::getOrderNum, orderNum)
//                        .eq(Transfer::getTransfereeName, name));
//        return transfer != null;
//    }

    /**
     * 查询二维码（付款码方式）订单状态
     *
     * @param orderId
     * @return
     */
    @Override
    public Integer queryOrderStatus(Long orderId) {
        Transfer transfer = baseMapper.selectOne(Wrappers.lambdaQuery(Transfer.class).eq(Transfer::getId, orderId).select(Transfer::getStatus));
        if (transfer == null) {
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }
        return transfer.getStatus();
    }

//    /**
//     * 付款码被扫方设置订单状态
//     *
//     * @param transfer
//     */
//    @Override
//    @Transactional
//    public void setQRCodeOrderStatus(Transfer transfer) {
//        String redisQRCodeKey = RedisConstant.TRANSACTION_QR_CODE_ORDER_REDIS_PREFIX
//                + transfer.getTransferorCardNum();
//        Object redisId = RedisUtil.get(redisQRCodeKey);
//        RedisUtil.del(redisQRCodeKey);
//        if (redisId == null) {
//            return;
//        }
//        Integer status = transfer.getStatus();
//        Integer money = transfer.getMoney();
//        if (TransferConstant.SUCCESS_FINISH.compareTo(status) == 0) {// 成功结束
//            // 转账
//            transfer = baseMapper.selectById((Serializable) redisId);
//            doTransfer(status, money, transfer);
//        } else if (TransferConstant.FAILED_FINISH.compareTo(status) == 0) {// 异常结束
//            // 修改状态
//            transfer.setStatus(status);
//        }
//        // 插入交易记录
//        insertHistory(transfer);
//    }
//
//    /**
//     * 二维码付款码主扫方关闭订单
//     *
//     * @param transfer
//     */
//    @Override
//    public void completeQRCodeOrder(Transfer transfer) {
//        String redisQRCodeKey = RedisConstant.TRANSACTION_QR_CODE_ORDER_REDIS_PREFIX
//                + transfer.getTransferorCardNum();
//        Object redisId = RedisUtil.get(redisQRCodeKey);
//        RedisUtil.del(redisQRCodeKey);
//        if (redisId == null) {
//            return;
//        }
//        // 更新状态
//        baseMapper.updateById(transfer);
//        // 插入交易记录
//        insertHistory(transfer);
//    }
//
//    /**
//     * 获取收款码 一分钟一次
//     *
//     * @param bankcardId
//     * @return
//     */
//    @Override
//    public String getCreditCode(Long bankcardId) {
//        // 卡号
//        Bankcard bankcard = bankCardService.getOne(Wrappers.lambdaQuery(Bankcard.class)
//                .eq(Bankcard::getId, bankcardId)
//                .select(Bankcard::getNumber, Bankcard::getUserId));
//        // 户名
//        UserInfo userInfo = Db.lambdaQuery(UserInfo.class).eq(UserInfo::getId, bankcard.getId()).select(UserInfo::getName).one();
//
//        String number = bankcard.getNumber();
//        String name = userInfo.getName();
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("transfereeCardNum", number);
//        jsonObject.put("transferorName", name);
//
//        return QRCodeUtil.genCode(jsonObject.toJSONString());
//    }
//
//    /**
//     * 获取付款码 一分钟一次
//     *
//     * @param bankcardId
//     * @return
//     */
//    @Override
//    public String getRemitCode(Long bankcardId) {
//        // 卡号
//        Bankcard bankcard = bankCardService.getOne(
//                Wrappers.lambdaQuery(Bankcard.class)
//                        .eq(Bankcard::getId, bankcardId)
//                        .select(Bankcard::getNumber, Bankcard::getUserId));
//        // 户名
//        UserInfo userInfo = Db.lambdaQuery(UserInfo.class).eq(UserInfo::getId, bankcard.getId()).select(UserInfo::getName).one();
//
//        String number = bankcard.getNumber();
//        String name = userInfo.getName();
//
//        UUID uuid = UUID.randomUUID();
//
//        String redisKey = RedisConstant.TRANSACTION_QR_CREDIT_CODE_PREFIX + uuid;
//        RedisUtil.setWithExpire(redisKey, uuid, 60, TimeUnit.SECONDS);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("transferorCardNum", number);
//        jsonObject.put("transferorName", name);
//        jsonObject.put("uuid", uuid);
//
//
//        return QRCodeUtil.genCode(jsonObject.toJSONString());
//    }

    /**
     * 设置转账订单信息
     * @param transfer
     */
    private static void setTransferInfo(Transfer transfer) {
        //设置备注
        if (transfer.getRemark() == null) {
            transfer.setRemark(TransferConstant.DEFAULT_REMARK);
        }
        //设置手续费
        transfer.setPremium(TransferConstant.DEFAULT_PREMIUM);
        //设置订单状态
        transfer.setStatus(TransferConstant.ONGOING_STATUS);
        //设置渠道
        transfer.setChannel(BizConstant.PHONE_BANK_CHANNEL);
        //设置渠道
        transfer.setMoney(BizConstant.DEFAULT_MONEY);
        //设置订单号
        transfer.setOrderNum(genOrderNum(transfer));
    }

    /**
     * 生成唯一订单号
     *
     * @param transfer
     * @return
     */
    @NotNull
    private static String genOrderNum(Transfer transfer) {
        //根据转账人卡号生成redis键
        String redisOrderKey = RedisConstant.TRANSACTION_ORDER_NUM_REDIS_PREFIX
                + transfer.getType()
                + transfer.getTransferorCardNum();
        String newOrderNum = null;
        //TODO 限额处理
        while (true) {
            Object orderRedisNum = RedisUtil.get(redisOrderKey);
            if (orderRedisNum == null) {//这一秒还没有生成过订单
                //存入缓存
                RedisUtil.setWithExpire(redisOrderKey, 0, 1, TimeUnit.SECONDS);
                //生成订单号 0-231226-00000-00001 0表示交易类型  231266表示年 17位
                newOrderNum = DomainBizUtil.genOrderNum(transfer.getType(), 0);
                //乐观锁
                if ((Integer) RedisUtil.get(redisOrderKey) == 0) {
                    break;
                }
            } else {//生成过订单 ,就只加一
                //生成订单号 0-231226-00000-00001 0表示交易类型  231266表示年 17位
                newOrderNum = DomainBizUtil.genOrderNum(transfer.getType(), (Integer) orderRedisNum);
                //乐观锁
                if (RedisUtil.get(redisOrderKey) == orderRedisNum) {
                    RedisUtil.set(redisOrderKey, (Integer) orderRedisNum + 1);
                    break;
                }
            }
        }
        return newOrderNum;
    }
    private void insertHistory(Transfer transfer) {
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .orderNum(transfer.getOrderNum())
                .type(transfer.getType())
                .result(transfer.getStatus())
                .transferorName(transfer.getTransferorName())
                .transfereeName(transfer.getTransfereeName())
                .transferorNum(transfer.getTransferorCardNum())
                .transfereeNum(transfer.getTransfereeCardNum())
                .transfereeBankName(BizConstant.BANK_NAME)
                .channel(BizConstant.PHONE_BANK_CHANNEL)
                .remark(transfer.getRemark())
                .premium(transfer.getPremium())
                .money(transfer.getMoney())
                .build();
        transactionHistoryService.save(transactionHistory);
    }

}
