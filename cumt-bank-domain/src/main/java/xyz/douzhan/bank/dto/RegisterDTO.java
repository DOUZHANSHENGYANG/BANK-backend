package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/**
 * 一些声明信息
 * Description:注册DTO
 * date: 2023/12/4 15:26
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "姓名")
public class RegisterDTO {
//    @NotBlank(message = "名字不能为空")
    @Schema(description = "手机号码")
    private String phoneNumber;
    @Schema(description = "I类银行卡id")
    private Long firstBankcardId;
    @Schema(description = "证件号码")
    private String documentsNumber ;
    @Schema(description = "账户密码")
    private String accountPWD;
    @Schema(description = "支付密码")
    private String payPWD;
//    @Pattern(regexp = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",message = "身份证号格式错误")
//    @Schema(description = "身份证号")
//    private String ICNum;

}
