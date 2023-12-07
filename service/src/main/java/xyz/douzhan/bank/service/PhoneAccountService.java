package xyz.douzhan.bank.service;

import xyz.douzhan.bank.dto.RegisterDTO;
import xyz.douzhan.bank.po.PhoneAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface PhoneAccountService extends IService<PhoneAccount> {
    /**
     * 注册手机账户
     * @param registerDTO
     * @return
     */
    Long register(RegisterDTO registerDTO);
}
