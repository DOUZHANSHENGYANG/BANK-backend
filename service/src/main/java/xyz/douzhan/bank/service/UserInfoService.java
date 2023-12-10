package xyz.douzhan.bank.service;

import xyz.douzhan.bank.po.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 根据手机银行id获取用户信息
     * @param id
     * @return
     */
    UserInfo getInfo(Long id);
}
