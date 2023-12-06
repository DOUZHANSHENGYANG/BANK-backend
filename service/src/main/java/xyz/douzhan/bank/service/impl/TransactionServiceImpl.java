package xyz.douzhan.bank.service.impl;

import xyz.douzhan.bank.po.Transaction;
import xyz.douzhan.bank.mapper.TransactionMapper;
import xyz.douzhan.bank.service.TransactionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {

}
