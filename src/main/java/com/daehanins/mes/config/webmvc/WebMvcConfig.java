package com.daehanins.mes.config.webmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.awt.image.BufferedImage;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

//    @Bean
//    public CommonsRequestLoggingFilter logFilter() {
//        CommonsRequestLoggingFilter filter
//                = new CommonsRequestLoggingFilter();
//        filter.setIncludeQueryString(true);
//        filter.setIncludePayload(true);
//        filter.setMaxPayloadLength(10000);
//        filter.setIncludeHeaders(false);
//        filter.setAfterMessagePrefix("REQUEST DATA : ");
//        return filter;
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/").setCachePeriod(31556926);
        registry.addResourceHandler("/pdfs/**").addResourceLocations("classpath:/static/pdfs/").setCachePeriod(31556926);
    }

//    @Bean
//    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
//        return new BufferedImageHttpMessageConverter();
//    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new BufferedImageHttpMessageConverter());
//        converters.add(new ByteArrayHttpMessageConverter());
//    }
}
