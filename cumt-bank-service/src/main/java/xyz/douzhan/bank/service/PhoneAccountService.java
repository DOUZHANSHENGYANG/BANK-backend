package xyz.douzhan.bank.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.dto.LoginDTO;
import xyz.douzhan.bank.dto.RegisterDTO;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.po.PhoneAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface PhoneAccountService extends IService<PhoneAccount> {
    /**
     * 注册手机账户
     *
     * @param registerDTO
     * @return
     */
    String register(RegisterDTO registerDTO);

    /**
     * 上传头像
     * @param file
     * @return
     */
    String upload(MultipartFile file);

    /**
     * 手机号短信登录
     *
     * @param loginDTO
     * @return
     */
    String smsLogin(LoginDTO loginDTO);
    /**
     * 手机号、证件号 密码登录
     *
     * @param loginDTO
     * @return
     */
    String usernamePWDLogin(LoginDTO loginDTO);

    /**
     * 根据id修改手机号
     *
     * @param phoneAccountId
     * @param phoneNumber
     * @return
     */
    ResponseResult updatePhone(Long phoneAccountId, String phoneNumber);

    /**
     * 根据id类型修改密码
     *
     * @param phoneAccountId
     * @param type
     * @param password
     * @param oldPassword
     * @return
     */
    ResponseResult modifyPassword(Long phoneAccountId, Integer type, String password, String oldPassword);
    /**
     * 根据id类型重置密码
     *
     * @param phoneAccountId
     * @param type
     * @param password
     * @return
     */
    ResponseResult resetPassword(Long phoneAccountId, Integer type, String password);

    /**
     * 注销账户
     * @param phoneAccountId
     */
    void deleteAccount(Long phoneAccountId);
}
