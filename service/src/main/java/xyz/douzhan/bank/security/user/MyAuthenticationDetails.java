package xyz.douzhan.bank.security.user;

import cn.hutool.core.collection.CollUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 一些声明信息
 * Description: 自定义认证附加信息
 * date: 2023/12/4 13:39
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class MyAuthenticationDetails {

    private Map<String, String> headers;

    public MyAuthenticationDetails(HttpServletRequest request) {
        this.headers = new HashMap<>();
        //在构造函数中获取并存储请求头信息
        Enumeration<String> authorization = request.getHeaders("Authorization");
        if (!CollUtil.isEmpty(authorization)) {
            headers.put("Authorization", authorization.nextElement());
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

}
