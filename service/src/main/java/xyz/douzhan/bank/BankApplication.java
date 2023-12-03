package xyz.douzhan.bank;

import jakarta.validation.constraints.Max;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 一些声明信息
 * Description: 项目启动类
 * date: 2023/12/1 23:10
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@SpringBootApplication
@MapperScan("xyz.douzhan.bank.mapper")
public class BankApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }
}
