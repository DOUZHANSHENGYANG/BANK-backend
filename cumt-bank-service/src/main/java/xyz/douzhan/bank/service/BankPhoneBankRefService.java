package xyz.douzhan.bank.service;

import xyz.douzhan.bank.dto.AliasDTO;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface BankPhoneBankRefService extends IService<BankPhoneBankRef> {
    /**
     * 查询总资产
     *
     * @return
     */
    Long getAsset();


    /**
     * 比较手机银行交易密码
     *
     * @param bankcardId
     * @param payPwd
     */
    void comparePayPwd(Long bankcardId, String payPwd);

    /**
     * 查询已绑定的银行卡账户id
     *
     * @return
     */
    List<Long> getBankcardIds();

    /**
     * 绑定默认卡
     * @param oldBankcardId
     * @param newBankcardId
     */
    void bindDefaultCard(Long oldBankcardId, Long newBankcardId);

    /**
     * 设别名
     * @param aliasDTO
     */
    void setAlias(AliasDTO aliasDTO);
}
