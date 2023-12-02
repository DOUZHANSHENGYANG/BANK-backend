package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import xyz.douzhan.bank.enums.CardType;
import xyz.douzhan.bank.enums.TransactionStatus;
import xyz.douzhan.bank.enums.TranscationType;

import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:交易记录实体类
 * date: 2023/11/24 20:54
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Builder
@TableName("transactionn")
public class Transaction {
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //交易类型 (0支出1收入)
    private TranscationType type;
    //工号
    private String money;
    //交易状态（0交易进行中1交易成功2交易异常）
    private TransactionStatus status;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
