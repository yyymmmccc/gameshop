package com.project.game.config;

import com.project.game.filter.JwtAuthenticationFilter;
import com.project.game.handler.OAuth2SuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
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
    private final String[] swaggerPath = {"/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/error", "configuratuin/security"};

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(CsrfConfigurer::disable) // JWT를 사용하기 때문에 비활성화 (CSRF라는 보안 기능)
                .httpBasic(HttpBasicConfigurer::disable) // JWT를 사용하기 때문에 비활성화 (기본 HTTP 인증 방식)
                .sessionManagement(sessionManagement -> sessionManagement // JWT를 사용하기 때문에 비활성화 (사용자 세션)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers( "/api/auth/join", "/api/auth/login",
                                "/api/auth/refresh-token", "/api/auth/check-certification", "/api/auth/**"
                                , "/oauth2/**", "/api/comment/**", "/file/**", "/api/auth/find-email"
                                , "/api/order/pay/approve", "/api/order/pay/cancel").permitAll()
                        .requestMatchers(swaggerPath).permitAll()
                        // 모든 사용자는 해당 url 접근을 허용시킨다는 말

                        .requestMatchers(HttpMethod.GET, "/api/board/**", "/api/game/**", "/api/review/**", "/api/cart/**").permitAll()
                        // 모든사용자는 해당 url GET방식은 허용

                        .anyRequest().authenticated()
                )       // 그 외의 요청은 인증을 필요로 한다는 말 (로그인 한 사용자만 가능)

                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(defaultOAuth2UserService))
                        // 사용자가 OAuth 아이디, 비밀번호를 치고 로그인 버튼 눌렀을 때defaultOAuth2UserService 여기서 처리
                        .successHandler(oAuth2SuccessHandler)  // defaultOAuth2UserService 성공적일 때
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())
                        // Security 컨택스트에 사용자의 정보가 없으면 인증 실패로 간주
                )       // 인증 실패 시 예외 처리 로직을 정의 -> new FailedAuthentication

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                    // jwtAuthenticationFilter -> 요청 header에 토큰을 검사
                    // UsernamePasswordAuthenticationFilter를 사용하지 않을 것이므로 jwt 필터를 먼저 실행
        return httpSecurity.build();
    }

    @Bean
    protected UrlBasedCorsConfigurationSource corsConfigurationSource(){
        // 다른 도메인, 리액트 허용하기 위함
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 모든 도메인 요청 허용
        configuration.addAllowedMethod("*"); // 모든 Http 메서드 허용
        configuration.addExposedHeader("*"); // 모든 헤더 허용
        // 모든 도메인, 모든 메서드, 모든 헤더를 허용하도록 설정합니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        // 설정한 것을 모든 URL 적용

        return source;
    }

    // 401 에러 즉, 사용자 인증 실패하면 실행 후 commence() 메서드 실행
    class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint{
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{ \"code\": \"AUTHORIZATION_FAIL\", \"message\": \"사용자 인증에 실패하였습니다.\" }");
        }
    }
}
