package edu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${inner.auth.url:http://localhost:8082/innerprosses}")
    private String scrapperBaseUrl;

    @Bean
    public WebClient scrapperWebClient() {
        return WebClient.builder()
                .baseUrl(scrapperBaseUrl)
                .build();
    }
}