package com.dailyAppTraining.eCommrce.configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My Spring Boot API")
                        .version("1.0")
                        .description("This is a sample Spring Boot API documented with Swagger")
                        .contact(new Contact()
                                .name("Elsayed Zahran")
                                .email("elsayedzahran@example.com")));
    }
}
