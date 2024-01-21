package com.school.registrationsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI myOpenAPI() {

        Contact contact = new Contact();
        contact.setEmail("jakub.woinski@icloud.com");
        contact.setName("Jakub");
        contact.setUrl("https://github.com/JWoinski");

        License mitLicense = new License().name("My own licence").url("licence url");

        Info info = new Info()
                .title("School registration system API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage students and courses.").termsOfService("https://www.schoolregistrationsystem.com/terms")
                .license(mitLicense);

        return new OpenAPI().info(info);
    }
}
