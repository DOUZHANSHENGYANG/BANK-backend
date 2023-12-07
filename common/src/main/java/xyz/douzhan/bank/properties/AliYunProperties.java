package xyz.douzhan.bank.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 一些声明信息
 * Description: 阿里云服务配置属性类
 * date: 2023/12/7 18:06
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Data
@ConfigurationProperties(prefix = "bank.aliyun")
public class AliYunProperties {
    private String accessKeyID="LTAI5tFMX5tTipQqFoyTpdGf";
    private String accessKeySecret="Bsvfp2DFINWN4cA0rFSexuXm6beAk3";
}
