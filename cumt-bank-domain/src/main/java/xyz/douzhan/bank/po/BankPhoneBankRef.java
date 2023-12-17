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
@TableName("bank_phone_bank_ref")
@Schema(description = "BankPhoneBankRef对象" )
public class BankPhoneBankRef implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "银行账号手机账号表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "银行账号id")
    private Long accountId;

    @Schema(description = "手机账号id")
    private Long phoneAccountId;

    @Schema(description ="账户类型")
    @TableField("type")
    private Integer type;

    @Schema(description ="手机银行账户密码")
    @TableField("pay_pwd")
    private String payPWD;

    @Schema(description = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "别名")
    @TableField("alias")
    private String alias;

    @Schema(description ="是否为默认账户 0为是 1为不是")
    @TableField("default_account")
    private Integer defaultAccount;

}
