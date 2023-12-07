package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;;
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
@TableName("account")
@Schema(description = "Account对象")
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "银行账户数据库id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "客户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "银行账户标识符 ，一共12位 ，前缀位数、本体账号位数和校验码位数，前2为标识账户类型，中间8位自定义生成（自定义为主键id）后2位为校验位 (校验规则：将前10位数字与权重（3, 7, 9, 10, 5, 8, 4, 2, 1, 6）进行一一对应的相乘，并将乘积相加。用相加的结果除以11，并取余数。	用11减去余数得到校验码。如果余数为0，则校验码为0；如果余数为x，则校验码为X)")
    @TableField("identifier")
    private String identifier;

    @Schema(description = "个人银行账户状态 0开户1正常2冻结3注销4透支5休眠 	备注：开户是银行账户最原始的状态，银行账户基本处于正常使用状态。长时间不使用的银行账户会进入休眠状态，经过激活银行账户为正常使用状态;存在违规操作的银行账户会被冻结，经过解冻银行账户为正常使用状态;余额小于零时银行账户为透支状态，往银行账户中存入金额才会恢复正常使用状态。")
    @TableField("status")
    private String status;

    @Schema(description = "个人银行账户类型 分为I类 II类 IIl类 	分别用数字0,1,2标识")
    @TableField("type")
    private String type;

    @Schema(description = "银行账户余额 不同类型账户余额上限不同")
    @TableField("balance")
    private String balance;

    @Schema(description = "账户密码")
    @TableField("password")
    private String password;

    @Schema(description = "预留手机号")
    @TableField("phone_number")
    private String phoneNumber;

    @Schema(description = "开户行名称 例如（中国建议银行深圳（分行）南山支行）")
    @TableField("bank_name")
    private String bankName;

    @Schema(description = "开户行地址 详细地址")
    @TableField("bank_address")
    private String bankAddress;

    @Schema(description = "机构代码 5位 从1开始")
    @TableField("institution_code")
    private String institutionCode;

    @Schema(description = "银行账户变动最新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "银行账户开立时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "若为 II 类 可选绑定I类账户   III 类账户 必须绑定I类 手机银行都要绑定I类")
    @TableField("account_id")
    private Integer accountId;
}
