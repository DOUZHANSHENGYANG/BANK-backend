package xyz.douzhan.bank.security.handler;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.utils.JWTUtils;
import xyz.douzhan.bank.utils.RedisUtils;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/4 14:02
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyLogoutHandler implements LogoutHandler {


    public MyLogoutHandler() {
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        获取token 这种方式获取不到 真无语了
//        String token = request.getHeader(JWTUtils.getJwtProperties().getTokenName());
            String token=request.getHeader(JWTUtils.getJwtProperties().getTokenName());
//            MyAuthenticationDetails details = (MyAuthenticationDetails) authentication.getDetails();
            //判断token是否为Null 清空缓存
            if (StrUtil.isNotEmpty(token)){
                RedisUtils.del("user:jwt:"+token);
            }
    }

}
