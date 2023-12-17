package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description:
 * date: 2023/12/16 0:13
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
@Setter
@TableName("bank_info")
@Schema(description = "开户行实体对象")
public class BankInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "开户行id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "银行名字")
    @TableField(value = "bank_name")
    private String name;

    @Schema(description = "机构代码")
    @TableField(value = "institution_code")
    private String institutionCode;

    @Schema(description = "银行详细地址")
    @TableField(value = "bank_address")
    private String address;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
