package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/6 21:47
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Schema(description = "用户名密码登录DTO")
public class UserNamePWDLoginDTO {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "验证码唯一标识")
    private String uuid;
    @Schema(description = "图形验证码")
    private String imgCode;
}
