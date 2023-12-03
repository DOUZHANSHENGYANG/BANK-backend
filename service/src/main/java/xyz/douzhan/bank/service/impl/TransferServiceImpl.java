package xyz.douzhan.bank.service.impl;


import xyz.douzhan.bank.po.Transfer;
import xyz.douzhan.bank.mapper.TransferMapper;
import xyz.douzhan.bank.service.TransferService;
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
public class TransferServiceImpl extends ServiceImpl<TransferMapper, Transfer> implements TransferService {

}
