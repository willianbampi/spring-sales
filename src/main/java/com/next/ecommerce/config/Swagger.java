package com.next.ecommerce.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                    .useDefaultResponseMessages(false)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.next.ecommerce.rest.controller"))
                    .paths(PathSelectors.any())
                    .build()
                    .securityContexts(Arrays.asList(securityContext()))
                    .securitySchemes(Arrays.asList(apiKey()))
                    .apiInfo(apiInfo());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                                .securityReferences(defaultAuth())
                                .forPaths(PathSelectors.any())
                                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizations = new AuthorizationScope[1];
        authorizations[0] = authorizationScope;
        SecurityReference securityReference = new SecurityReference("JWTD", authorizations);
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(securityReference);
        return securityReferences;
    }

    public ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                    .title("Sales API")
                    .description("Sales API Example Project in Spring")
                    .version("1.0")
                    .contact(contact())
                    .build();
    }

    private Contact contact() {
        return new Contact("Wilian Bampi", "https://google.com", "willian.bampi@gmail.com");
    }
    
}
