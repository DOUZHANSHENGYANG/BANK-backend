package xyz.douzhan.bank.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.constants.BizConstant;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.properties.MinIOProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * date: 2023/12/17 15:56
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
@Slf4j

public class MinIOUtil {
    private static final String DOCUMENTS = BizConstant.DOCUMENTS;
    private static MinIOProperties minIOProperties;
    private static MinioClient minioClient;

    public MinIOUtil(MinIOProperties minIOProperties, MinioClient minioClient){
        MinIOUtil.minIOProperties=minIOProperties;
        MinIOUtil.minioClient=minioClient;
    }

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public static boolean bucketExists(String bucketName) {
        boolean found = false;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public static Boolean createBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public static Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public static List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 文件上传(用户证件)
     *
     * @param file
     * @param firstAccountId
     * @param side
     * @returns
     */
    public static String uploadDocuments(MultipartFile file, Long firstAccountId, String side) {
        String objectName=null;
        try {
            String originalFilename = file.getOriginalFilename();
            //eg: firstAccountId + documents + side + UUID + . + png
            objectName = firstAccountId+DOCUMENTS+side+UUID.randomUUID() + FileUtil.getFileTypeSuffixWithDot(originalFilename);

            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(minIOProperties.getDocumentsBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
        return objectName;
    }

    /**
     * 文件下载
     * @param fileName 文件名称
     * @return Boolean
     */
    public static byte[] download(String fileName) {
        // 1.创建请求
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(minIOProperties.getDocumentsBucketName())
                .object(fileName)
                .build();
        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()){
            // 2.发送请求
            GetObjectResponse response = minioClient.getObject(objectArgs);
            os.write(response.readAllBytes());
            os.flush();
            return os.toByteArray();
//                res.setCharacterEncoding("utf-8");
//                // 设置强制下载不打开
//                // res.setContentType("application/force-download");
//                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
//                try (ServletOutputStream stream = res.getOutputStream()){
//                    stream.write(bytes);
//                    stream.flush();
        } catch (Exception e){
            throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
        }
    }

    /**
     * 查看文件对象
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(minIOProperties.getDocumentsBucketName())
                        .build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
        return items;
    }

    /**
     * 预览图片(用户证件)
     * @param fileName
     * @return
     */
    public String previewDocuments(String fileName){
        try {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder()
                .bucket(minIOProperties.getDocumentsBucketName())
                .object(fileName)
                .method(Method.GET)
                .build();
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }
    /**
     * 删除
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName){
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minIOProperties.getDocumentsBucketName())
                            .object(fileName)
                            .build());
        }catch (Exception e){
            throw new BizException(e.getMessage());
        }
        return true;
    }
}
