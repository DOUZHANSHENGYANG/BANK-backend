package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import xyz.douzhan.bank.handler.EncodeTypeHandler;

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
@TableName(value = "transfer")
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
    @TableField("status")
    private Integer status;

    @Schema(description = "订单号 0-231226-00000-0001 0表示交易类型 231266表示年月日 00000表示秒 最后五位表示订单号")
    @TableField("order_num")
    private String orderNum;

    @Schema(description = "转账人姓名")
    @TableField(value = "transferor_name")
    private String transferorName;

    @Schema(description = "转账人卡号")
    @TableField(value = "transferor_card_num")
    private String transferorCardNum;


    @Schema(description = "收款人姓名")
    @TableField(value = "transferee_name")
    private String transfereeName;

    @Schema(description = "收款人卡号")
    @TableField(value = "transferee_card_num")
    private String transfereeCardNum;

    @Schema(description = "金额")
    @TableField("money")
    private Integer money;

    @Schema(description = "渠道,0个人网银 1手机银行")
    @TableField(value = "channel")
    private Integer channel;

    @Schema(description = "手续费")
    @TableField("premium")
    private Integer premium;

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
