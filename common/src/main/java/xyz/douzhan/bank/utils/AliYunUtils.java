package xyz.douzhan.bank.utils;

import ch.qos.logback.core.joran.event.BodyEvent;
import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import com.aliyun.credentials.provider.EnvironmentVariableCredentialsProvider;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.properties.AliYunProperties;
import com.aliyun.tea.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * 一些声明信息
 * Description:短信工具类
 * date: 2023/12/7 18:05
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Slf4j
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

    public static String upload( MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "examplebucket";
//        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
//        String objectName = "exampledir/exampleobject.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(aliYunProperties.getOssEndPoint(), aliYunProperties.getAccessKeyID(), aliYunProperties.getAccessKeySecret());

        try {
//            // 填写字符串。
//            String content = "Hello OSS，你好世界";
            //eg: UUID + . + png
             String fileName = UUID.randomUUID() + "."
                    + Objects.requireNonNull(file.getContentType()).substring(
                    file.getContentType().lastIndexOf("/") + 1);    // 创建文件名称

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliYunProperties.getOssBucketName(), fileName, file.getInputStream());

            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传字符串。
            PutObjectResult result = ossClient.putObject(putObjectRequest);

            return "https://"+aliYunProperties.getOssBucketName()+"."+aliYunProperties.getOssEndPoint()+"/"+fileName;
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
            throw new ThirdPartyAPIException("阿里云对象存储异常:"+oe.getMessage());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
            throw new ThirdPartyAPIException("阿里云对象存储异常:"+ce.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BizException("前端文件上传异常:"+e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

    }
}
