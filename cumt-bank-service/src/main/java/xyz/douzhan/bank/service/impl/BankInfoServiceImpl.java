package xyz.douzhan.bank.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.mapper.BankInfoMapper;
import xyz.douzhan.bank.po.BankInfo;
import xyz.douzhan.bank.service.BankInfoService;

/**
 * Description:
 * date: 2023/12/16 0:18
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Service
public class BankInfoServiceImpl extends ServiceImpl<BankInfoMapper, BankInfo> implements BankInfoService {
}
