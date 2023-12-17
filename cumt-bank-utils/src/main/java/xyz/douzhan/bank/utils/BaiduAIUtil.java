package xyz.douzhan.bank.utils;

import cn.hutool.core.util.StrUtil;
import com.baidu.aip.ocr.AipOcr;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.constants.BizConstant;
import xyz.douzhan.bank.constants.ThirdAPIConfigConstant;
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


    private static  String APP_ID;
    private static  String API_KEY;
    private static  String SECRET_KEY;


    public BaiduAIUtil() {
        Map<String, String> config = FileUtil.getConfig(ThirdAPIConfigConstant.BAIDU);
        APP_ID=config.get(ThirdAPIConfigConstant.BAIDU_APP_ID);
        API_KEY=config.get(ThirdAPIConfigConstant.BAIDU_API_KEY);
        SECRET_KEY=config.get(ThirdAPIConfigConstant.BAIDU_SECRET_KEY);
    }

    /**
     * 人脸识别 将身份证和实时抓拍的人脸做对比 80以上就为合格
     *
     * @param base64Live
     * @param base64IDCard
     * @return
     */
    public static Boolean faceAuth(String base64Live, String base64IDCard) {
        try {
            // 1.创建请求
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(
                    mediaType,
                    "[{\"image\":\"" + base64Live + "\",\"image_type\":\"BASE64\",\"face_type\":\"LIVE\",\"quality_control\":\"NORMAL\",\"liveness_control\":\"NONE\",\"spoofing_control\":\"NORMAL\"},{\"image\":\"" + base64IDCard + "\",\"image_type\":\"BASE64\",\"face_type\":\"LIVE\",\"quality_control\":\"NORMAL\",\"liveness_control\":\"NONE\",\"spoofing_control\":\"NORMAL\"}]");
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rest/2.0/face/v3/match?access_token=" + getAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            // 2.发送请求
            Response response = HttpClientUtil.getHTTP_CLIENT().newCall(request).execute();
            // 3.处理结果
            JSONObject result = new JSONObject(response.body().string());
            if (!StrUtil.equals(result.getString("error_msg"), "success", false)) {
                return false;
            }
            if (result.getJSONObject("result").getBigDecimal("score").compareTo(new BigDecimal("80.00")) == -1) {
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
    public static Map<String, Object> ocr(Integer type, String base64Img, String idCardSide) throws JsonProcessingException {
        AipOcr client = createOcrClient();
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");

        JSONObject res = null;
        Map<String, Object> resultMap = null;
        if (type == BizConstant.ID_CARD) {//身份证识别
            res = client.idcard(base64Img, idCardSide, options);
            resultMap = parseJsonString(res.toString(), "words_result", "words");
        } else if (type == BizConstant.BANKCARD) {//银行卡识别
            res = client.bankcard(base64Img, options);
            JSONObject result = res.getJSONObject("result");
            resultMap = result.toMap();
        }
        return resultMap;
    }

    /**
     * 解析jsonObject
     *
     * @param jsonString
     * @param position
     * @param name
     * @return
     * @throws JsonProcessingException
     */
    private static Map<String, Object> parseJsonString(String jsonString, String position, String name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = new HashMap<>();
        JsonNode jsonNode = mapper.readTree(jsonString);
        JsonNode node = jsonNode.get(position);
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            String words = field.getValue().get(name).asText();
            resultMap.put(key, words);
        }
        return resultMap;
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" +APP_ID
                + "&client_secret=" + APP_ID);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HttpClientUtil.getHTTP_CLIENT().newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

    /**
     * 创建客户短
     *
     * @return
     */
    private static AipOcr createOcrClient() {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID,API_KEY,SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
//
//        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        return client;
//        // 调用接口
//        String path = "test.jpg";
//        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
//        System.out.println(res.toString(2));
    }
}
