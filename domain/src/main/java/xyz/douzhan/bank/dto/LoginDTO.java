package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "手机号")
    private String phoneNumber;
    @Schema(description = "短信验证码")
    private String smsCode;
    @Schema(description = "图像验证码")
    private String imgCode;
    @Schema(description = "证件号码")
    private String documentsNumber;
    @Schema(description = "密码")
    private String password;
}
