package xyz.douzhan.bank.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.constants.AuthConstants;
import xyz.douzhan.bank.constants.BizExceptionConstants;
import xyz.douzhan.bank.constants.BizVerifyConstants;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.LoginDTO;
import xyz.douzhan.bank.dto.RegisterDTO;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.mapper.PhoneAccountMapper;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.redis.LoginInfoRedis;
import xyz.douzhan.bank.service.PhoneAccountService;
import xyz.douzhan.bank.utils.AliYunUtils;
import xyz.douzhan.bank.utils.JWTUtils;
import xyz.douzhan.bank.utils.RedisUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
public class PhoneAccountServiceImpl extends ServiceImpl<PhoneAccountMapper, PhoneAccount> implements PhoneAccountService {

    @Override
    @Transactional
    public boolean updatePhone(Long id, String phoneNumber) {
        PhoneAccount phoneAccount = new PhoneAccount();
        phoneAccount.setId(id);
        phoneAccount.setPhoneNumber(phoneNumber);
        return this.updateById(phoneAccount);
    }

    @Override
    @Transactional
    public boolean modifyPassword(Long id, Integer type, String password, String oldPassword) {
        //检验参数非空
        if (id == null && type == null && password == null && oldPassword == null) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //检验类型参数合法性
        if (type < 0 || type > 1) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //查询密码
        PhoneAccount tempPhoneAccount = this.baseMapper.selectById(id);
        if (tempPhoneAccount == null) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //修改账号密码
        if (type == 0) {
            //比较密码是否正确
            if (StrUtil.equals(oldPassword, tempPhoneAccount.getAccountPWD())) {
                throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
            }
            //校验新密码合法性
            if (!ReUtil.isMatch(BizVerifyConstants.ACCOUNT_PASSWORD_REGEX, password) || ReUtil.isMatch(BizVerifyConstants.SIMPLE_DIGITAL_AND_CHARACTER, password)) {
                throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
            }
            //校验新密码不能为生日
            //查询生日
            UserInfo userInfo = Db.lambdaQuery(UserInfo.class)
                    .eq( UserInfo::getId, tempPhoneAccount.getUserInfoId())
                    .select(UserInfo::getDocumentsNum).one();
            if (userInfo == null) {
                throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
            }
            //比较生日
            if (ReUtil.isMatch(userInfo.getDocumentsNum().substring(9, 15), password)) {
                throw new BizException(BizExceptionConstants.CAN_NOT_INCLUDE_BIRTHDAY);
            }
            //修改密码
            PhoneAccount phoneAccount = new PhoneAccount();
            phoneAccount.setId(id);
            phoneAccount.setAccountPWD(password);
            return this.updateById(phoneAccount);

        }

        //修改支付账号密码
        //比较密码是否正确
        if (StrUtil.equals(oldPassword, tempPhoneAccount.getPayPWD())) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //校验长度
        if (password.length() != 6) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //校验是否为简单连续数字字母
        if (ReUtil.isMatch(BizVerifyConstants.SIMPLE_DIGITAL_AND_CHARACTER, password)){
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //修改密码
        PhoneAccount phoneAccount = new PhoneAccount();
        phoneAccount.setId(id);
        phoneAccount.setPayPWD(password);
        return this.updateById(phoneAccount);
    }

    @Override
    @Transactional
    public boolean resetPassword(Long id, Integer type, String password) {
        //检验参数非空
        if (id == null && type == null && password == null) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //检验类型参数合法性
        if (type < 0 || type > 1) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //修改账号密码
        if (type == 0) {
            //校验新密码合法性
            if (!ReUtil.isMatch(BizVerifyConstants.ACCOUNT_PASSWORD_REGEX, password) || ReUtil.isMatch(BizVerifyConstants.SIMPLE_DIGITAL_AND_CHARACTER, password)) {
                throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
            }
            //校验新密码不能为生日
            //查询生日
            PhoneAccount tempPhoneAccount = this.baseMapper.selectById(id);
            if(tempPhoneAccount==null){
                throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
            }
            UserInfo userInfo = Db.lambdaQuery(UserInfo.class).eq( UserInfo::getId, tempPhoneAccount.getUserInfoId()).select(UserInfo::getDocumentsNum).one();
            if (userInfo == null) {
                throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
            }
            //比较生日
            if (ReUtil.isMatch(userInfo.getDocumentsNum().substring(9, 15), password)) {
                throw new BizException(BizExceptionConstants.CAN_NOT_INCLUDE_BIRTHDAY);
            }
            //修改密码
            PhoneAccount phoneAccount = new PhoneAccount();
            phoneAccount.setId(id);
            phoneAccount.setAccountPWD(password);
            return this.updateById(phoneAccount);
        }
        //修改支付账号密码
        //校验长度
        if (password.length() != 6) {
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //修改密码
        PhoneAccount phoneAccount = new PhoneAccount();
        phoneAccount.setId(id);
        phoneAccount.setPayPWD(password);
        return this.updateById(phoneAccount);
    }

    @Override
    @Transactional
    public Long register(RegisterDTO registerDTO) {
        PhoneAccount phoneAccount = new PhoneAccount();
        //复制信息
        BeanUtils.copyProperties(registerDTO, phoneAccount);
        //查询用户信息id
        UserInfo userInfo = Db.lambdaQuery(UserInfo.class).eq(UserInfo::getDocumentsNum, registerDTO.getDocumentsNumber()).one();
        Long userInfoId = userInfo.getId();
        //设置用户信息id
        phoneAccount.setUserInfoId(userInfoId);
        //插入
        baseMapper.insert(phoneAccount);
        return phoneAccount.getId();
    }

    @Override
    @Transactional
    public String upload(MultipartFile file) {
        Long phoneAccountId = UserContext.getContext();
        //上传文件
        String url = AliYunUtils.upload(file);
        if (StrUtil.isEmpty(url)) {
            throw new ThirdPartyAPIException("阿里云上传图片失败:");
        }
        //头像url存储到数据库
        PhoneAccount phoneAccount = new PhoneAccount();
        phoneAccount.setId(phoneAccountId);
        phoneAccount.setAvatar(url);
        baseMapper.insert(phoneAccount);
        //并返回给前端
        return url;
    }

    @Override
    public String smsLogin(LoginDTO loginDTO) {
        //判断类型合法
        Integer type = loginDTO.getType();
        if (type == null || type != 0) {
            throw new AuthenticationException(AuthConstants.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
        }
        //关键字段不能为空
        String phoneNumber = loginDTO.getPhoneNumber();
        String smsCode = loginDTO.getSmsCode();
        if (phoneNumber == null || smsCode == null) {
            throw new AuthenticationException(AuthConstants.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
        }
        //检验短信验证码
        Object code = RedisUtils.get(AuthConstants.SMS_REDIS_PREFIX + phoneNumber);
        // TODO 恢复删除操作
        RedisUtils.del(AuthConstants.SMS_REDIS_PREFIX + phoneNumber);
        if (code == null || !StrUtil.equals(code.toString(), smsCode)) {
            throw new AuthenticationException(AuthConstants.AUTHENTICATION_PARAMETER_IS_INVALID);
        }
        //检验手机号合法性
        PhoneAccount phoneAccount = this.baseMapper
                .selectOne(new LambdaQueryWrapper<PhoneAccount>()
                        .eq(PhoneAccount::getPhoneNumber, phoneNumber).select(PhoneAccount::getId));
        return authPwdAndPostLogin(phoneAccount == null, phoneAccount);
    }

    @Override
    public String usernamePWDLogin(LoginDTO loginDTO) {
        //判断类型合法
        Integer type = loginDTO.getType();
        if (type == null || type == 0) {
            throw new AuthenticationException(AuthConstants.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
        }
        //手机号密码
        if (type == 1) {
            //判空操作
            String phoneNumber = loginDTO.getPhoneNumber();
            String password = loginDTO.getPassword();
            if (phoneNumber == null || password == null) {
                throw new AuthenticationException(AuthConstants.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
            }
            //查询账号
            PhoneAccount phoneAccount = this.baseMapper
                    .selectOne(new LambdaQueryWrapper<PhoneAccount>()
                            .eq(PhoneAccount::getPhoneNumber, phoneNumber).select(PhoneAccount::getId, PhoneAccount::getAccountPWD));
            //比较密码
            return authPwdAndPostLogin(phoneAccount == null || !Objects.equals(phoneAccount.getAccountPWD(), password), phoneAccount);
        }
        //证件号密码
        if (type == 2) {
            //判空操作
            String documentsNumber = loginDTO.getDocumentsNumber();
            String password = loginDTO.getPassword();
            if (documentsNumber == null || password == null) {
                throw new AuthenticationException(AuthConstants.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
            }
            //查询账号
            UserInfo userInfo = Db.lambdaQuery(UserInfo.class)
                    .eq(UserInfo::getDocumentsNum, documentsNumber)
                    .select(UserInfo::getId).one();
            PhoneAccount phoneAccount = this.baseMapper.selectOne(
                    new LambdaQueryWrapper<PhoneAccount>()
                            .eq(userInfo != null, PhoneAccount::getUserInfoId, userInfo.getId())
                            .select(PhoneAccount::getId, PhoneAccount::getAccountPWD));
            return authPwdAndPostLogin(phoneAccount == null || !Objects.equals(phoneAccount.getAccountPWD(), password), phoneAccount);
        }

        //类型不合法
        throw new AuthenticationException(AuthConstants.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
    }

    private static String authPwdAndPostLogin(boolean flag, PhoneAccount phoneAccount) {
        //比较密码
        if (flag) {
            throw new AuthenticationException(AuthConstants.AUTHENTICATION_PARAMETER_IS_INVALID);
        }
        //设置上下文
        UserContext.setContext(phoneAccount.getId());
        //生成jwt
        String token = JWTUtils.genToken(phoneAccount.getId());
        LoginInfoRedis loginInfoRedis = new LoginInfoRedis();
        loginInfoRedis.phoneAccountId(phoneAccount.getId());
        //设置缓存
        RedisUtils.setWithExpire(AuthConstants.USR_JWT_PREFIX + token, loginInfoRedis, JWTUtils.getJwtProperties().getTtl(), TimeUnit.DAYS);

        return token;
    }
}
