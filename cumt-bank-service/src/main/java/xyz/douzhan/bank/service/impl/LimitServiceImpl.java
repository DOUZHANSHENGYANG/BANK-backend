package xyz.douzhan.bank.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.mapper.LimitMapper;
import xyz.douzhan.bank.po.Limit;
import xyz.douzhan.bank.service.AsyncService;
import xyz.douzhan.bank.service.LimitService;
import xyz.douzhan.bank.utils.AliYunUtil;

/**
 * 一些声明信息
 * Description: 异步服务实现类
 * date: 2023/12/11 10:34
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Service

public class LimitServiceImpl extends ServiceImpl<LimitMapper,Limit> implements LimitService {
}
