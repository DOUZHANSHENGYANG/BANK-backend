package xyz.douzhan.bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/1 23:11
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("print")
    public ResponseEntity print(){
        return ResponseEntity.ok("Hello world");
    }
}
