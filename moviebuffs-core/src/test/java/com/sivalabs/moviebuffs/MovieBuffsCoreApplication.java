package com.sivalabs.moviebuffs;

import com.sivalabs.moviebuffs.importer.DataImportProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({DataImportProperties.class})
@EnableAsync
@EnableScheduling
public class MovieBuffsCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieBuffsCoreApplication.class, args);
	}

}
