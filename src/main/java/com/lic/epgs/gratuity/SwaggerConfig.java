package com.lic.epgs.gratuity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build();
    }
    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }
    private ApiInfo apiInfo() {
        return new ApiInfo("LIC Application - Gratuity Module", "APIs to provide LIC Application - Gratuity Module", "1.0", "Terms of service", new Contact("LIC Gratuity Module", "#", "vignesh.v_isl@fasoftware.com"), "	License of API", "API license URL", Collections.emptyList());
    }
}

/**
 * @EnableSwagger2 enables SpringFox support for Swagger 2.
 * DocumentationType.SWAGGER_2 tells the Docket bean that we are using version 2 of Swagger specification.
 * select() creates a builder, which is used to define which controllers and which of their methods should be included in the generated documentation.
 * apis() defines the classes (controller and model classes) to be included. Here we are including all of them, but you can limit them by a base package, class annotations and more.
 * paths() allow you to define which controller's methods should be included based on their path mappings. We are now including all of them but you can limit it using regex and more.
 *
 * http://localhost:8080/v2/api-docs - API Document Generated in JSON format.
 * http://localhost:8080/swagger-ui.html#/
 */
