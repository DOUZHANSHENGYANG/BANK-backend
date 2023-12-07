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
@TableName("bank_phone_bank_ref")
@Schema(description = "BankPhoneBankRef对象" )
public class BankPhoneBankRef implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "银行账号手机账号表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "银行账号id")
//    @TableId("account_id")
    private Integer accountId;

    @Schema(description = "手机账号id")
//    @TableId("phone_account_id")
    private Integer phoneAccountId;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Schema(description = "头像url")
    @TableField("avatar")
    private String avatar;
}
