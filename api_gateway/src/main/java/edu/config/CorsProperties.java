package edu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    private boolean enabled = true;
    private List<String> paths;
    private List<String> origins;
    private List<String> methods;
    private List<String> headers;
    private boolean allowCredentials;
}
