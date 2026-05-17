package com.example.survey_uni.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        System.out.println("API Documents at: http://localhost:8080/surveyuni/swagger-ui/index.html");
        return new OpenAPI()
                .info(new Info()
                        .title("University Survey API")
                        .version("1.0"))
                // 1. Apply the security requirement globally to all endpoints
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 2. Define the security scheme type (JWT Bearer)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

}
