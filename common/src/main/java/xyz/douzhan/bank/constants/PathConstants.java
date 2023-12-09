package xyz.douzhan.bank.constants;

import java.util.List;

/**
 * 一些声明信息
 * Description: 路径常量
 * date: 2023/12/8 19:17
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface PathConstants {
    //需要放行的白名单
     List<String> WHITE_LIST =
            List.of("/doc.html","/swagger/**","/swagger-ui.html","/swagger-resources/**",
                    "/webjars/**", "/v2/**","/v2/api-docs-ext/**","/v3/api-docs/**",
                    "/bank/mobile/account/login/*","/bank/mobile/account/register/**","/common/**",
                    "/test/**");

}
