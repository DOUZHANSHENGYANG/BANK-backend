package xyz.douzhan.bank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 一些声明信息
 * Description:   swagger接口文档
 * date: 2023/12/1 23:51
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Configuration
public class Knife4jConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank 接口文档")
                        .description("手机银行接口文档")
                        .version("v1.0.0")
                        .contact(new Contact().name("斗战圣洋").email("3555936530@qq.com"))
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                );

    }

    @Bean
    public GroupedOpenApi clientApi() {
        return GroupedOpenApi.builder()
                .group("bank-client")
                .pathsToMatch("/bank/**")
                .build();
    }

    @Bean
    public GroupedOpenApi commonApi() {
        return GroupedOpenApi.builder()
                .group("bank-common")
                .pathsToMatch("/common/**")
                .build();
    }


    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("bank-admin")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi testApi() {
        return GroupedOpenApi.builder()
                .group("bank-test")
                .pathsToMatch("/test/**")
                .build();
    }


//    @Bean
//    public OpenAPI springShopOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Bank API")
//                        .description("手机银行接口文档")
//                        .version("v0.0.1")
//                        .license(new License().name("Apache 2.0").url("https://github.com/DOUZHANSHENGYANG")))
//                .externalDocs(new ExternalDocumentation()
//                        .description("Bank Wiki Documentation")
//                        .url("https://kecat.top"));
//
//    }

//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("springshop-admin")
//                .pathsToMatch("/admin/**")
//                .addOpenApiMethodFilter(method -> method.isAnnotationPresent(Admin.class))
//                .build();
//    }

}