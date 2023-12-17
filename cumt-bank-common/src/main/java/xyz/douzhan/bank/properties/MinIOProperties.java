package xyz.douzhan.bank.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * date: 2023/12/17 15:38
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Component
@ConfigurationProperties(prefix = "bank.minio")
public class MinIOProperties {
    private String endpoint;
    private String documentsBucketName;
    private String secretKey;
    private String accessKey;
}
