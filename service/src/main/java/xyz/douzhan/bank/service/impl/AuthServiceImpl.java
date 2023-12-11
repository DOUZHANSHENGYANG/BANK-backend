package xyz.douzhan.bank.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.constants.AuthConstant;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.dto.OCRDTO;
import xyz.douzhan.bank.dto.ValidateVerifyCodeDTO;
import xyz.douzhan.bank.enums.VerifyCodeType;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.service.AsyncService;
import xyz.douzhan.bank.service.AuthService;
import xyz.douzhan.bank.utils.*;
import xyz.douzhan.bank.vo.ImgVerifyCodeVO;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 一些声明信息
 * Description:认证服务类
 * date: 2023/12/11 11:36
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AsyncService asyncService;
    @Override
    public ImgVerifyCodeVO getVerifyCode(Integer type, Integer exp, String phoneNumber) {
        //校验参数合法性
        if (type < 0 || type > 1) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        if (exp < 30) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //获取验证码
        AbstractCaptcha captcha = VerifyCodeUtils.genVerifyCode(VerifyCodeType.PURENUM);
        String code = captcha.getCode();

        //类型为短信验证码
        if (VerifyCodeType.SMS.ordinal() == type) {
            //校验手机号不能为空
            CommonBizUtils.assertArgsNotNull(phoneNumber);
            //验证码放入缓存
            RedisUtils.setWithExpire(AuthConstant.SMS_REDIS_PREFIX + phoneNumber, code, exp, TimeUnit.SECONDS);
            //发送短信
            try {
                asyncService.sendMessage(code,phoneNumber);
            } catch (Exception e) {
                throw new AuthenticationException(BizExceptionConstant.SEND_SHORT_MESSAGE_FAILED);
            }

            return null;
        }
        //类型为图形验证码
        //生成验证码序号
        String uuid = UUID.randomUUID().toString();
        //将验证码存储到缓存
        RedisUtils.setWithExpire(AuthConstant.IMG_REDIS_PREFIX + uuid, code, exp, TimeUnit.SECONDS);
        return new ImgVerifyCodeVO().uuid(uuid).imageBase64Data(captcha.getImageBase64Data());
    }

    @Override
    public void validateVerifyCode(ValidateVerifyCodeDTO verifyCodeDTO) {
        Object code = null;
        //图形验证码
        if (verifyCodeDTO.getType() == VerifyCodeType.PIC.ordinal()) {
            code = RedisUtils.get(AuthConstant.IMG_REDIS_PREFIX + verifyCodeDTO.getUuid());
            RedisUtils.del(AuthConstant.IMG_REDIS_PREFIX + verifyCodeDTO.getUuid());
            if (code == null) {//过期
                throw new AuthenticationException(AuthConstant.EXPIRED_VERIFICATION_RETURN_MESSAGE);
            }
            if (!StrUtil.equals(verifyCodeDTO.getImgCode(), code.toString(), false)) {//无效
                throw new AuthenticationException(AuthConstant.VERIFY_CODE_ERROR_RETURN_MESSAGE);
            }
            return;
        }
        //短信验证码
        code = RedisUtils.get(AuthConstant.SMS_REDIS_PREFIX + verifyCodeDTO.getPhoneNumber());
        RedisUtils.del(AuthConstant.SMS_REDIS_PREFIX + verifyCodeDTO.getPhoneNumber());
        if (code == null) {//过期
            throw new AuthenticationException(AuthConstant.EXPIRED_VERIFICATION_RETURN_MESSAGE);
        }
        if (!StrUtil.equals(verifyCodeDTO.getSmsCode(), code.toString(), false)) {//无效
            throw new AuthenticationException(AuthConstant.VERIFY_CODE_ERROR_RETURN_MESSAGE);
        }
    }

    @Override
    public Map<String, Object> ocr(OCRDTO ocrdto, MultipartFile file) {
        Map<String, Object> ocrResult=null;
        try{
            //将图片文件编码成base64字符串
            String base64Img = Base64.encode(file.getInputStream());
            if (ocrdto.getType() == 0) {
                String side = "";
                if (ocrdto.getSide() == 0) {
                    side = AuthConstant.IMG_FRONT;
                } else {
//                    side = AuthConstant.IMG_BACK;
                }
                //TODO 身份证信息识别 待完善图片
                ocrResult = BaiduAIUtils.ocr(ocrdto.getType(), base64Img, side);
            }
        } catch (Exception e) {
            throw new AuthenticationException(AuthConstant.OCR_ERROR);
        }
        return ocrResult;

    }


}
