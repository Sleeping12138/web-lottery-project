package com.test.lotterysystem.Comment.Config;

import com.test.lotterysystem.Comment.Interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/**/*.html",
                        "/css/**",
                        "/js/**",
                        "/pic/**",
                        "/*.jpg",
                        "/*.png",
                        "/favicon.ico",
                        "/user/*/login",
                        "/user/register",
                        "/user/verificationCode/send",
                        "/winning-records/show"
                );
    }
}
