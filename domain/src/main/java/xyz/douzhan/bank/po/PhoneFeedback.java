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
@TableName("phone_feedback")
@Schema(description = "PhoneFeedback对象")
public class PhoneFeedback implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "问题反馈建议id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description ="类型（0反馈1建议）")
    @TableField("type")
    private String type;

    @Schema(description ="描述")
    @TableField("description")
    private String description;

    @Schema(description ="截图url")
    @TableField("snapshot")
    private String snapshot;

    @Schema(description ="问题类别（0账户1转账2查询3便捷服务4其他）")
    @TableField("question_type")
    private String questionType;

    @Schema(description ="创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description ="更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
}
