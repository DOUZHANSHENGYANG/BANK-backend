package xyz.douzhan.bank.service.impl;

import xyz.douzhan.bank.po.TransactionHistory;
import xyz.douzhan.bank.mapper.TransactionHistoryMapper;
import xyz.douzhan.bank.service.TransactionHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-11
 */
@Service
public class TransactionHistoryServiceImpl extends ServiceImpl<TransactionHistoryMapper, TransactionHistory> implements TransactionHistoryService {

}
