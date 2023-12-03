package xyz.douzhan.bank.security.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.utils.HttpUtils;
import xyz.douzhan.bank.utils.JWTUtils;
import xyz.douzhan.bank.utils.RedisUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 一些声明信息
 * Description: 登录退出处理器
 * date: 2023/11/29 21:09
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //获取token
        String token = request.getHeader(JWTUtils.getJwtProperties().getTokenName());
        //清空缓存
        if (StrUtil.isNotEmpty(token)){
            RedisUtils.del("user:jwt:"+token);
        }
        //返回结果
        Result result = Result.success(token).message("登出成功");
        HttpUtils.sendMessage(response,result);
    }
}
