package com.example.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 原有的资源处理配置
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().toString() + File.separator;
        String location = "file:" + uploadPath;
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }

    // 新增：CORS 跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
