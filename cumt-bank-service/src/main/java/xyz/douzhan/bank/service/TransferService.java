package xyz.douzhan.bank.service;

import xyz.douzhan.bank.dto.DigitalReceiptDTO;
import xyz.douzhan.bank.dto.result.PageResponseResult;
import xyz.douzhan.bank.po.Transfer;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.douzhan.bank.vo.SupportBankVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface TransferService extends IService<Transfer> {


    /**
     * 获取支持的转账银行
     * @return
     */
    List<SupportBankVO> getSupportBank();

    /**
     * 创建转账订单
     */
    Long createOrder(Transfer transfer) ;

    /**
     * 完成订单
     *
     * @param orderId
     * @param status
     */
    void completeOrder(Long orderId, Integer status);

    /**
     * 获取电子回单
     *
     * @param receiptDTO
     * @return
     */
//    PageResponseResult getHistoryRecord(DigitalReceiptDTO receiptDTO);

    /**
     * 验证电子回单
     * @param orderNum
     * @param name
     * @return
     */
//    Boolean validateHistoryRecord(String orderNum, String name);

    /**
     * 查询二维码订单状态
     *
     * @param orderId
     * @return
     */
    Integer queryOrderStatus(Long orderId);

//    /**
//     * 二维码被扫方设置订单状态
//     *
//     * @param transfer
//     */
//    void setQRCodeOrderStatus(Transfer transfer);
//
//    /**
//     * 二维码付款码主扫方关闭订单
//     *
//     * @param transfer
//     */
//    void completeQRCodeOrder(Transfer transfer);
//
//    /**
//     * 获取收款码 一分钟一次
//     *
//     * @param bankcardId
//     * @return
//     */
//    String getCreditCode(Long bankcardId);
//
//    /**
//     * 获取付款码 一分钟一次
//     *
//     * @param bankcardId
//     * @return
//     */
//    String getRemitCode(Long bankcardId);
}
