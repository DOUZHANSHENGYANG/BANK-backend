package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import xyz.douzhan.bank.enums.BankCardStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:反馈建议信息
 * date: 2023/11/24 21:06
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Builder
@TableName("feedback")
public class FeedBack implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //类型 0反馈1建议
    private Integer type;
    //描述
    private String description;
    //问题类别（0账户1转账2查询3便捷服务4其他）
    private String questionType;
    //截图url
    private String snapshot;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
