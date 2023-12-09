package xyz.douzhan.bank.service;

import xyz.douzhan.bank.po.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.douzhan.bank.vo.AccountVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface AccountService extends IService<Account> {
    /**
     * 根据手机id获取账户信息
     *
     * @param id
     * @return
     */
    AccountVO getAccountInfo(Long id);
}
