package com.typeqast.meter.config;

import com.typeqast.meter.controller.converter.CsvHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public CsvHttpMessageConverter csvHttpMessageConverter() {
        return new CsvHttpMessageConverter();
    }
}
