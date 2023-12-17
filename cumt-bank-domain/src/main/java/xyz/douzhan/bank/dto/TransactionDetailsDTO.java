package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/12 0:55
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "交易明细DTO")
public class TransactionDetailsDTO {
    @Schema(description = "收支类型 0全部 1收入 2支出 需要默认为0")
    private  Integer type;
    @Schema(description = "账户id")
    private Long accountId;
    @Schema(description = "开始时间")
    private  Integer startTime;
    @Schema(description = "结束时间 ")
    private  Integer endTime;
    @Schema(description = "查询页码")
    private  Integer startPage;
    @Schema(description = "查询条数")
    private  Integer pageNum;

}
