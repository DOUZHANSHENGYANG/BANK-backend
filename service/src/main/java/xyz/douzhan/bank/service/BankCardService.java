package xyz.douzhan.bank.service;

import xyz.douzhan.bank.po.Bankcard;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.douzhan.bank.vo.BankCardVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface BankCardService extends IService<Bankcard> {

    List<BankCardVO> getByPhoneNumber(String phoneNumber, Integer type);
}
