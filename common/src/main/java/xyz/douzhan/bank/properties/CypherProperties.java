package xyz.douzhan.bank.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 一些声明信息
 * Description: 密钥配置类
 * date: 2023/12/2 15:00
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Data
@ConfigurationProperties(prefix = "bank.cypher")
public class CypherProperties {
    //jks文件类型
    private String fileType="jks";
    //文件位置
    private String Location="classpath:demo.jks";
    //文件密码
    private String storePass="654321";
    //密钥别名
    private String alias="demo";
    //查看密钥对的私钥密码，公钥进入文件即可查看
    private String keyPass="123456";
}
