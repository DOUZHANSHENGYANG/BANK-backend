package xyz.douzhan.bank.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 一些声明信息
 * Description:银行业务字配置属性类
 * date: 2023/12/10 17:20
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Getter
@ConfigurationProperties(prefix = "bank.biz")
public class BankProperties {
    String BANK_SWIFT_CODE="CUMTCNBN001";//两位需要取机构代码前两位
    String BANK_NAME="CUMT银行";
}
