package com.swyg.findingahomesafely.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@OpenAPIDefinition(
//        info = @Info(title = "User-Service API 명세서",
//                description = "사용자 어플 서비스 API 명세서",
//                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("세이프한 집구하기")
                        .description("세이프하게 집구하는 방법, 세집!")
                        .version("1.0.0"));
    }

}
