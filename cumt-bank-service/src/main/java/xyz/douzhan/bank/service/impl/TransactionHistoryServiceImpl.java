package xyz.douzhan.bank.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.dto.TransactionDetailsDTO;
import xyz.douzhan.bank.mapper.TransactionHistoryMapper;
import xyz.douzhan.bank.po.TransactionHistory;
import xyz.douzhan.bank.service.TransactionHistoryService;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-11
 */
@Service
public class TransactionHistoryServiceImpl extends ServiceImpl<TransactionHistoryMapper, TransactionHistory> implements TransactionHistoryService {



    /**
     * 获取交易明细
     * @param detailsDTO
     * @return
     */
    @Override
    public List<TransactionHistory> getTransactionDetails(TransactionDetailsDTO detailsDTO) {
        return null;
    }
}
