package xyz.douzhan.bank.service;

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
     * 转账（后置）
     *
     * @param transactionId
     * @param transactionId
     */
    Long createTransfer(Transfer transfer, Long transactionId) ;

    /**
     * 完成订单
     *
     * @param id
     * @param status
     */
    void complete(Long id, Integer status,Double money);
}
