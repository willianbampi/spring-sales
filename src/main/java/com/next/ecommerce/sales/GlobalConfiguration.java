package com.next.ecommerce.sales;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//@DevelopmentProfile
@Configuration
@Profile("development")
public class GlobalConfiguration {
    
    @Bean(name = "applicationMessage")
    public String applicationMessage() {
        return "Welcome";
    }
}
