package xyz.douzhan.bank.service;

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
}
