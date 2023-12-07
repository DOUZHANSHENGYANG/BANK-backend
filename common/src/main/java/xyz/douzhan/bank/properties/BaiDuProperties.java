package xyz.douzhan.bank.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 一些声明信息
 * Description:百度第三方配置属性类
 * date: 2023/12/7 20:02
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Data
@ConfigurationProperties(prefix = "bank.baidu")
public class BaiDuProperties {
    private String AppID="44513880";
    private String APIKey="YXtFzXr6ssK8eZoPPDKDTi2w";
    private String SecretKey ="y9GaO27dkLTPHnKcvxrcXbxSpU6892Ox";

}
