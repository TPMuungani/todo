package com.tmuungani.todo.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI myOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.components(new Components().addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));

        Contact contact = new Contact();
        contact.setEmail("tafadzwamuungani18@gmail.com");
        contact.setName("Tafadzwa Muungani the developer");
        contact.setUrl("https://www.linkedin.com/in/tpmuungani/");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");
        Info info = new Info()
                .title("TODO (WORK TASK MANAGER)")
                .version("1.0")
                .contact(contact)
                .description("WORK TASK MANAGER").termsOfService("https://www.linkedin.com/in/tpmuungani/")
                .license(mitLicense);
        return openAPI.info(info);
    }
}
