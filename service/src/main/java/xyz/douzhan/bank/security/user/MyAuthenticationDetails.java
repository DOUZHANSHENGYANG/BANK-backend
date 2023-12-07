package xyz.douzhan.bank.security.user;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import xyz.douzhan.bank.utils.HttpUtils;

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

    private Map<String, String> info;

    public MyAuthenticationDetails(HttpServletRequest request) {
        this.info = new HashMap<>();
        //在构造函数中获取并存储请求头信息
        Enumeration<String> authorization = request.getHeaders("Authorization");
        if (!CollUtil.isEmpty(authorization)) {
            info.put("Authorization", authorization.nextElement());
        }
//        //在构造函数中尝试获取并存储请求体部分信息
//        String body = HttpUtils.getBody(request);
//        JSONObject jsonObject = JSON.parseObject(body);
//        if (jsonObject != null) {
//            info.put("type",jsonObject.getString("type"));
//        }
    }

    public Map<String, String> getInfo() {
        return info;
    }

}
