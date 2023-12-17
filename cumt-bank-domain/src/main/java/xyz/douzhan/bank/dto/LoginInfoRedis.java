package xyz.douzhan.bank.dto;

import cn.hutool.log.Log;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/8 19:26
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Accessors(fluent = true,chain = true)
public class LoginInfoRedis {
    /**
     * 用户名
     */
    private Long phoneAccountId;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录地
     */
    private String loginLocation;
}
