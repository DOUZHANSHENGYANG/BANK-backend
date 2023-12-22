package xyz.douzhan.bank.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import xyz.douzhan.bank.constants.BizConstant;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 一些声明信息
 * Description: 百度第三方接口
 * date: 2023/12/7 20:01
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Slf4j
public class BaiduAIUtil {


    private static final String APP_ID = "45520727";
    private static final String API_KEY = "OfCtjLuzy6NlEYfmMPIqG7Vb";
    private static final String SECRET_KEY = "pmGmsHWYl1Pqn93vWHIWFKdpk2Ivy359";
    private static final String ACCESS_TOKEN = "24.a07d43621337d98b6719c5b3cb27bd63.2592000.1705833393.282335-45520727";


    /**
     * 人脸识别 将身份证和实时抓拍的人脸做对比 80以上就为合格
     *
     * @param base64Live
     * @param base64IDCard
     * @return
     */
    public static Boolean faceAuth(String base64Live, String base64IDCard) {
        try {
//            String json = String.format("[{\"image\":\"%s\",\"image_type\":\"BASE64\",\"face_type\":\"LIVE\"},{\"image\":\"%s\",\"image_type\":\"BASE64\",\"face_type\":\"IDCARD\"}]", base64Live, base64IDCard);
            // 1.创建请求
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "[{\"image\":\"/9j/4QCORXhpZgAATU0AKgAAAAgABQEAAAMAAAABAswAAAEBAAMAAAABBhAAAIdpAAQAAAABAAAASgESAAMAAAABAAAAAAEyAAIA...\",\"image_type\":\"BASE64\",\"face_type\":\"LIVE\",\"liveness_control\":\"NORMAL\",\"spoofing_control\":\"NORMAL\"},{\"image\":\"/9j/4QCqRXhpZgAATU0AKgAAAAgABQEAAAQAAAABAAAC0AEBAAQAAAABAAADwIdpAAQAAAABAAAAXgESAAMAAAABAAEAAAEyAAIA...\",\"image_type\":\"BASE64\",\"face_type\":\"IDCARD\",\"spoofing_control\":\"NORMAL\"}]");
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/face/v3/match?access_token=24.a07d43621337d98b6719c5b3cb27bd63.2592000.1705833393.282335-45520727")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            // 2.发送请求
            Response response = HttpClientUtil.getHTTP_CLIENT().newCall(request).execute();
            System.out.println(response.body().string());

            // 3.处理结果
            JSONObject result = new JSONObject(response.body().string());
            if (!StrUtil.equals(result.getString("error_msg"), "success", true)) {
                return false;
            }
            if (result.getJSONObject("result").getBigDecimal("score").compareTo(new BigDecimal("70.00")) == -1) {
                return false;
            }
        } catch (IOException e) {
            throw new ThirdPartyAPIException("百度人脸识别异常:" + e.getMessage());
        }
        return true;
    }


    /**
     * 调用百度ocr接口实现身份证，银行卡识别
     *
     * @param base64Img
     * @param idCardSide
     * @return
     */
    public static String ocr(String base64Img, String idCardSide) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        // image 可以通过 getFileContentAsBase64("C:\fakepath\frontFile.jpg") 方法获取,如果Content-Type是application/x-www-form-urlencoded时,第二个参数传true
        RequestBody body = RequestBody.create(mediaType, "id_card_side=front&image=" + base64Img + "&detect_risk=true&detect_quality=true&detect_photo=false&detect_card=false&detect_direction=true");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?access_token=24.a07d43621337d98b6719c5b3cb27bd63.2592000.1705833393.282335-45520727")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HttpClientUtil.getHTTP_CLIENT().newCall(request).execute();
        return parse(response.body().string());
    }

    private static String parse(String string) {
        Gson gson = new Gson();
        HashMap<String, Object> result = new HashMap<>();
        JsonObject jsonObject = gson.fromJson(string, JsonObject.class);
        JsonObject wordsResult = jsonObject.getAsJsonObject("words_result");
        if (wordsResult == null) {
            throw new ThirdPartyAPIException("证件识别错误");
        }
        return wordsResult.toString();
    }


}

