package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/6 21:56
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "验证码校验")
public class ValidateVerifyCodeDTO {
    @Schema(description = "0为图形验证码，1为短信验证码")
    private Integer type;
    @Schema(description = "图形验证码id")
    private String uuid;
    @Schema(description = "图形验证码")
    private String imgCode;
    @Schema(description = "手机号")
    private String phoneNumber;
    @Schema(description = "手机号短信验证码")
    private String smsCode;
}
