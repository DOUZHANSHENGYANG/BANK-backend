package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xyz.douzhan.bank.po.Transfer;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/11 10:13
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "前置交易DTO")
public class TransactionDTO {
    @Schema(description = "交易实体")
    private Transfer transfer;

    @Schema(description = "手机账户id")
    private Long phoneAccountId;


}
