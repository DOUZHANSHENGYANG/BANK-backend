package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-11
 */
@Getter
@Setter
@Builder
@TableName("transaction_history")
@Schema(description = "TransactionHistory对象")
public class TransactionHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "交易id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "交易号")
    @TableField("transfer_num")
    private String transferNum;

    @Schema(description = "交易类型")
    @TableField("type")
    private Integer type;

    @Schema(description = "交易结果（0成功1失败）")
    @TableField("result")
    private Integer result;

    @Schema(description = "付款人姓名")
    @TableField("transferor_name")
    private String transferorName;

    @Schema(description = "付款方账号（卡号）")
    @TableField("transferor_account")
    private String transferorAccount;

    @Schema(description = "收款人姓名")
    @TableField("transferee_name")
    private String transfereeName;

    @Schema(description = "收款方账号（卡号）")
    @TableField("transferee_account")
    private String transfereeAccount;

    @Schema(description = "收款人银行名称")
    @TableField("transferee_bank_name")
    private String transfereeBankName;

    @Schema(description = "转账渠道（0线下1手机银行）")
    @TableField("channel")
    private Integer channel;

    @Schema(description = "交易备注（默认转账）")
    @TableField("remark")
    private String remark;

    @Schema(description = "手续费（默认0.00）")
    @TableField("premium")
    private Double premium;

    @Schema(description = "交易日期")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
