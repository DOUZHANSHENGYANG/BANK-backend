package xyz.douzhan.bank.demo1;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.douzhan.bank.po.Safe;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/3 22:16
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@SpringBootTest
public class BTest {
    @Test
    public void testGetSafe(){
        String username="zhangsan123";
        Safe safe = Db.lambdaQuery(Safe.class).eq(Safe::getUsername, username).one();
        System.out.println("safe.getUsername() = " + safe.getUsername());
    }
}
