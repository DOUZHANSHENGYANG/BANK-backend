package xyz.douzhan.bank.security.filter;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.douzhan.bank.po.Safe;
import xyz.douzhan.bank.security.handler.MyAuthenticationException;
import xyz.douzhan.bank.security.user.JWTUserRedis;
import xyz.douzhan.bank.utils.JWTUtils;
import xyz.douzhan.bank.utils.RedisUtils;
import xyz.douzhan.bank.utils.SecurityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 一些声明信息
 * Description: jwt拦截校验器
 * date: 2023/11/25 16:58
 *
 * @author 斗战圣洋
 * @since JDK 17
 */


public class JWTAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 若用户未通过身份验证，但有用户凭证（如用户名），就根据凭证从数据湖库获取用户信息
     * 检验是否有效，若用户和令牌有效有效，则创建一个UsernamePasswordAuthenticationToken对象，传递UserDetails & 凭证 & 权限信息
     * 扩展上面生成的authToken，包含我们请求的详细信息，然后更新安全上下文中的身份验证令牌
     * 这样就可以在后面的登录认证中无需检验就放行了，实现jwt认证
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.从请求头尝试获取token
        String authHeader = request.getHeader(JWTUtils.getJwtProperties().getTokenName());
        //2.没有token,则可能是去登录，放行
        if (authHeader==null||!authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        //3.有就获取token
        String token = authHeader.substring(7);
        //4.从缓存尝试获取token
        JWTUserRedis userRedis = (JWTUserRedis) RedisUtils.get("user:jwt:" + token);

        Long safeId=null;
        if (userRedis==null){
            //4.1 获取不到，尝试解析token
            try{
                JWTUtils.verifyToken(token);
            }catch (Exception e){
                throw new MyAuthenticationException(e.getMessage());
            }
            safeId  = (Long) JWTUtils.parseToken(token);
        }else {

            safeId=userRedis.safeId();
        }
        //4.2 根据id查询用户信息
        Safe safe = Db.lambdaQuery(Safe.class).eq(Safe::getUsername, safeId).one();

        //4.3 获取权限
        List<SimpleGrantedAuthority> authorities = SecurityUtils.getAuthorities(safe.getRole());
        //4.4 构造token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                safe.getUsername(), null, authorities);
        //4.5 更新安全上下文的持有用户,通过验证
        authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
