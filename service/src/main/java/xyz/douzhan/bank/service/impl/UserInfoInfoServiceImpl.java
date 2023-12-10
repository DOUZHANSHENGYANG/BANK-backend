package xyz.douzhan.bank.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.po.PhoneAccount;
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.mapper.UserInfoMapper;
import xyz.douzhan.bank.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.BizUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
@Transactional
public class UserInfoInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Override
    public UserInfo getInfo(Long id) {
        //参数非空校验
        BizUtils.assertArgsNotNull(id);
        //查询账户对应用户信息id
        PhoneAccount phoneAccount = Db.lambdaQuery(PhoneAccount.class).eq(PhoneAccount::getId, id).select(PhoneAccount::getUserInfoId).one();
        BizUtils.assertArgsNotNull(phoneAccount);
        //查询用户信息
        UserInfo userInfo = this.baseMapper.selectById(phoneAccount.getUserInfoId());
        BizUtils.assertArgsNotNull(phoneAccount);
        return userInfo;
    }

}
