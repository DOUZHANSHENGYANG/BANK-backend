package xyz.douzhan.bank.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 一些声明信息
 * Description:银行业务字配置属性类
 * date: 2023/12/10 17:20
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Data
@ConfigurationProperties(prefix = "bank.biz")
public class BizProperties {
    private String swiftCode="CUMTCNBN001";//两位需要取机构代码前两位
    private String bankName="CUMT银行徐州（分行）铜山支行";
    private String institutionCode="00001";
    private String bankAddress="中国江苏省徐州市铜山区中国矿业大学学苑南路";
    private List<String> IIN=List.of("622333");
    private String regionCode="110";
}
