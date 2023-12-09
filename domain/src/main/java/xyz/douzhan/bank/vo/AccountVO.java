package xyz.douzhan.bank.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xyz.douzhan.bank.enums.AccountStatus;
import xyz.douzhan.bank.enums.AccountType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/9 15:49
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "账户VO")
public class AccountVO {
    @Schema(description = "对应银行账户id")
    private Long accountId;

    @Schema(description = "对应账户状态")
    private AccountStatus status;

    @Schema(description = "卡号")
    private String bankcardNum;

    @Schema(description = "个人银行借记账户类型 分为I类 II类 III类 	分别用数字0,1,2标识")
    private AccountType type;

    @Schema(description = "类别名字")
    private String typeName;

    @Schema(description = "账户别名 可为空")
    private String alias;

    @Schema(description = "可用账户余额 不同类型账户余额上限不同")
    private String balance;

    @Schema(description = "是否为默认账户 可为空  0不是 1是")
    private String isDefault;

    @Schema(description = "开户行名称 例如（中国建议银行深圳（分行）南山支行）")
    private String bankName;

    @Schema(description = "开户日期")
    @JsonFormat(pattern = "yyyy年MM月dd日",timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "签约账户")
    private String contractedAccount;
}
