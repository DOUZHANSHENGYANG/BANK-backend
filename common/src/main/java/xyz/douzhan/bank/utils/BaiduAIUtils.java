package xyz.douzhan.bank.utils;

import cn.hutool.core.util.StrUtil;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.properties.AliYunProperties;
import xyz.douzhan.bank.properties.BaiDuProperties;

import java.util.Base64;
import java.util.HashMap;

/**
 * 一些声明信息
 * Description: 百度第三方接口
 * date: 2023/12/7 20:01
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class BaiduAIUtils {
    private static BaiDuProperties baiDuProperties;

    public BaiduAIUtils(BaiDuProperties baiDuProperties) throws Exception {
        BaiduAIUtils.baiDuProperties = baiDuProperties;
    }

    public Boolean ocrIDCard(String base64Img, String idCardSide) {
        AipOcr client = createClient();
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");
        try{
            JSONObject res = client.idcard(base64Img, idCardSide, options);
            String imageStatus = res.getString("image_status");
            if (imageStatus!=null&&!StrUtil.equals(imageStatus,"normal")){
                throw new BizException("身份证识别异常");
            }
            JSONArray wordsResult = res.getJSONArray("words_result");
            int wordsResultNum = res.getInt("words_result_num");
            for (int i = 0; i < wordsResultNum; i++) {
                wordsResult.getJSONObject(i).getString("words")
            }
        }catch (Exception e){

        }


    }

    /**
     * 创建客户短
     * @return
     */
    private static AipOcr createClient() {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(baiDuProperties.getAppID(), baiDuProperties.getAPIKey(), baiDuProperties.getSecretKey());

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
