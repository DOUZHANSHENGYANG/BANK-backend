package xyz.douzhan.bank.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:
 * date: 2023/12/20 21:17
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "交易明细VO")
public class TransactionDetailsVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单交易号")
    private String orderNum;

    @Schema(description = "收支 0收入1支出")
    private Integer ie;

    @Schema(description = "交易类型 0本行装账")
    private Integer type;

    @Schema(description = "付款人姓名")
    @TableField("transferor_name")
    private String transferorName;

    @Schema(description = "付款方账号")
    @TableField("transferor_num")
    private String transferorNum;


    @Schema(description = "收款人姓名")
    @TableField("transferee_name")
    private String transfereeName;

    @Schema(description = "收款方账号")
    @TableField("transferee_num")
    private String transfereeNum;


    @Schema(description = "收款人银行名称")
    @TableField("transferee_bank_name")
    private String transfereeBankName;

    @Schema(description = "转账渠道（0线下1手机银行）")
    @TableField("channel")
    private Integer channel;

    @Schema(description = "交易金额")
    private Integer money;

    @Schema(description = "交易备注（默认转账）")
    private String remark;

    @Schema(description = "手续费（默认0.00）")
    private Integer premium;

    @Schema(description = "交易日期")
    private LocalDateTime createTime;
}
