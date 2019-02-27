package com.kasite;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
//@Profile({ "dev", "test" })  
public class Swagger2 {
//swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.kasite"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;
    }
    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
    	
    	ApiInfoBuilder builder = new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
                //创建人
                .contact(new Contact("daiys", "http://www.jkzl.com", "343675979@qq.com"))
                //版本号
                .version("2.0")
                //描述
                .description("API 描述");
    		
        return builder.build();
    }
    
    private List<SecurityContext> securityContexts() {
    		List<SecurityContext> list = new ArrayList<>();
    		list.add(SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("/*.*"))
                        .build());
        return list;
    }
    
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        
        List<SecurityReference> list = new ArrayList<>();
        list.add(new SecurityReference("clientId", authorizationScopes));
        list.add(new SecurityReference("clientSecret", authorizationScopes));
    		list.add(new SecurityReference("accessToken", authorizationScopes));
    		
    		return list;
    }
    
    private List<ApiKey> securitySchemes() {
        List<ApiKey> list = new ArrayList<>();
        list.add(new ApiKey("clientId", "客户端ID", "header"));
        list.add(new ApiKey("clientSecret", "客户端秘钥", "header"));
    		list.add(new ApiKey("accessToken", "客户端访问标识", "header"));
        return list;
    }
 
}