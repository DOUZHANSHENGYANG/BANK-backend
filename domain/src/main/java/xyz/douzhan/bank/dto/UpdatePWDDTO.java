package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/9 13:43
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "密码对象 修改重置共用")
public class UpdatePWDDTO {
    @Schema(description = "用户账号id")
    private Long id;
    @Schema(description = "类型 0为账户密码 1为支付密码")
    private Integer type;
    @Schema(description = "新密码")
    private String password;
    @Schema(description = "旧密码")
    private String oldPassword;
}
