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
 * date: 2023/12/23 15:23
 *
 * @author 斗战圣洋
 * @since JDK 17
 */


@Getter
@Setter
@TableName(value = "limit_money")
@Schema(description = "限额表")
public class Limit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "限额id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "银行卡账号id")
    @TableField("bankcard_id")
    private Long bankcardId;

    @Schema(description = "类型")
    @TableField(value = "type")
    private Integer type;

    @Schema(description = "")
    @TableField("day_in")
    private Integer dayIn;

    @Schema(description = "")
    @TableField("day_out")
    private Integer dayOut;

    @Schema(description = "")
    @TableField("year_in")
    private Integer yearIn;

    @Schema(description = "")
    @TableField("year_out")
    private Integer yearOut;


    @Schema(description = "更新日期")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}

