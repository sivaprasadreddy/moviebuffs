package com.sivalabs.moviebuffs.config;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.moviebuffs.importer.MovieDataImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@ConditionalOnProperty(name = "application.import-tmdb-data-async", havingValue = "true")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MovieDataImporter movieDataImporter;
    private final ApplicationProperties applicationProperties;

    @Override
    public void run(String... args) throws IOException, CsvValidationException {
        if(applicationProperties.isImportTmdbDataAsync()) {
            log.info("Initializing TMDB data in async mode");
            movieDataImporter.importDataAsync();
        } else {
            log.info("Initializing TMDB data in sync mode");
            movieDataImporter.importData();
        }
        log.debug("TMDB data initialized successfully");
    }
}
