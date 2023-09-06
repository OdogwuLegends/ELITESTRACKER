package com.capstoneproject.ElitesTracker.ipConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final WebUtils webUtils;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webUtils)
                .addPathPatterns("/api/**");
    }
}
