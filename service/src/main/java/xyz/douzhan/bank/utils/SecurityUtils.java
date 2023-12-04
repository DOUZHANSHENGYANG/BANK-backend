package xyz.douzhan.bank.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 一些声明信息
 * Description: 安全工具类
 * date: 2023/12/3 21:29
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class SecurityUtils {
    private static final String ROLE_PREFIX="ROLE_";
    public static List<SimpleGrantedAuthority> getAuthorities(String role){
        List <SimpleGrantedAuthority> authorities= new ArrayList<SimpleGrantedAuthority>();
        if (role.contains(".")){
            authorities = Arrays.stream(role.split("\\."))
                    .map(r->new SimpleGrantedAuthority(ROLE_PREFIX+r))
                    .toList();
        }else {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
