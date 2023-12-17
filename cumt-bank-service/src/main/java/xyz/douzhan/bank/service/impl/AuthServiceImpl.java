package xyz.douzhan.bank.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.constants.*;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.OCRDTO;
import xyz.douzhan.bank.dto.ValidateVerifyCodeDTO;
import xyz.douzhan.bank.enums.VerifyCodeType;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.service.AsyncService;
import xyz.douzhan.bank.service.AuthService;
import xyz.douzhan.bank.service.BankPhoneBankRefService;
import xyz.douzhan.bank.service.PhoneAccountService;
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
    private final PhoneAccountService phoneAccountService;
    private final BankPhoneBankRefService bankPhoneBankRefService;

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
        AbstractCaptcha captcha = VerifyCodeUtil.genVerifyCode(VerifyCodeType.PURENUM);
        String code = captcha.getCode();

        //类型为短信验证码
        if (VerifyCodeType.SMS.ordinal() == type) {
            //校验手机号不能为空
            CommonBizUtil.assertArgsNotNull(phoneNumber);
            //验证码放入缓存
            RedisUtil.setWithExpire(RedisConstant.SMS_REDIS_PREFIX + phoneNumber, code, exp, TimeUnit.SECONDS);
            //发送短信
            asyncService.sendMessage(code, phoneNumber);
            //返回null就行
            return null;
        }
        //类型为图形验证码
        //生成验证码序号
        String uuid = UUID.randomUUID().toString();
        //将验证码存储到缓存
        RedisUtil.setWithExpire(RedisConstant.IMG_REDIS_PREFIX + uuid, code, exp, TimeUnit.SECONDS);
        return new ImgVerifyCodeVO().uuid(uuid).imageBase64Data(captcha.getImageBase64Data());
    }

    @Override
    public void validateVerifyCode(ValidateVerifyCodeDTO verifyCodeDTO) {
        Object code = null;
        // 图形验证码
        if (verifyCodeDTO.getType() == VerifyCodeType.PIC.ordinal()) {
            code = RedisUtil.get(RedisConstant.IMG_REDIS_PREFIX + verifyCodeDTO.getUuid());
            // 取出立马删除
            RedisUtil.del(RedisConstant.IMG_REDIS_PREFIX + verifyCodeDTO.getUuid());
            // 过期或错误
            if (code == null || !StrUtil.equals(verifyCodeDTO.getImgCode(), code.toString())) {
                throw new AuthenticationException(AuthExceptionConstant.VERFIY_CODE_ERROR);
            }
            return;
        }
        // 短信验证码
        code = RedisUtil.get(RedisConstant.SMS_REDIS_PREFIX + verifyCodeDTO.getPhoneNumber());
        // 过期或错误
        if (code == null || !StrUtil.equals(verifyCodeDTO.getSmsCode(), code.toString())) {
            throw new AuthenticationException(AuthExceptionConstant.VERFIY_CODE_ERROR);
        }
    }

    /**
     * 图像识别
     *
     * @param ocrdto
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> ocr(OCRDTO ocrdto, MultipartFile file) {
        Map<String, Object> ocrResult = null;
        String side=null;
        try {
            // 将图片文件编码成base64字符串
            String base64Img = Base64.encode(file.getInputStream());
            // 正反面
            if (BizConstant.PIC_FRONT.compareTo(ocrdto.getType())==0){
                side=BizConstant.DOCUMENTS_FRONT;
            }else {
                side=BizConstant.DOCUMENTS_BACK;
            }
            ocrResult = BaiduAIUtil.ocr(ocrdto.getType(), base64Img, side);
        } catch (Exception e) {
            throw new ThirdPartyAPIException(ThirdAPIExceptionConstant.BAIDU_OCR_ERROR);
        }
        return ocrResult;

    }

    /**
     * 上传证件
     *
     * @param frontFile
     * @param backFile
     * @param firstAccountId
     * @return
     */
    @Override
    public JSONObject uploadDocuments(MultipartFile frontFile, MultipartFile backFile, Long firstAccountId) {
        String frontDocumentsURI = MinIOUtil.uploadDocuments(frontFile, firstAccountId, BizConstant.DOCUMENTS_FRONT);
        String backDocumentsURI = MinIOUtil.uploadDocuments(backFile, firstAccountId, BizConstant.DOCUMENTS_BACK);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(BizConstant.DOCUMENTS_FRONT, frontDocumentsURI);
        jsonObject.put(BizConstant.DOCUMENTS_BACK, backDocumentsURI);
        return jsonObject;
    }

    /**
     * 注册前的人脸认证
     *
     * @param frontFile
     * @param liveFile
     * @return
     */
    @Override
    public boolean faceAuthTemp(MultipartFile frontFile, MultipartFile liveFile) {
        return faceAuthCommon(frontFile, null, liveFile);
    }


    /**
     * 注册后的人脸认证
     *
     * @param liveFile
     * @return
     */
    @Override
    public Boolean faceAuth(MultipartFile liveFile) {
        //加载证件正面照片
        Long phoneAccountId = UserContext.getContext();
        PhoneAccount phoneAccount = phoneAccountService.getOne(Wrappers.lambdaQuery(PhoneAccount.class)
                .eq(PhoneAccount::getId, phoneAccountId)
                .select(PhoneAccount::getDocFrontUri));
        byte[] frontFile = MinIOUtil.download(phoneAccount.getDocFrontUri());
        //验证
        faceAuthCommon(null, frontFile, liveFile);
        return null;
    }

    private boolean faceAuthCommon(MultipartFile frontFile1, byte[] frontFile2, MultipartFile liveFile) {
        boolean isSuccess = false;
        try {
            String base64IDCard = null;
            if (frontFile1 != null) {
                base64IDCard = Base64.encode(frontFile1.getInputStream());
            } else {
                base64IDCard = Base64.encode(frontFile2);
            }
            String base64Live = Base64.encode(liveFile.getInputStream());
            isSuccess = BaiduAIUtil.faceAuth(base64Live, base64IDCard);
        } catch (Exception e) {
            throw new ThirdPartyAPIException(ThirdAPIExceptionConstant.BAIDU_FACE_AUTH_FAILED);
        }
        return isSuccess;
    }

}
