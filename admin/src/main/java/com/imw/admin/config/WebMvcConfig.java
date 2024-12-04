package com.imw.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imw.admin.common.ApiConstants;
import com.imw.admin.common.SubscriptionInterceptor;
import com.imw.admin.common.UserExistsInOrganizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private UserExistsInOrganizationInterceptor userExistsInOrganizationInterceptor;

    @Autowired
    private SubscriptionInterceptor subscriptionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userExistsInOrganizationInterceptor)
                .addPathPatterns(ApiConstants.ADMIN+"/user/**")
                .excludePathPatterns(ApiConstants.ADMIN+"/user");

        registry.addInterceptor(subscriptionInterceptor)
                .addPathPatterns(ApiConstants.ADMIN+"/**", ApiConstants.EMPLOYEE+"/**")
                .excludePathPatterns(
                        ApiConstants.ADMIN+"/cart/**",
                        ApiConstants.ADMIN+"/subscription/**",
                        ApiConstants.ADMIN+"/subscriptions",
                        ApiConstants.ADMIN+"/order/**",
                        ApiConstants.ADMIN+"/orders"
                );
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Allow all origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Allow specific methods
                .allowedHeaders("*"); // Allow all headers
    }
}
