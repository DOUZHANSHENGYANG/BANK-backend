package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/6 22:55
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "手机号登录")
public class OCRDTO {
    @Schema(description = "类型 0为身份证")
    private Integer type;
    @Schema(description = "正反面 0为正面 1为反面 仅银行卡有效")
    private Integer side;
}
