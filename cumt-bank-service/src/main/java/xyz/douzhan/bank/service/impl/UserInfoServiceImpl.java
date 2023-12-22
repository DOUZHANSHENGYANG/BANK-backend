package xyz.douzhan.bank.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.mapper.UserInfoMapper;
import xyz.douzhan.bank.service.PhoneAccountService;
import xyz.douzhan.bank.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.CypherUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Override
    public UserInfo getInfo() {
        //查询账户对应用户信息id
        PhoneAccount phoneAccount = Db.lambdaQuery(PhoneAccount.class)
                .eq(PhoneAccount::getId, UserContext.getContext())
                .select(PhoneAccount::getUserInfoId)
                .one();

        //查询用户信息
        return baseMapper.selectById(phoneAccount.getUserInfoId());
    }

    /**
     * 更新用户信息
     *
     * @param userInfo
     */
    @Override
    public void updateUserInfo(UserInfo userInfo) {
        PhoneAccount phoneAccount = Db.getOne(Wrappers.lambdaQuery(PhoneAccount.class).eq(PhoneAccount::getId, UserContext.getContext()).select(PhoneAccount::getUserInfoId));
        userInfo.setId(phoneAccount.getUserInfoId());
        baseMapper.updateById(userInfo);
    }

}
