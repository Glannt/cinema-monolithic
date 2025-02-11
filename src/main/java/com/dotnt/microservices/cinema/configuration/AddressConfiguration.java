package com.dotnt.microservices.cinema.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AddressConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

