package com.project.game.global.config;

import com.project.game.global.filter.JwtAuthenticationFilter;
import com.project.game.global.handler.OAuth2SuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final String[] swaggerPath = {"/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/error", "configuration/security"};

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                ) // cors 정책 허용

                .csrf(CsrfConfigurer::disable) // JWT를 사용하기 때문에 비활성화 (CSRF라는 보안 기능)
                .httpBasic(HttpBasicConfigurer::disable) // JWT를 사용하기 때문에 비활성화 (기본 HTTP 인증 방식)

                .sessionManagement(sessionManagement -> sessionManagement // JWT를 사용하기 때문에 비활성화 (사용자 세션)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/admin/auth/login").permitAll()
                        .requestMatchers
                                ("/api/admin/auth/logout",
                                        "/api/admin/game/**",
                                        "/api/admin/user/**").hasRole("ADMIN")

                        .requestMatchers("/api/user/auth/**"
                                , "/oauth2/**"
                                , "/file/**"
                                //, "/api/user/order/pay/approve"
                                //, "/api/user/order/pay/cancel"
                                , "/api/user/payment/**"
                                , "/api/user/order/**"
                                , "/**").permitAll()

                        .requestMatchers(swaggerPath).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/user/board/**"
                                , "/api/user/game/**"
                                , "/api/user/review/**"
                                , "/api/user/comment/**").permitAll()

                        .anyRequest().authenticated()
                )       // 그 외의 요청은 인증을 필요

                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(defaultOAuth2UserService))
                        // 사용자가 OAuth 아이디, 비밀번호를 치고 로그인 버튼 눌렀을 때defaultOAuth2UserService 여기서 처리
                        .successHandler(oAuth2SuccessHandler)  // defaultOAuth2UserService 성공적일 때
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())
                        .accessDeniedHandler(new FailedAccessDeniedHandler())
                        // Security 컨택스트에 사용자의 정보가 없으면 인증 실패로 간주
                )       // hasRole 권한이 다를 경우 실패 핸들러

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                    // jwtAuthenticationFilter -> 요청 header에 토큰을 검사
                    // UsernamePasswordAuthenticationFilter를 사용하지 않을 것이므로 jwt 필터를 먼저 실행
        return httpSecurity.build();
    }

    @Bean
    protected UrlBasedCorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://genu99.github.io"); // 정확한 출처 설정
        //configuration.addAllowedOrigin("http://localhost:3000"); // 정확한 출처 설정
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // 쿠키 및 인증 정보를 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    // 401 에러 즉, 사용자 인증 실패하면 실행 후 commence() 메서드 실행

    class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{ \"code\": \"AUTHORIZATION_FAIL\", \"message\": \"사용자 인증에 실패하였습니다.\" }");
        }
    }

    class FailedAccessDeniedHandler implements AccessDeniedHandler{

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{ \"code\": \"FORBIDDEN_FAIL\", \"message\": \"권한이 없습니다.\" }");
        }
    }
}
