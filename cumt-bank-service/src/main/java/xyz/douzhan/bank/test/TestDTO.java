package xyz.douzhan.bank.test;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/2 12:54
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Accessors(fluent = true,chain = true)
@Schema(name = "测试dto",description = "测试dto描述")
public class TestDTO {
    @Schema(name = "测试dto属性",description = "测试dto属性描述")
    private String test;
}
