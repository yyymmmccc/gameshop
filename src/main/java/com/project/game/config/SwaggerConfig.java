package com.project.game.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        // JWT SecurityRequirement 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);

        // Authorization 헤더와 Refresh Token 헤더 SecurityScheme 설정
        Components components = new Components()
                .addSecuritySchemes(AUTHORIZATION_HEADER, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .name(AUTHORIZATION_HEADER)
                        .in(SecurityScheme.In.HEADER))
                .addSecuritySchemes(REFRESH_TOKEN_HEADER, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .name(REFRESH_TOKEN_HEADER)
                        .in(SecurityScheme.In.HEADER));

        // OpenAPI 객체 생성 및 설정
        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
                .title("게임 쇼핑몰 API") // API의 제목
                .version("1.0.0"); // API의 버전
    }
}