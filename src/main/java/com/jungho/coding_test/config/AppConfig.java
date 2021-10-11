package com.jungho.coding_test.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    /** ModelMapper 빈 등록 */
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
