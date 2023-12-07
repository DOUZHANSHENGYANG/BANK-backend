package xyz.douzhan.bank.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import xyz.douzhan.bank.dto.RegisterDTO;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.mapper.PhoneAccountMapper;
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.service.PhoneAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
public class PhoneAccountServiceImpl extends ServiceImpl<PhoneAccountMapper, PhoneAccount> implements PhoneAccountService {

    @Override
    public Long register(RegisterDTO registerDTO) {
        PhoneAccount phoneAccount = new PhoneAccount();
        //复制信息
        BeanUtils.copyProperties(registerDTO,phoneAccount);
        //查询用户信息id
        UserInfo userInfo = Db.lambdaQuery(UserInfo.class).eq(UserInfo::getCardNum, registerDTO.getDocumentsNumber()).one();
        Long userInfoId = userInfo.getId();
        //设置用户信息id
        phoneAccount.setUserInfoId(userInfoId);
        //设置用户角色
        phoneAccount.setRole("USER");
        //插入
        baseMapper.insert(phoneAccount);
        return phoneAccount.getId();
    }
}
