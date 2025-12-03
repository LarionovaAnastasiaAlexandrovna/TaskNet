package edu.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsConfig {

    private final CorsProperties props;

    public CorsConfig(CorsProperties props) {
        this.props = props;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                if (!props.isEnabled()) {
                    return;
                }

                props.getPaths().forEach(path ->
                        registry.addMapping(path)
                                .allowedOrigins(props.getOrigins().toArray(new String[0]))
                                .allowedMethods(props.getMethods().toArray(new String[0]))
                                .allowedHeaders(props.getHeaders().toArray(new String[0]))
                                .allowCredentials(props.isAllowCredentials())
                );
            }
        };
    }
}

