package xyz.douzhan.bank.po;//package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import xyz.douzhan.bank.enums.Role;
import xyz.douzhan.bank.handler.EncodeTypeHandler;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.List;

/**
 * 一些声明信息
 * Description:用户安全类
 * date: 2023/11/24 20:43
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Builder
@TableName(value = "safe", autoResultMap = true)
public class Safe implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //用户名
    private String username;
    //手机号
    private String tel;
    //账号密码
    @TableField(typeHandler = EncodeTypeHandler.class)
    private String accountPwd;
    //支付密码
    @TableField(typeHandler = EncodeTypeHandler.class)
    private String payPwd;
    //证书url
    private String certificate;
    //支付密码输入错误三次则锁定银行卡
    private Integer times;
    //锁定时间
    private LocalDateTime lockedTime;
    //角色
    private String role;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;
    //创建时间
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

}
