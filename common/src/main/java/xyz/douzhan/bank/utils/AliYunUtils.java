package xyz.douzhan.bank.utils;

import ch.qos.logback.core.joran.event.BodyEvent;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.properties.AliYunProperties;
import com.aliyun.tea.*;
/**
 * 一些声明信息
 * Description:短信工具类
 * date: 2023/12/7 18:05
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class AliYunUtils {
    private static AliYunProperties aliYunProperties;
    public AliYunUtils(AliYunProperties aliYunProperties) throws Exception {
        AliYunUtils.aliYunProperties=aliYunProperties;
    }




    /**
     * 发送短信验证码 模版和签名暂时没弄
     * @param code
     * @return
     * @throws Exception
     */
    public static Boolean sendSM(String code) throws Exception {

        com.aliyun.dysmsapi20170525.Client client = AliYunUtils.createClient(aliYunProperties.getAccessKeyID(), aliYunProperties.getAccessKeySecret());
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers("16680284176")
                .setTemplateParam("{\"code\":\""+code+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        SendSmsResponseBody body=null;
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
             body= response.getBody();
        } catch (TeaException error) {
            // 错误 message
//            System.out.println(error.getMessage());
            // 诊断地址
//            System.out.println(error.getData().get("Recommend"));
//            com.aliyun.teautil.Common.assertAsString(error.message);
            throw new ThirdPartyAPIException(error.getMessage());
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            throw new ThirdPartyAPIException(error.getMessage());
//            // 错误 message
//            System.out.println(error.getMessage());
//            // 诊断地址
//            System.out.println(error.getData().get("Recommend"));
//            com.aliyun.teautil.Common.assertAsString(error.message);
        }
        if (body!=null&&body.getCode().equals("OK")){
            return true;
        }
        return false;
    }
    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    private static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

}
