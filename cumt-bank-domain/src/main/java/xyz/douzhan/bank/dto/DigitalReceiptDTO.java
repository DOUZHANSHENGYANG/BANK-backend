package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/12 0:43
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "电子回单DTO")
public class DigitalReceiptDTO {
    @Schema(description = "渠道类型 0个人网银 1手机银行  ")
    private  Integer channelType ;
    @Schema(description = "交易类型 0行内转账 1跨行转账 必须0 ")
    private  Integer transferType;
    @Schema(description = "电子回单卡号")
    private String cardNum;
    @Schema(description = "开始时间")
    private LocalDate startTime;
    @Schema(description = "结束时间 ")
    private  LocalDate endTime;
    @Schema(description = "起始页码")
    private  Integer startPage;
    @Schema(description = "查询条数")
    private  Integer pageNum;
}
