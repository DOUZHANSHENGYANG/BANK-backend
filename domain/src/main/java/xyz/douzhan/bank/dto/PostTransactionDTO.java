package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/11 12:21
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "后置交易DTO")
public class PostTransactionDTO {
    @Schema(description = "手机账户id")
    private String phoneNumber;
    @Schema(description = "短信验证码")
    private String smsCode;
    @Schema(description = "交易id")
    private String transactionId;
}
