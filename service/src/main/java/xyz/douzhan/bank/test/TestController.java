package xyz.douzhan.bank.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 一些声明信息
 * Description:测试接口类
 * date: 2023/12/1 23:11
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@RestController
@Tag(name="测试接口")
@RequestMapping("test")
public class TestController {
    @GetMapping("print")
    @Operation(summary = "测试接口方法描述")
    public TestDTO print(
            @RequestParam(required = false,name = "test")
            @Parameter(description = "测试请求参数") String test
    ){
        return new TestDTO().test(test+"congrats you!");
    }
}
