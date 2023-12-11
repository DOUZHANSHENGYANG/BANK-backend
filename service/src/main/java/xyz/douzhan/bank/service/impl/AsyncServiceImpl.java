package xyz.douzhan.bank.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.service.AsyncService;
import xyz.douzhan.bank.utils.AliYunUtils;

/**
 * 一些声明信息
 * Description: 异步服务实现类
 * date: 2023/12/11 10:34
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {
    @Override
    @Async
    public void sendMessage(String code,String phoneNumber) throws Exception {
        AliYunUtils.sendSM( code, phoneNumber);
    }
}
