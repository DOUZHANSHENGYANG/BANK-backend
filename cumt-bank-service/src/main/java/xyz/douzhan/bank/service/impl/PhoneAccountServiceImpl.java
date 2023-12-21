package xyz.douzhan.bank.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.constants.*;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.LoginDTO;
import xyz.douzhan.bank.dto.LoginInfoRedis;
import xyz.douzhan.bank.dto.RegisterDTO;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.enums.AccountType;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.mapper.PhoneAccountMapper;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.service.BankPhoneBankRefService;
import xyz.douzhan.bank.service.PhoneAccountService;
import xyz.douzhan.bank.service.UserInfoService;
import xyz.douzhan.bank.utils.AliYunUtil;
import xyz.douzhan.bank.utils.CypherUtil;
import xyz.douzhan.bank.utils.JWTUtil;
import xyz.douzhan.bank.utils.RedisUtil;

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
@RequiredArgsConstructor
public class PhoneAccountServiceImpl extends ServiceImpl<PhoneAccountMapper, PhoneAccount> implements PhoneAccountService {
    private final BankPhoneBankRefService bankPhoneBankRefService;
    private final UserInfoService userInfoService;

    /**
     * 修改手机号
     *
     * @param phoneNumber
     * @return
     */
    @Override
    @Transactional
    public ResponseResult updatePhone( String phoneNumber) {
        Long phoneAccountId=UserContext.getContext();
        PhoneAccount phoneAccount = new PhoneAccount();
        phoneAccount.setId(phoneAccountId);
        phoneAccount.setPhoneNumber(phoneNumber);
        baseMapper.updateById(phoneAccount);
        return ResponseResult.success();
    }

