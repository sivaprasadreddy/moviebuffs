package com.sivalabs.moviebuffs;

import com.sivalabs.moviebuffs.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
@EnableAsync
@EnableScheduling
public class MovieBuffsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieBuffsApplication.class, args);
	}

}
