package com.sivalabs.moviebuffs.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {
    private String moviesDataFile;
    private String movieCreditsFile;
}
