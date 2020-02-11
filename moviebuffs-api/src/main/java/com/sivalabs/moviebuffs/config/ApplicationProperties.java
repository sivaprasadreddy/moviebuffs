package com.sivalabs.moviebuffs.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {
    private boolean importTmdbData;
    private boolean importTmdbDataAsync;
    private String moviesDataFile;
    private String movieCreditsFile;
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
