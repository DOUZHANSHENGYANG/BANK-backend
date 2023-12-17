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
@TableName("phone_payer")
@Schema(description= "PhonePayer对象" )
public class PhonePayer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description ="常用转账人id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description ="用户id")
    @TableField("user_id")
    private Integer userId;

    @Schema(description ="转账卡号")
    @TableField("bank_num")
    private String bankNum;

    @Schema(description ="转账人名字")
    @TableField("name")
    private String name;

    @Schema(description ="创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description ="更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
