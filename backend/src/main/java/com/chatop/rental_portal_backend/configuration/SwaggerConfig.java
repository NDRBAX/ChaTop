package com.chatop.rental_portal_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI rentalPortalOpenAPI() {
        log.info("### SWAGGER CONFIGURATION ###");
        return new OpenAPI()
                .info(new Info().title("Rental Portal API")
                        .description(
                                "Documentation for Rental Portal API to mangage rentals properties and users in Chatop company")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

}
