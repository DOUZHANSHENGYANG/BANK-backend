package xyz.douzhan.bank.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import xyz.douzhan.bank.po.PhoneAccount;

import java.util.Collection;

/**
 * 一些声明信息
 * Description: 自定义userDetails
 * date: 2023/12/2 19:58
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class CustomUserDetails extends User {
    private PhoneAccount phoneAccount;
    public CustomUserDetails(PhoneAccount phoneAccount, Collection<? extends GrantedAuthority> authorities) {
        super(phoneAccount.getUsername(), phoneAccount.getAccountPwd(), authorities);
        this.phoneAccount=phoneAccount;
    }
    public Long getPhoneAccountId() {
        return phoneAccount.getId();
    }

    public void setPhoneAccount(PhoneAccount phoneAccount) {
        this.phoneAccount = phoneAccount;
    }
}
