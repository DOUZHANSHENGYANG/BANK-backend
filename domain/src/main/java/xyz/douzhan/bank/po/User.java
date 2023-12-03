package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;
import xyz.douzhan.bank.enums.CardType;
import xyz.douzhan.bank.enums.Gender;
import xyz.douzhan.bank.enums.UserStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:
 * date: 2023/11/24 19:41
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Builder
@TableName("user")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //名字
    private String name;
    //账户状态 0正常1冻结2注销
    private UserStatus status;
    //证件类型 0身份证1护照2外国人永久居留身份证3港澳居民居住证4台湾居民居住证
    private CardType cardType;
    //证件号
    private String cardNum;
    //性别 0男1女
    private Gender gender;
    //头像url
    private String avatar;
    //民族
    private String ethnicity;
    //生日
    private LocalDate birthday;
    //出生地
    private String birthplace;
    //国家
    private String country;
    //地区
    private String region;
    //地址
    private String address;
    //职业
    private String profession;
    //组织单位
    private String organization;
    //更新时间
    private LocalDateTime updateTime;
    //创建时间
    private LocalDateTime createTime;

}
