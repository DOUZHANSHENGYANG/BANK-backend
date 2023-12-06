package xyz.douzhan.bank.security.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
     * 手机账户id
     */
    private Long phoneAccountId;

    /**
     * 权限集合
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 登录时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
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
