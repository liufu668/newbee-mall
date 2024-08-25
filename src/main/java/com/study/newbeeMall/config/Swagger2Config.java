package com.study.newbeeMall.config;

import com.study.newbeeMall.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2 //启用Swagger API文档
public class Swagger2Config {

    /**
     * 返回实例Docket(Swagger API摘要)
     * 在方法中指定扫描的控制器包路径,
     * 只有在此路径下的Controller类才会自动生成Swagger API文档
     * @return
     */
    @Bean
    public Docket api(){
        // 创建一个 ParameterBuilder 实例，用于构建 Swagger 请求参数
        ParameterBuilder tokenParam = new ParameterBuilder();

        // 创建一个参数列表，用于存储所有 Swagger 文档的请求参数
        List<Parameter> swaggerParams = new ArrayList<>();

        // 配置 token 参数的详细信息
        tokenParam.name("token") // 参数名称为 "token"
                .description("用户认证信息") // 参数描述
                .modelRef(new ModelRef("string")) // 参数类型为字符串
                .parameterType("header") // 参数位置为请求头
                .required(false) // 这个参数不是必需的
                .build(); // 完成构建参数

        // 将构建好的 token 参数添加到参数列表中
        swaggerParams.add(tokenParam.build());

        return new Docket(DocumentationType.SWAGGER_2)  // 指定 Swagger 的版本为 2
                .apiInfo(apiInfo())  // 设定 API 文档的基本信息
                .ignoredParameterTypes(User.class)
                .select()  // 进入 API 选择模式
                .apis(RequestHandlerSelectors.basePackage("com.study.newbeeMall.api"))  // 指定扫描的包路径，Swagger 将扫描该包下的所有 Controller 类
                .paths(PathSelectors.any())  // 指定要包含的路径，PathSelectors.any() 表示包括所有路径
                .build()
                .globalOperationParameters(swaggerParams);  // 完成 Docket 实例的构建
    }

    /**
     * 配置一些基本的显示信息,
     * 比如标题,描述,版本,服务条款,联系方式等
     * @return
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("新蜂商城接口文档")
                .description("swagger 文档 by liufu")
                .version("2.0")
                .build();
    }
}