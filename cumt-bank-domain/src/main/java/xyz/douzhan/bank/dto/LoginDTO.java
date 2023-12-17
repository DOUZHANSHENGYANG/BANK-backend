package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/7 0:01
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "人脸认证DTO")
public class LoginDTO {
    @Schema(description = "登录方式统一图形验证码  0为手机号短信码 1为手机号密码 2为证件号密码")
    private Integer type;
//    @Pattern(regexp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])|(18[0-9])|(19[8,9]))\\d{8}$")
    @Schema(description = "手机号")
    private String phoneNumber;
    @Schema(description = "短信验证码")
    private String smsCode;
//    @Pattern(regexp = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")
    @Schema(description = "证件号码")
    private String documentsNumber;
    @Schema(description = "密码")
    private String password;
}
