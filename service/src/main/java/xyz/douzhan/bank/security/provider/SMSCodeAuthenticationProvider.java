package xyz.douzhan.bank.security.provider;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import xyz.douzhan.bank.po.Safe;
import xyz.douzhan.bank.security.token.SmsCodeAuthenticationToken;
import xyz.douzhan.bank.security.user.CustomUserDetails;
import xyz.douzhan.bank.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 一些声明信息
 * Description:手机号登录provider
 * date: 2023/11/28 20:23
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class SMSCodeAuthenticationProvider implements AuthenticationProvider {
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String mobile = (String) authentication.getPrincipal();

        //根据手机号加载用户
        UserDetails user = loadUserByMobile(mobile);

        return createSuccessAuthentication(user, authentication, user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //如果是SmsCodeAuthenticationToken该类型，则在该处理器做登录校验
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 跟账号登录保持一致
     *
     * @param principal
     * @param authentication
     * @param user
     * @return
     */
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                user,
                authentication.getCredentials(),
                authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());

        return result;
    }

    /**
     * 获取用户信息
     *
     * @param mobile
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        Safe safe = Db.lambdaQuery(Safe.class).eq(Safe::getTel, mobile).one();
        if (safe == null) {
            throw new UsernameNotFoundException("该手机号不存在");
        }

        return new CustomUserDetails(safe, SecurityUtils.getAuthorities(safe.getRole()));
    }
}
