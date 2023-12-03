package xyz.douzhan.bank.service.impl;


import xyz.douzhan.bank.po.User;
import xyz.douzhan.bank.mapper.UserMapper;
import xyz.douzhan.bank.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
