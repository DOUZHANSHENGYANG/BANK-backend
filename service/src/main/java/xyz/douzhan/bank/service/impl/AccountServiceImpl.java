package xyz.douzhan.bank.service.impl;

import org.springframework.beans.BeanUtils;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.mapper.AccountMapper;
import xyz.douzhan.bank.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.BizUtils;
import xyz.douzhan.bank.vo.AccountVO;

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

    @Override
    public AccountVO getAccountInfo(Long id) {
        //非空判断
        BizUtils.assertArgsNotNull(id);
        //根据id查询账户信息
        Account account = this.baseMapper.selectById(id);
        //非空判断
        BizUtils.assertArgsNotNull(account);
        //复制
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account,accountVO);
        accountVO.setAccountId(account.getId());
        return accountVO;
    }
}
