package xyz.douzhan.bank.security.user;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 * 一些声明信息
 * Description:JWT验证redis中存储的用户信息
 * date: 2023/11/28 19:54
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Accessors(fluent = true,chain = true)
public class JWTUserRedis implements Serializable {
    /**
     * 安全信息id
     */
    private Long safeId;

    /**
     * 权限集合
     */
    private Collection<? extends GrantedAuthority> authorities;

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
