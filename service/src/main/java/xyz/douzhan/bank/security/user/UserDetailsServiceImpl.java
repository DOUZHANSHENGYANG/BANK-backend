package xyz.douzhan.bank.security.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.utils.SecurityUtils;

/**
 * 一些声明信息
 * Description:一般而言，只需定制userDetailsService即可，满足不了再定制AuthenticationProvider,那就直接写在配置类里
 * date: 2023/11/28 19:41
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询安全信息
        PhoneAccount phoneAccount= Db.lambdaQuery(PhoneAccount.class).eq(StrUtil.isNotEmpty(username),PhoneAccount::getUsername,username).one();
        if (phoneAccount==null){
            throw new UsernameNotFoundException("该用户不存在");
        }
        return new CustomUserDetails(phoneAccount, SecurityUtils.getAuthorities(phoneAccount.getRole()));

    }
}
