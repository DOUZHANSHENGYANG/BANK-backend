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
@TableName("bank_card")
@Schema(description = "BankCard对象")
public class BankCard implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "银行卡id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "客户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "对应账户id")
    @TableField("account_id")
    private Long accountId;

    @Schema(description = "对应账户类型 0为I类 1为II 类")
    @TableField("account_type")
    private String accountType;

    @Schema(description = "卡类型（01借记卡10信用卡）	借记卡按功能不同分为转账卡、专用卡、储值卡（预付钱包式借记卡）。借记卡不能透支。	信用卡又分为贷记卡（直接可透支）和准贷记卡（先存钱才透支）	卡类 余额上限 单笔支付上限 日累计支付限额 年	   	")
    @TableField("type")
    private String type;

    @Schema(description = "状态（0正常1锁定2睡眠3挂失4注销） 正常状态：可以正常进行存取款、消费等交易活动。	挂失状态：银行卡处于挂失状态，无法进行任何交易活动。销户状态：银行卡已注销，无法进行任何交易活动。锁定状态：由于密码输入错误等原因导致银行卡被锁定，无法进行任何交易活动。睡眠状态：银行卡长时间未使用，且余额为0，需要激活后才能继续使用。")
    @TableField("status")
    private String status;

    @Schema(description = "卡号 前4固定 2代表种类 10代表自动生成 ")
    @TableField("number")
    private String number;

    @Schema(description = "是否是实体卡 0为实体 1为电子虚拟 ")
    @TableField("medium")
    private String medium;

    @Schema(description = "金额")
    @TableField("money")
    private String money;

    @Schema(description = "别名（用户自定义）")
    @TableField("alias")
    private String alias;

    @Schema(description = "输入支付密码错误次数 超过3次 自动锁定（可选）")
    @TableField("times")
    private LocalDateTime times;

    @Schema(description = "锁定的时间（可选）")
    @TableField("locked_time")
    private LocalDateTime lockedTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
