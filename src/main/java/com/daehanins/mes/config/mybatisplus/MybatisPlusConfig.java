package com.daehanins.mes.config.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Exrickx
 */
@Configuration
@MapperScan("com.daehanins.mes.*.*.mapper")
public class MybatisPlusConfig {

    /**
     * Paging plugin that automatically identifies database types
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
