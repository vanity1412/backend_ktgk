package com.utetea.backend.config;
//VU VAN THONG 23162098
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//VU VAN THONG 23162098
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve images from assets/ folder
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("file:assets/");
    }
}
