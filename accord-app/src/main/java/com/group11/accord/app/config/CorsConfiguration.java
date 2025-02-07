package com.group11.accord.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Value("${cors.urls}")
    private List<String> allowedOrigins;

    @Value("${cors.methods}")
    private List<String> allowedMethods;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
            .allowedOrigins(allowedOrigins.toArray(new String[0]))
            .allowedMethods(allowedMethods.toArray(new String[0]))
            .allowedHeaders("*")
            .maxAge(3600);
    }
}
