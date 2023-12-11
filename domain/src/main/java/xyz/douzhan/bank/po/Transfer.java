package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Getter
@Setter
@TableName(value = "transfer",autoResultMap = true)
@Schema(description = "转账交易实体")
public class Transfer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "类型（0行内转账1行外装账2二维码转入3二维码转出）")
    @TableField("type")
    private Integer type;

    @Schema(description = "状态（0进行中1成功2异常失败）")
    @TableField("state")
    private Integer state;

    @Schema(description = "订单号 0-231226-00000-0001 0表示交易类型 231266表示年月日 00000表示秒 最后五位表示订单号")
    @TableField("order_num")
    private String orderNum;

    @Schema(description = "转账人姓名")
    @TableField("transferor_name")
    private String transferorName;

    @Schema(description = "转账人账户号")
    @TableField("transferor_account_id")
    private Long transferorAccountId;

    @Schema(description = "收款人姓名")
    @TableField("transferee_name")
    private String transfereeName;

    @Schema(description = "收款人账户号")
    @TableField("transferee_account_id")
    private Long transfereeAccountId;

    @Schema(description = "转账人银行名称")
    @TableField("transferor_bank_swiftcode")
    private String transferorBankSwiftCode;

    @Schema(description = "收款人银行名称")
    @TableField("transferee_bank_swiftcode")
    private String transfereeBankSwiftCode;

    @Schema(description = "金额")
    @TableField("money")
    private String money;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
