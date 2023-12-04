package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 一些声明信息
 * Description: 登录实体
 * date: 2023/11/26 0:05
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "账号密码和手机号登录共用")
public class LoginDTO {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "验证码唯一标识")
    private String uuid;
    @Schema(description = "图形验证码")
    private String imgCode;


    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "短信验证码")
    private String smsCode;
}
