package com.project.game.global.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.servers.Server;
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
        String accessKey = "Access Token";  // 설명
        String refreshKey = "Refresh Token";  // 입력칸 설명

        // JWT SecurityRequirement 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(accessKey).addList(refreshKey);

        SecurityScheme accessTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Jwt")
                .in(SecurityScheme.In.HEADER)
                .name(AUTHORIZATION_HEADER); // 헤더 키 AUTHORIZATION_HEADER

        SecurityScheme refreshTokenSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(REFRESH_TOKEN_HEADER); // 헤더 키 REFRESH_TOKEN_HEADER

        Components components = new Components()
                .addSecuritySchemes(accessKey, accessTokenSecurityScheme)
                .addSecuritySchemes(refreshKey, refreshTokenSecurityScheme);

        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);

    }

    private Info apiInfo() {
        return new Info()
                .title("게임 쇼핑몰 API") // API의 제목
                .version("1.0.0"); // API의 버전
    }
}