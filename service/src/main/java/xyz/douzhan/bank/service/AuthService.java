package xyz.douzhan.bank.service;

import xyz.douzhan.bank.dto.ValidateVerifyCodeDTO;
import xyz.douzhan.bank.vo.ImgVerifyCodeVO;

import javax.security.sasl.AuthenticationException;

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
     * 比较交易密码
     * @param id
     * @param payPwd
     */
    void comparePayPwd(Long id, String payPwd);
}
