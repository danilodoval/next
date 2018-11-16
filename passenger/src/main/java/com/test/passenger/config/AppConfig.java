package com.test.passenger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.javasampleapproach.corsjavaconfig")
public class AppConfig implements WebMvcConfigurer {

    @Value("${driver.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("POST", "GET", "PUT", "PATH", "DELETE")
                .allowedHeaders("Content-Type")
                .exposedHeaders("header-1", "header-2")
                .allowCredentials(false)
                .maxAge(6000);
    }
}
