package com.example.carpooling.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Carpooling System API")
                        .description("API documentation for the Carpooling backend system.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jass (Meda Jaswanth)")
                                .email("jaswanthm811@gmail.com")
                                .url("https://github.com/CoDe-WiZaDd-18-DOTCOM")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                ))
                .addTagsItem(new Tag().name("Authentication apis").description("Signup and login using JWT"))
                .addTagsItem(new Tag().name("Users").description("Manage user profile, preferences and picture"))
                .addTagsItem(new Tag().name("Rides").description("Ride creation, listing, and search"))
                .addTagsItem(new Tag().name("booking apis").description("Book, view, or approve ride requests"))
                .addTagsItem(new Tag().name("SOS").description("Emergency alerts, live location sharing"));
    }
}
