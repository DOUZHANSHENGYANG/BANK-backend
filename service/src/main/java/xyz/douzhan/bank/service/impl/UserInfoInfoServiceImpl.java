package xyz.douzhan.bank.service.impl;

import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.mapper.UserInfoMapper;
import xyz.douzhan.bank.service.UserInfoService;
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
@Transactional
public class UserInfoInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


}
