package xyz.douzhan.bank.mapper;

import xyz.douzhan.bank.po.Transaction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-03
 */
@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {

}
