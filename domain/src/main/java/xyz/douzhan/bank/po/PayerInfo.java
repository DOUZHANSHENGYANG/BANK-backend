package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import xyz.douzhan.bank.enums.BankCardStatus;
import xyz.douzhan.bank.enums.CardType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:管理员实体类
 * date: 2023/11/24 20:54
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Builder
@TableName("payer_info")
public class PayerInfo implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private String userId;
    //银行卡号
    private String bankNum;
    //名字
    private String name;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