    /**
     * 根据id类型修改密码
     *
     * @param type
     * @param password
     * @param oldPassword
     * @return
     */
    @Override
    @Transactional
    public ResponseResult modifyPassword( Integer type, String password, String oldPassword) {
        //检验参数非空
        if ( type == null && password == null && oldPassword == null) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //检验类型参数合法性
        if (type < 0 || type > 1) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        oldPassword=CypherUtil.decryptSM4(oldPassword);
        password=CypherUtil.decryptSM4(password);
        Long phoneAccountId=UserContext.getContext();
        //修改账号密码
        if (type == 0) {
            //查询密码
            PhoneAccount account = baseMapper.selectOne(
                    Wrappers.lambdaQuery(PhoneAccount.class)
                            .eq(PhoneAccount::getId, phoneAccountId)
                            .select(PhoneAccount::getAccountPWD, PhoneAccount::getUserInfoId));
            if (account == null) {
                throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
            }
            //比较密码是否正确
            if (!StrUtil.equals(oldPassword, account.getAccountPWD())) {
                throw new BizException(BizExceptionConstant.OLD_ACCOUNT_PWD_ERROR);
            }
            //校验新密码合法性
            if (!ReUtil.isMatch(AuthExceptionConstant.ACCOUNT_PASSWORD_REGEX, password) || ReUtil.isMatch(AuthExceptionConstant.SIMPLE_DIGITAL_AND_CHARACTER, password)) {
                throw new BizException(BizExceptionConstant.INVALID_PWD_FORMAT);
            }
            //校验新密码不能为生日
            //查询生日
            UserInfo userInfo = userInfoService.getOne(
                    Wrappers.lambdaQuery(UserInfo.class)
                            .eq(UserInfo::getId, account.getUserInfoId())
                            .select(UserInfo::getDocumentsNum));
            //比较生日
            if (ReUtil.isMatch(userInfo.getDocumentsNum().substring(9, 15), password)) {
                throw new BizException(BizExceptionConstant.INVALID_PWD_FORMAT);
            }
            //修改账户密码
            PhoneAccount phoneAccount = new PhoneAccount();
            phoneAccount.setId(phoneAccountId);
            phoneAccount.setAccountPWD(password);
            baseMapper.updateById(phoneAccount);
        } else {  //修改支付账号密码
            //查询支付密码
            BankPhoneBankRef phoneBankRef = bankPhoneBankRefService.getOne(
                    Wrappers.lambdaQuery(BankPhoneBankRef.class)
                            .eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId)
                            .select(BankPhoneBankRef::getPayPWD, BankPhoneBankRef::getId));
            if (phoneBankRef == null) {
                throw new BizException(BizExceptionConstant.INVALID_ACCOUNT_PARAMETER);
            }
            //比较密码是否正确
            if (!StrUtil.equals( oldPassword, phoneBankRef.getPayPWD())) {
                throw new BizException(BizExceptionConstant.OLD_PAY_PWD_ERROR);
            }
            //校验长度
            if (password.length() != 6) {
                throw new BizException(BizExceptionConstant.INVALID_PWD_FORMAT);
            }
            //校验是否为简单连续数字字母
            if (ReUtil.isMatch(AuthExceptionConstant.SIMPLE_DIGITAL_AND_CHARACTER, password)) {
                throw new BizException(BizExceptionConstant.INVALID_PWD_FORMAT);
            }
            //修改密码
            BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();
            bankPhoneBankRef.setId(phoneBankRef.getId());
            bankPhoneBankRef.setPayPWD(password);
            bankPhoneBankRefService.updateById(bankPhoneBankRef);
        }
        return ResponseResult.success();
    }

    /**
     * 重置密码
     *
     * @param type
     * @param password
     * @return
     */
    @Override
    @Transactional
    public ResponseResult resetPassword( Integer type, String password) {
        //检验参数非空
        if ( type == null && password == null) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        //检验类型参数合法性
        if (type < 0 || type > 1) {
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
        password=CypherUtil.decryptSM4(password);
        Long phoneAccountId=UserContext.getContext();
        //修改账号密码
        if (type == 0) {
            //校验新密码合法性
            if (!ReUtil.isMatch(AuthExceptionConstant.ACCOUNT_PASSWORD_REGEX, password) || ReUtil.isMatch(AuthExceptionConstant.SIMPLE_DIGITAL_AND_CHARACTER, password)) {
                throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
            }
            //查询生日
            PhoneAccount account = baseMapper.selectOne(
                    Wrappers.lambdaQuery(PhoneAccount.class)
                            .eq(PhoneAccount::getId, phoneAccountId)
                            .select(PhoneAccount::getUserInfoId));
            if (account == null) {
                throw new BizException(BizExceptionConstant.INVALID_PWD_FORMAT);
            }
            UserInfo userInfo = userInfoService.getOne(
                    Wrappers.lambdaQuery(UserInfo.class)
                            .eq(UserInfo::getId, account.getUserInfoId())
                            .select(UserInfo::getDocumentsNum));
            //校验新密码不能为生日
            if (ReUtil.isMatch(userInfo.getDocumentsNum().substring(9, 15), password)) {
                throw new BizException(BizExceptionConstant.CAN_NOT_INCLUDE_BIRTHDAY);
            }
            //修改密码
            PhoneAccount phoneAccount = new PhoneAccount();
            phoneAccount.setId(phoneAccountId);
            phoneAccount.setAccountPWD(password);
            baseMapper.updateById(phoneAccount);
        } else { //修改支付密码
            //校验长度
            if (password.length() != 6) {
                throw new BizException(BizExceptionConstant.INVALID_PWD_FORMAT);
            }
            //修改密码
            BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();
            bankPhoneBankRef.setPayPWD(password);
            bankPhoneBankRefService.update(bankPhoneBankRef, Wrappers.lambdaUpdate(BankPhoneBankRef.class).eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId));
        }
        return ResponseResult.success();
    }

    /**
     * 注销账户
     *
     */
    @Override
    @Transactional
    public void deleteAccount() {
        Long phoneAccountId=UserContext.getContext();
        //删除手机银行账户关联表
        bankPhoneBankRefService.remove(Wrappers.lambdaQuery(BankPhoneBankRef.class).eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId));
        //删除银行表
        this.baseMapper.deleteById(phoneAccountId);
    }

    @Override
    @Transactional
    public String register(RegisterDTO registerDTO) {
        //创建手机银行账户信息
        PhoneAccount phoneAccount = new PhoneAccount();
        BeanUtils.copyProperties(registerDTO,phoneAccount);

        //查询用户信息id
        UserInfo userInfo = userInfoService.getOne(
                Wrappers.lambdaQuery(UserInfo.class)
                        .eq(UserInfo::getDocumentsNum, registerDTO.getDocumentsNumber())
                        .select(UserInfo::getId));
        if (userInfo==null){
            throw new AuthenticationException(AuthExceptionConstant.DOCUMENTS_ERROR);
        }
        phoneAccount.setUserInfoId(userInfo.getId());
        phoneAccount.setAccountPWD(CypherUtil.decryptSM4(phoneAccount.getAccountPWD()));
        baseMapper.insert(phoneAccount);
        //创建I类账户和手机银行账户关联
        BankPhoneBankRef bankPhoneBankRef = new BankPhoneBankRef();
        bankPhoneBankRef.setPayPWD(CypherUtil.decryptSM4(registerDTO.getPayPWD()));
        bankPhoneBankRef.setAccountId(registerDTO.getFirstBankcardId());
        bankPhoneBankRef.setType(AccountType.FIRST.getValue());
        bankPhoneBankRef.setDefaultAccount(BizConstant.IS_DEFAULT_ACCOUNT);
        bankPhoneBankRef.setPhoneAccountId(phoneAccount.getId());
        bankPhoneBankRefService.save(bankPhoneBankRef);

        String token=null;
        try {
             token = JWTUtil.genToken(phoneAccount.getUserInfoId());
        }catch (Exception e){
            throw new AuthenticationException(e.getMessage());
        }
        return token;
    }

    @Override
    @Transactional
    public String upload(MultipartFile file) {
        //上传文件
        String url = AliYunUtil.upload(file,UserContext.getContext());
        if (StrUtil.isEmpty(url)) {
            throw new ThirdPartyAPIException(ThirdAPIExceptionConstant.ALIYUN_UPLOAD_AVATAR_FAILED);
        }
        //头像url存储到数据库
        PhoneAccount phoneAccount = new PhoneAccount();
        phoneAccount.setId(UserContext.getContext());
        phoneAccount.setAvatar(url);
        baseMapper.updateById(phoneAccount);
        //并返回给前端
        return url;
    }

    @Override
    public String smsLogin(LoginDTO loginDTO) {
        //判断类型合法
        Integer type = loginDTO.getType();
        if (type == null || type != 0) {
            throw new AuthenticationException(AuthExceptionConstant.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
        }
        //关键字段不能为空
        String phoneNumber = loginDTO.getPhoneNumber();
        String smsCode = loginDTO.getSmsCode();
        if (phoneNumber == null || smsCode == null) {
            throw new AuthenticationException(AuthExceptionConstant.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
        }
        //检验短信验证码
        Object code = RedisUtil.get(RedisConstant.SMS_REDIS_PREFIX + phoneNumber);
        // TODO 恢复删除操作
        RedisUtil.del(RedisConstant.SMS_REDIS_PREFIX + phoneNumber);
        if (code == null || !StrUtil.equals(code.toString(), smsCode)) {
            throw new AuthenticationException(AuthExceptionConstant.VERFIY_CODE_ERROR);
        }
        //检验手机号合法性
        PhoneAccount phoneAccount = baseMapper
                .selectOne(new LambdaQueryWrapper<PhoneAccount>()
                        .eq(PhoneAccount::getPhoneNumber, phoneNumber).select(PhoneAccount::getId));
        return authPwdAndPostLogin(phoneAccount == null, phoneAccount);
    }

    @Override
    public String usernamePWDLogin(LoginDTO loginDTO) {
        //判断类型合法
        Integer type = loginDTO.getType();
        if (type == null) {
            throw new AuthenticationException(AuthExceptionConstant.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
        }
        //手机号密码
        if (type == 1) {
            //判空操作
            String phoneNumber = loginDTO.getPhoneNumber();
            String password = loginDTO.getPassword();
            if (phoneNumber == null || password == null) {
                throw new AuthenticationException(AuthExceptionConstant.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
            }
            //查询账号
            PhoneAccount phoneAccount = baseMapper
                    .selectOne(new LambdaQueryWrapper<PhoneAccount>()
                            .eq(PhoneAccount::getPhoneNumber, phoneNumber).select(PhoneAccount::getId, PhoneAccount::getAccountPWD));
            //比较密码
            return authPwdAndPostLogin(
                    phoneAccount == null || !StrUtil.equals(
                            phoneAccount.getAccountPWD(),
                            CypherUtil.decryptSM4(password)), phoneAccount);
        }
        //证件号密码
        if (type == 2) {
            //判空操作
            String documentsNumber = loginDTO.getDocumentsNumber();
            String password = loginDTO.getPassword();
            if (documentsNumber == null || password == null) {
                throw new AuthenticationException(AuthExceptionConstant.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
            }
            //查询账号
            UserInfo userInfo = userInfoService.getOne(
                    Wrappers.lambdaQuery(UserInfo.class)
                            .eq(UserInfo::getDocumentsNum, documentsNumber)
                            .select(UserInfo::getId));

            PhoneAccount phoneAccount = baseMapper.selectOne(
                    new LambdaQueryWrapper<PhoneAccount>()
                            .eq(userInfo != null, PhoneAccount::getUserInfoId, userInfo.getId())
                            .select(PhoneAccount::getId, PhoneAccount::getAccountPWD));
            return authPwdAndPostLogin(
                    phoneAccount == null || !StrUtil.equals(
                            phoneAccount.getAccountPWD(),
                            CypherUtil.decryptSM4(password)), phoneAccount);
        }

        //类型不合法
        throw new AuthenticationException(AuthExceptionConstant.AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED);
    }

    /**
     * 验证密码且后置操作
     *
     * @param flag
     * @param phoneAccount
     * @return
     */
    private static String authPwdAndPostLogin(boolean flag, PhoneAccount phoneAccount) {
        //比较密码
        if (flag) {
            throw new AuthenticationException(AuthExceptionConstant.AUTHENTICATION_PARAMETER_IS_INVALID);
        }
        //生成jwt
        String token = JWTUtil.genToken(phoneAccount.getId());
        LoginInfoRedis loginInfoRedis = new LoginInfoRedis();
        loginInfoRedis.phoneAccountId(phoneAccount.getId());
        //设置缓存
        RedisUtil.setWithExpire(RedisConstant.USR_JWT_PREFIX + token, loginInfoRedis, JWTUtil.getJwtProperties().getTtl(), TimeUnit.DAYS);

        return token;
    }
}
