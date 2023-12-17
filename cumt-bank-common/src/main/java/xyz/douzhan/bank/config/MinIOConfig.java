package xyz.douzhan.bank.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.douzhan.bank.properties.MinIOProperties;

/**
 * Description:
 * date: 2023/12/17 15:52
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Configuration
public class MinIOConfig {
    private final MinIOProperties minIOProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minIOProperties.getEndpoint())
                .credentials(minIOProperties.getAccessKey(), minIOProperties.getSecretKey())
                .build();
    }
}
