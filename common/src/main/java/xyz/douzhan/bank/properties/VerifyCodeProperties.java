package xyz.douzhan.bank.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 一些声明信息
 * Description:验证码配置属性类
 * date: 2023/11/25 22:32
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Component
@ConfigurationProperties(prefix = "bank.verify-code")
public class VerifyCodeProperties {
    //长 宽 验证码字符数 干扰元素个数 干扰线宽度
    private int width=200;
    private int height=100;
    private int codeCount=4;
    private int circleCount=20;
    private int thickness=4;
}
