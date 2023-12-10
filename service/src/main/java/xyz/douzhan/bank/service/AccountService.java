package xyz.douzhan.bank.service;

import com.alibaba.fastjson2.JSONObject;
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

    /**
     * 根据手机账户id判断查询账户卡号
     *
     * @param id
     * @param type
     * @return
     */
    JSONObject getAccountNumber(Long id, Integer type);

    /**
     * 创建II III类账户
     *
     * @param account
     * @param phoneAccountId
     */
    void createAccount(Account account, Long phoneAccountId);
}
