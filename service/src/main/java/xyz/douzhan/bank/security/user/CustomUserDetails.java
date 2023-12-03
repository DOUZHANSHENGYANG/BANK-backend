package xyz.douzhan.bank.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import xyz.douzhan.bank.po.Safe;

import java.util.Collection;

/**
 * 一些声明信息
 * Description: 自定义userDetails
 * date: 2023/12/2 19:58
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class CustomUserDetails extends User {
    private Safe safe;
    public CustomUserDetails(Safe safe, Collection<? extends GrantedAuthority> authorities) {
        super(safe.getUsername(), safe.getAccountPwd(), authorities);
        this.safe=safe;
    }
    public Long getSafeId() {
        return safe.getId();
    }

    public void setSafe(Safe sysUser) {
        this.safe = safe;
    }
}
