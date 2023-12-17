package xyz.douzhan.bank.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.dto.OCRDTO;
import xyz.douzhan.bank.dto.ValidateVerifyCodeDTO;
import xyz.douzhan.bank.vo.ImgVerifyCodeVO;

import java.util.Map;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/11 11:35
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface AuthService {
    /**
     * 根据类型获取验证码
     * @param type
     * @param exp
     * @param phoneNumber
     * @return
     */
    ImgVerifyCodeVO getVerifyCode(Integer type, Integer exp, String phoneNumber) ;

    /**
     * 验证验证码合法性
     * @param verifyCodeDTO
     */
    void validateVerifyCode(ValidateVerifyCodeDTO verifyCodeDTO) ;

    /**
     * 图像识别
     *
     * @param ocrdto
     * @param file
     * @return
     */
    Map<String, Object> ocr(OCRDTO ocrdto, MultipartFile file);

    /**
     * 上传证件
     *
     * @param frontFile
     * @param backFile
     * @param phoneNumber
     * @return
     */
    JSONObject uploadDocuments(MultipartFile frontFile, MultipartFile backFile, Long phoneNumber);

    /**
     * 注册前的人脸认证
     *
     * @param frontFile
     * @param liveFile
     * @return
     */
    boolean faceAuthTemp(MultipartFile frontFile, MultipartFile liveFile);

    /**
     * 注册后的人脸认证
     * @param liveFile
     * @return
     */
    Boolean faceAuth(MultipartFile liveFile);
}
