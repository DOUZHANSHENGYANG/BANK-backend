package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("transaction")
@Schema(description = "Transaction对象")
public class Transaction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "类型（0转账转入1装账转出2二维码转入3二维码转出）")
    @TableField("type")
    private String type;

    @Schema(description = "状态（0正在1成功结束2异常结束）")
    @TableField("state")
    private String state;

    @Schema(description = "订单号 0-231226-00000-0001 0表示交易类型 231266表示年月日 00000表示秒 最后五位表示订单号")
    @TableField("order_num")
    private String orderNum;

    @Schema(description = "转账人姓名")
    @TableField("transferor_account_name")
    private Integer transferorAccountName;

    @Schema(description = "转账人账户号")
    @TableField("transferor_account_identifier")
    private String transferorAccountIdentifier;

    @Schema(description = "被转账人姓名")
    @TableField("transferee_account_name")
    private Integer transfereeAccountName;

    @Schema(description = "被转账人账户号")
    @TableField("transferee_account_identifier")
    private String transfereeAccountIdentifier;

    @Schema(description = "金额")
    @TableField("money")
    private String money;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
}
