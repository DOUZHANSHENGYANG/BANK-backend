package xyz.douzhan.bank.utils;

import lombok.Getter;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/8 14:51
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class HttpClientUtils {
    @Getter
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();



}
