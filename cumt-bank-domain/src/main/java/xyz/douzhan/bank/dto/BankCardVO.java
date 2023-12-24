package xyz.douzhan.bank.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xyz.douzhan.bank.enums.BankcardStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:
 * date: 2023/12/17 13:37
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema
public class BankCardVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "银行卡id")
    private Long id;
    @Schema(description = "状态")
    private BankcardStatus status;
    @Schema(description = "对应账户类型 0 I类 1 II 类 2 III类")
    private Integer type;
    @Schema(description = "只有前八位和后四位，应该只展示后四位，卡号单独查，因为单独存储")
    private String number;
    @Schema(description = "余额 ")
    private Long balance;
    @Schema(description = "开户行机构 ")
    private String bankName;
    @Schema(description = "介质 0没有实体 1有实体 ")
    private Integer medium;
    @Schema(description = "绑定账户id 二类卡可选绑定 三类卡必须绑定 电子银行必须绑定")
    private Long bankcardId;
    @Schema(description = "开户日期")
    private LocalDateTime createTime;
    @Schema(description = "0 为默认 1不是")
    private Integer defaultAccount;
    @Schema(description = "有则非空 没有就为空")
    private String alias;
}
