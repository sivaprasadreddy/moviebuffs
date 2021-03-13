package com.sivalabs.moviebuffs.config;

import com.sivalabs.moviebuffs.importer.DataImportProperties;
import com.sivalabs.moviebuffs.importer.JsonMovieDataImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final JsonMovieDataImporter movieDataImporter;

    private final DataImportProperties dataImportProperties;

    @Override
    public void run(String... args) throws Exception {
        if (dataImportProperties.isDisabled()) {
            log.info("TMDB data initialization is disabled");
            return;
        }
        if (dataImportProperties.isAsync()) {
            log.info("Initializing TMDB data in async mode");
            movieDataImporter.importDataAsync();
        } else {
            log.info("Initializing TMDB data in sync mode");
            movieDataImporter.importData();
        }
        log.debug("TMDB data initialized successfully");
    }
}
