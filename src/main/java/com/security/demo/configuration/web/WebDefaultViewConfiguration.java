package com.security.demo.configuration.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author coco
 * @date 2020-09-06 14:43
 **/
@Configuration
public class WebDefaultViewConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 这里之所以多了一"/",是为了解决打war时访问不到问题
        registry.addResourceHandler("/**")
                .addResourceLocations("/","classpath:/views","classpath:/static");
    }
}
