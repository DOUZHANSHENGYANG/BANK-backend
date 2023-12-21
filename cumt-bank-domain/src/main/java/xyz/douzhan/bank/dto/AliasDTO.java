package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xyz.douzhan.bank.enums.BankcardStatus;

/**
 * Description:
 * date: 2023/12/21 9:41
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema
public class AliasDTO {
    @Schema(description = "银行卡id")
    private Long accountId;
    @Schema(description = "别名")
    private String alias;
}
