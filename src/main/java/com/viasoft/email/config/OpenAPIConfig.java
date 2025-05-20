package com.viasoft.email.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Email API")
                        .description("API capaz de enviar emails utilizando diferentes provedores, como OCI e AWS.")
                        .version("v1.0"));
    }
}
