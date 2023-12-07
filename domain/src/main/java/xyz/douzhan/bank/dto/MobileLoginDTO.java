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
@Schema(description = "手机号登录")
public class MobileLoginDTO {
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "短信验证码")
    private String smsCode;
}
