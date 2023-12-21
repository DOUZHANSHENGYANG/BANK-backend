package xyz.douzhan.bank.service;

import xyz.douzhan.bank.dto.TransactionDetailsDTO;
import xyz.douzhan.bank.dto.result.PageResponseResult;
import xyz.douzhan.bank.po.TransactionHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-11
 */
public interface TransactionHistoryService extends IService<TransactionHistory> {

    /**
     * 获取交易明细
     * @param detailsDTO
     * @return
     */
    PageResponseResult getTransactionDetails(TransactionDetailsDTO detailsDTO);
}
