package xyz.douzhan.bank.constants;

import java.util.List;

/**
 * 一些声明信息
 * Description: 认证常量 特意从业务常量中分开
 * date: 2023/12/8 20:07
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class AuthConstant {
    //需要放行的白名单
    public static final List<String> WHITE_LIST =
            List.of("/doc.html", "/swagger/**", "/swagger-ui.html", "/swagger-resources/**",
                    "/webjars/**", "/v2/**", "/v2/api-docs-ext/**", "/v3/api-docs/**",
                    "/bank/mobile/account/login/up","/bank/mobile/account/login/sms", "/bank/mobile/account/register", "/bank/common/auth/**",
                    "/bank/bank/bankcard/first");

    public static final String IMG_FRONT = "front";
    public static final String IMG_BACK = "back";
}
