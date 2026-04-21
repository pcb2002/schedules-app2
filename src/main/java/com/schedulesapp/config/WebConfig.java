package com.schedulesapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;

    public WebConfig(LoginCheckInterceptor loginCheckInterceptor) {
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                // 1. 원칙: api로 시작하는 모든 요청은 일단 검문소(로그인 확인)를 거쳐라!
                // .addPathPatterns("/api/**")

                // 2. 예외: 단, 회원가입(유저 생성)과 로그인은 검문 없이 통과시켜라! (프리패스)
                .excludePathPatterns(
                        "/signup",
                        "/login"
                );
    }
}
