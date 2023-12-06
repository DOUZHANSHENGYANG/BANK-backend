package xyz.douzhan.bank.demo1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class UserGeneratorTest {
    @Test
    public void testGen() {
        List<String> tables = new ArrayList<>();
        tables.add("account");
        tables.add("bank_card");
        tables.add("feedback");
        tables.add("transaction");
        tables.add("user");
        tables.add("phone_feedback");
        tables.add("phone_account");
        tables.add("phone_payer");
        tables.add("bank_phone_bank");

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/bank", "root", "1234")
                .globalConfig(builder -> {
                    builder.author("斗战圣洋")               //作者
                            .outputDir(System.getProperty("user.dir") + "\\src\\main\\java")    //输出路径(写到java目录)
                            .enableSwagger()           //开启swagger
                            .commentDate("yyyy-MM-dd")
                            .fileOverride();            //开启覆盖之前生成的文件

                })
                .packageConfig(builder -> {
                    builder.parent("xyz.douzhan.bank")
                            .moduleName("test")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .mapper("mapper")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "\\src\\main\\resources\\mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .addTablePrefix("p_")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()
                            .enableLombok()
//                            .logicDeleteColumnName("deleted")
                            .enableTableFieldAnnotation()
                            .controllerBuilder()
                            // 映射路径使用连字符格式，而不是驼峰
                            .enableHyphenStyle()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            .mapperBuilder()
                            //生成通用的resultMap
                            .enableBaseResultMap()
                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
//                .templateConfig(new Consumer<TemplateConfig.Builder>() {
//                    @Override
//                    public void accept(TemplateConfig.Builder builder) {
//                        // 实体类使用我们自定义模板
//                        builder.entity("templates/myentity.java");
//                    }
//                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}

