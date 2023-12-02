package xyz.douzhan.bank.po;//package xyz.douzhan.bank.po;
//
//import com.baomidou.mybatisplus.annotation.*;
//import lombok.Builder;
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import xyz.douzhan.bank.enums.Role;
//import xyz.douzhan.bank.handler.EncodeTypeHandler;
//
//import java.time.LocalDateTime;
//import java.util.Collection;
//import java.util.List;
//
///**
// * 一些声明信息
// * Description:用户安全类
// * date: 2023/11/24 20:43
// *
// * @author 斗战圣洋
// * @since JDK 17
// */
//@Data
//@Builder
//@TableName(value = "safe",autoResultMap = true)
//public class Safe implements UserDetails {
//    //主键
//    @TableId(type = IdType.AUTO)
//    private Long id;
//    //用户id
//    private Long userId;
//    //用户名
//    private String username;
//    //手机号
//    private String tel;
//    //账号密码
//    @TableField(typeHandler = EncodeTypeHandler.class)
//    private String accountPwd;
//    //支付密码
//    @TableField(typeHandler = EncodeTypeHandler.class)
//    private String payPwd;
//    //证书url
//    private String certificate;
//    //支付密码输入错误三次则锁定银行卡
//    private Integer times;
//    //锁定时间
//    private LocalDateTime lockedTime;
//    //角色
//    private Role role;
//    //更新时间
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updateTime;
//    //创建时间
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime createTime;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role.getDesc()));
//    }
//
//    @Override
//    public String getPassword() {
//        return accountPwd;
//    }
//
//    /**
//     * 账户没有过期
//     * @return
//     */
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    /**
//     * 账户没有锁定
//     * @return
//     */
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    /**
//     * 凭证没有过期
//     * @return
//     */
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    /**
//     * 用户是否启用
//     * @return
//     */
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
