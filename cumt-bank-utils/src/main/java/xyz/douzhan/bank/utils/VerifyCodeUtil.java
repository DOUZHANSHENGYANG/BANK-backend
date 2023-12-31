package xyz.douzhan.bank.utils;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.enums.VerifyCodeType;
import xyz.douzhan.bank.properties.VerifyCodeProperties;


/**
 * 一些声明信息
 * Description:验证码工具类
 * date: 2023/11/25 22:22
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class VerifyCodeUtil {


    private static VerifyCodeProperties verifyCodeProperties;

    public VerifyCodeUtil(VerifyCodeProperties verifyCodeProperties){
        VerifyCodeUtil.verifyCodeProperties=verifyCodeProperties;
    }

    /**
     * 根据类型获取验证码
     * @param type
     * @return
     */
    public static AbstractCaptcha genVerifyCode(VerifyCodeType type){
        return switch (type){
            case PIC, SMS:
                yield null;
            case LINE: yield CaptchaUtil.createLineCaptcha(200, 100);
            case CIRCLE: yield CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
            case SHEAR: yield CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
            case PURENUM: {
                // 自定义纯数字的验证码（随机4位数字，可重复）
                RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
                LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
                lineCaptcha.setGenerator(randomGenerator);
                yield lineCaptcha;
            }
        };
    }
}
