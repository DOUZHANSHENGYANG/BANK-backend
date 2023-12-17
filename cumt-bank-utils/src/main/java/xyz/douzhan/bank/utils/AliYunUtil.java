package xyz.douzhan.bank.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.constants.ThirdAPIConfigConstant;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;

import java.io.IOException;
import java.util.Map;
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

@Slf4j
public class AliYunUtil {
    private static String ACCESS_KEY_ID;
    private static String ACCESS_KEY_SECRET;
    private static String OSS_BUCKET_NAME;
    private static String OSS_END_POINT;

    private static String SignName = "阿里云短信测试";
    private static String TemplateCode = "SMS_154950909";
    private static String OK_CODE = "OK";
    private static String AVATAR = "avatar";

    public AliYunUtil() {
        Map<String, String> config = FileUtil.getConfig(ThirdAPIConfigConstant.ALIYUN);
        ACCESS_KEY_ID = config.get(ThirdAPIConfigConstant.ALIYUN_ACCESS_KEY_ID);
        ACCESS_KEY_SECRET = config.get(ThirdAPIConfigConstant.ALIYUN_ACCESS_KEY_SECRET);
        OSS_BUCKET_NAME = config.get(ThirdAPIConfigConstant.ALIYUN_OSS_BUCKET_NAME);
        OSS_END_POINT = config.get(ThirdAPIConfigConstant.ALIYUN_OSS_END_POINT);
    }

    /**
     * aliyun oss 上传文件（图片）
     *
     * @param file
     * @return
     */
    public static String upload(MultipartFile file, Long userId) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "examplebucket";
//        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
//        String objectName = "exampledir/exampleobject.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(OSS_END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        try {
            //eg: userId + avatar + UUID + . + png
            String fileName = userId + AVATAR + UUID.randomUUID() + FileUtil.getFileTypeSuffixWithDot(file.getOriginalFilename());    // 创建文件名称
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(OSS_BUCKET_NAME, fileName, file.getInputStream());
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);
            //上传
            PutObjectResult result = ossClient.putObject(putObjectRequest);

            return "https://" + OSS_BUCKET_NAME + "." + OSS_END_POINT + "/" + fileName;
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
            throw new ThirdPartyAPIException("阿里云对象存储异常:" + oe.getMessage());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
            throw new ThirdPartyAPIException("阿里云对象存储异常:" + ce.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BizException("前端文件上传异常:" + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

    }


    /**
     * 发送短信第二步 模版和签名暂时没弄
     *
     * @param code
     * @param phoneNumber
     * @throws Exception
     */
    public static void sendSM(String code, String phoneNumber) {
        SendSmsResponseBody body = null;
        try {
            // 1.创建请求
            com.aliyun.dysmsapi20170525.Client client = AliYunUtil.createClient(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                    .setSignName(SignName)
                    .setTemplateCode(TemplateCode)
                    .setPhoneNumbers(phoneNumber)
                    .setTemplateParam("{\"code\":\"" + code + "\"}");
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            // 2.发送短信
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            body = response.getBody();
        } catch (Exception error) { //3. 错误处理
            // 错误 message
            // System.out.println(error.getMessage());
            // 诊断地址
            // System.out.println(error.getData().get("Recommend"));
            // com.aliyun.teautil.Common.assertAsString(error.message);
            throw new ThirdPartyAPIException(error.getMessage());
        }
        if (body == null || !body.getCode().equals(OK_CODE)) {
            throw new BizException(BizExceptionConstant.SEND_SHORT_MESSAGE_FAILED);
        }
    }

    /**
     * 发送短信第一步 使用AK&SK创建账号客户端
     *
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
