package xyz.douzhan.bank.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import xyz.douzhan.bank.result.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 一些声明信息
 * Description:http工具类
 * date: 2023/11/28 18:59
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class HttpUtils {

    /**
     * 返回前端结果
     * @param response
     * @param result
     * @throws IOException
     */
    public static void sendMessage(HttpServletResponse response,Result result) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        String jsonResult = JSON.toJSONString(result);
        PrintWriter writer = response.getWriter();
        writer.print(jsonResult);
        writer.flush();
        writer.close();
    }
    /**
     * 从请求中获取请求体
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getBody(HttpServletRequest request) {
        //判断是否为post，put方法
        if (!List.of("POST", "PUT").contains(request.getMethod())) {
            return null;
        }
        //获取输入流
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取ip地址
     * @param request
     * @return
     */
    public static String getRemoteHostIP(HttpServletRequest request){
        if(request == null) {         return "unknown";    }    String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }

}
