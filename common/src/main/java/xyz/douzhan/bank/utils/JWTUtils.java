package xyz.douzhan.bank.utils;


import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.properties.JWTProperties;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

/**
 * 一些声明信息
 * Description:jwt工具类
 * date: 2023/12/2 14:38
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class JWTUtils {
    private static JWTProperties jwtProperties;
    private static JWTSigner jwtSigner;

    public JWTUtils(JWTProperties jwtProperties) {
        JWTUtils.jwtProperties = jwtProperties;
        JWTUtils.jwtSigner = CypherUtils.getJwtSigner();
    }


    /**
     * 生成token
     * @param userInfo
     * @return
     */
    public static String genToken(String userInfo) {
        //jwt过期时间
        long expireTime = LocalDateTime.now().plusDays(jwtProperties.getTtl()).toEpochSecond(ZoneOffset.UTC);
        HashMap<String, Object> payloads = new HashMap<>();
        payloads.put(jwtProperties.getUserInfo(), userInfo);
        payloads.put(JWT.EXPIRES_AT, expireTime);
        return JWTUtil.createToken(payloads, jwtSigner);

    }

    /**
     * 校验token，失败抛出异常
     *
     * @param token
     */
    public static void verifyToken(String token){
        if(JWTUtil.verify(token, jwtSigner)){
            throw new RuntimeException("jwt格式不合法");
        }
        JWT jwt = JWTUtil.parseToken(token);
        NumberWithFormat exp = (NumberWithFormat) jwt.getPayload().getClaim(JWT.EXPIRES_AT);

        if (LocalDateTime.ofEpochSecond(exp.longValue(),0,ZoneOffset.UTC).isBefore(LocalDateTime.now())){
            throw new RuntimeException("jwt过期");
        }
    }

    /**
     * 解析token
     * @return
     */
    public static Object parseToken(String token){
        verifyToken(token);
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayload().getClaim(jwtProperties.getUserInfo());
    }

}
