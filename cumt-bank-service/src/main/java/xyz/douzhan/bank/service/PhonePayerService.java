package xyz.douzhan.bank.service;

import xyz.douzhan.bank.po.PhonePayer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface PhonePayerService extends IService<PhonePayer> {
    /**
     * 修改
     * @param phonePayer
     */
    void update(PhonePayer phonePayer);
}
