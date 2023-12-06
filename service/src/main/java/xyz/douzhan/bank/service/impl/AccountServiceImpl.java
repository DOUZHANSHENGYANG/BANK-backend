package xyz.douzhan.bank.service.impl;

import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.mapper.AccountMapper;
import xyz.douzhan.bank.service.AccountService;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
