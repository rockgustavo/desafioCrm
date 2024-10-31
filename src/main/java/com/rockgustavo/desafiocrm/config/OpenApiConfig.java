package com.rockgustavo.desafiocrm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Desafio CRM - Viasoft")
                        .version("1.0")
                        .description("Desafio que apresenta a tecnologia back-end com Spring Boot"));
    }
}