package xyz.douzhan.bank.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.constants.BizConstant;
import xyz.douzhan.bank.constants.TransferConstant;
import xyz.douzhan.bank.po.Transfer;
import xyz.douzhan.bank.service.AsyncService;
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
@Slf4j
public class AsyncServiceImpl implements AsyncService {
    private static final String QR_CODE_ORDER_CRON="15 * * * * *";

    @Override
    @Async("MyAsyncPoolTaskExecutor")
    public void sendMessage(String code,String phoneNumber)  {
        AliYunUtil.sendSM( code, phoneNumber);
    }

}
