package xyz.douzhan.bank.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.dto.LoginDTO;
import xyz.douzhan.bank.dto.RegisterDTO;
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
     * @param registerDTO
     * @return
     */
    Long register(RegisterDTO registerDTO);

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
     * @param id
     * @param phoneNumber
     * @return
     */
    boolean updatePhone(Long id, String phoneNumber);

    /**
     * 根据id类型修改密码
     *
     * @param id
     * @param type
     * @param password
     * @param oldPassword
     * @return
     */
    boolean modifyPassword(Long id, Integer type, String password, String oldPassword);
    /**
     * 根据id类型重置密码
     *
     * @param id
     * @param type
     * @param password
     * @return
     */
    boolean resetPassword(Long id, Integer type, String password);

    /**
     * 注销账户
     * @param id
     */
    void deleteAccount(Long id);
}
