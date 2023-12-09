package xyz.douzhan.bank.service;

import xyz.douzhan.bank.po.BankPhoneBankRef;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.douzhan.bank.vo.AccountVO;

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
     * 根据id查询资产
     *
     * @param id
     * @return
     */
    String getAsset(Long id);

    /**
     * 根据id查询总资产
     *
     * @param id
     * @return
     */
    List<AccountVO> getAccount(Long id);
}
