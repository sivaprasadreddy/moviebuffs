package com.sivalabs.moviebuffs.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {
    private boolean importTmdbData;
    private boolean importTmdbDataAsync;
    private List<String> moviesDataFiles;
    private List<String> movieCreditsFiles;
    private static final Long DEFAULT_JWT_TOKEN_EXPIRES = 604800L;

    private JwtConfig jwt = new JwtConfig();

    @Data
    public static class JwtConfig {
        private String issuer = "todo";
        private String header = "Authorization";
        private Long expiresIn = DEFAULT_JWT_TOKEN_EXPIRES;
        private String secret = "";
    }
}
