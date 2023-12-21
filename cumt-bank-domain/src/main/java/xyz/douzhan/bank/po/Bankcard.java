package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import xyz.douzhan.bank.enums.BankcardStatus;
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
@TableName(value = "bankcard")
@Schema(description = "BankCard对象")
public class Bankcard implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "银行卡id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "客户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "状态")
    @TableField("status")
    private BankcardStatus status;

    @Schema(description = "对应账户类型 0 I类 1 II 类 2 III类")
    @TableField("type")
    private Integer type;


    @Schema(description = "卡号 留着前八位和后四位 卡号 62 23 33（IIN）+ 12位 +（2位业务种类，3为地区代码110 ，8位账号顺序号）+ 1位校验位（Luhnalgorithm生成）存储截断版本 hash版本加")
    @TableField(value = "number")
    private String number;


    @Schema(description = "余额 ")
    @TableField("balance")
    private Long balance;


    @Schema(description = "支付密码 ")
    @TableField(value = "password")
    private String password;

    @Schema(description = "开户预留手机号")
    @TableField(value = "phone_number")
    private String phoneNumber;

    @Schema(description = "开户行id ")
    @TableField("bank_info_id")
    private Integer bankInfoId;

    @Schema(description = "介质 0没有实体 1有实体 ")
    @TableField("medium")
    private Integer medium;

    @Schema(description = "用途")
    @TableField("purpose")
    private String purpose;

    @Schema(description = "绑定账户id")
    @TableField("bankcard_id")
    private Long bankcardId;

    @Schema(description = "乐观锁")
    @TableField("version")
    @Version
    private Integer version;

    @Schema(description = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除字段")
    @TableLogic
    private Integer deleted;
}
