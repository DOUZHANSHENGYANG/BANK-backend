package xyz.douzhan.bank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/6 23:11
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "证件信息VO")
public class BankCardVO {
    @Schema(description = "卡号")
    private String cardNumber;
    @Schema(description = "银行描述")
    private String bankDescription;
}
