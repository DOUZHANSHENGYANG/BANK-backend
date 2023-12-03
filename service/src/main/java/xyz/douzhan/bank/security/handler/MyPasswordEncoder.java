package xyz.douzhan.bank.security.handler;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 一些声明信息
 * Description: 自定义passwordEncoder 不进行任何处理
 * date: 2023/12/2 19:34
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return StrUtil.equals(rawPassword,encodedPassword);
    }
}
