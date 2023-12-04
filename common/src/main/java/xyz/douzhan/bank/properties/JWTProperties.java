package xyz.douzhan.bank.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 一些声明信息
 * Description:jwt配置属性类
 * date: 2023/12/2 14:39
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Data
@ConfigurationProperties(prefix = "bank.jwt")
public class JWTProperties {
    private String tokenName="Authorization";
    private Integer ttl= 3650;
    private String userInfo="userInfo";


}
