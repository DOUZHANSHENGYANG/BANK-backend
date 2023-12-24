package xyz.douzhan.bank.filter;

import cn.hutool.core.convert.NumberWithFormat;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.DataBinder;
import xyz.douzhan.bank.constants.AuthConstant;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.dto.LoginInfoRedis;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.utils.HttpUtil;
import xyz.douzhan.bank.utils.JWTUtil;
import xyz.douzhan.bank.utils.RedisUtil;

import java.io.IOException;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/8 19:13
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class JWTAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        UserContext.setContext(1L);
//        filterChain.doFilter(servletRequest, servletResponse);
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        //是否匹配放行白名单
        Boolean isMatch = HttpUtil.match(AuthConstant.WHITE_LIST, request);
        if (isMatch){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }


        //1.从请求头尝试获取token
        String authHeader = request.getHeader(JWTUtil.getJwtProperties().getTokenName());
        //2.没有token,则可能是去登录，放行
        if (authHeader==null||!authHeader.startsWith("Bearer ")){
            throw new AuthenticationException("未进行登录,请登录");
        }
        //3.有就获取token
        String token = authHeader.substring(7);
        //4.从缓存尝试获取token
        LoginInfoRedis userRedis = (LoginInfoRedis) RedisUtil.get("user:jwt:" + token);

        Long phoneAccountId=null;
        try {
            if (userRedis==null){
                NumberWithFormat numberWithFormat = (NumberWithFormat) JWTUtil.parseToken(token);
                phoneAccountId=numberWithFormat.longValue();

            }else {
                phoneAccountId=userRedis.phoneAccountId();
            }
        }catch (Exception e){
            throw new AuthenticationException("未登录或token校验失败:"+e.getMessage());
        }
        // 如果有判断是否存在账户 防止非法访问
        PhoneAccount phoneAccount = Db.lambdaQuery(PhoneAccount.class).eq(PhoneAccount::getId, phoneAccountId).select(PhoneAccount::getUserInfoId).one();
        if (phoneAccount==null){
            throw new AuthenticationException("账户不存在，非法访问");
        }

        UserContext.setContext(phoneAccountId);

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
