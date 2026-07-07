package com.kumar.ecomapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Contact contact = new Contact()
                .name("Praveen Kumar")
                .email("praveen@gmail.com")
                .url("https://github.com/praveenkumar");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info()
                .title("E-Commerce REST API")
                .version("1.0.0")
                .description("""
                        REST APIs for E-Commerce Application.
                        
                        Features:
                        - User Management
                        - Product Management
                        - Category Management
                        - Cart Management
                        - Order Management
                        - Payment Management
                        """)
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info);
    }
}