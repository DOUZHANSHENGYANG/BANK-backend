package xyz.douzhan.bank.service;

/**
 * 一些声明信息
 * Description:异步任务服务类
 * date: 2023/12/11 10:33
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface AsyncService {
    /**
     * 发送短信
     * @throws InterruptedException
     */
    public void sendMessage(String code,String phoneNumber);
}
