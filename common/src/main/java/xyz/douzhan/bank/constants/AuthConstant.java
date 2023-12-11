package xyz.douzhan.bank.constants;

import java.util.List;

/**
 * 一些声明信息
 * Description: 认证常量 特意从业务常量中分开
 * date: 2023/12/8 20:07
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface AuthConstant {
    //需要放行的白名单
    List<String> WHITE_LIST =
            List.of("/doc.html","/swagger/**","/swagger-ui.html","/swagger-resources/**",
                    "/webjars/**", "/v2/**","/v2/api-docs-ext/**","/v3/api-docs/**",
                    "/bank/mobile/account/login/*","/bank/mobile/account/register/**","/auth/**",
                    "/test/**");

    String AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED="认证参数不支持";
    String AUTHENTICATION_PARAMETER_IS_INVALID="认证参数不合法";

    String SMS_REDIS_PREFIX="user:smscode:";
    String IMG_REDIS_PREFIX="user:imgcode:";
    String USR_JWT_PREFIX="user:jwt:";
    String EXPIRED_VERIFICATION_RETURN_MESSAGE="验证码已过期，请重新获取";
    String VERIFY_CODE_ERROR_RETURN_MESSAGE="验证码错误";
    String IMG_FRONT="front";
    String IMG_BACK="back";
    String INVALID_PAY_PWD="支付错误";
    String OCR_ERROR="ocr识别异常";
}
