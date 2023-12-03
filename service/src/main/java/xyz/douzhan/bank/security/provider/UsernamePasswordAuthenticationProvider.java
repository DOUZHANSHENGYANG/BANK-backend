package xyz.douzhan.bank.security.provider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import xyz.douzhan.bank.security.handler.MyPasswordEncoder;
import xyz.douzhan.bank.security.token.JwtAuthenticationToken;
import xyz.douzhan.bank.security.user.UserDetailsServiceImpl;

/**
 * 一些声明信息
 * Description: 账号密码登录provider
 * date: 2023/11/28 19:28
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

public class UsernamePasswordAuthenticationProvider extends DaoAuthenticationProvider {
    public UsernamePasswordAuthenticationProvider(UserDetailsServiceImpl userDetailsService, MyPasswordEncoder passwordEncoder){
        setHideUserNotFoundExceptions(false);
        setUserDetailsService(userDetailsService);
        //使用自定义加密器
        setPasswordEncoder( passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //如果是JwtAuthenticatioToken该类型，则在该处理器做登录校验
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
