package com.sivalabs.moviebuffs.config;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.moviebuffs.service.MovieDataImporter;
import com.sivalabs.moviebuffs.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Profile({Constants.PROFILE_NOT_PROD, Constants.PROFILE_NOT_HEROKU})
public class DataInitializer implements CommandLineRunner {

    private final MovieDataImporter movieDataImporter;

    @Autowired
    public DataInitializer(MovieDataImporter movieDataImporter) {
        this.movieDataImporter = movieDataImporter;
    }

    @Override
    public void run(String... args) throws IOException, CsvValidationException {
        movieDataImporter.importData();
        log.debug("Data initialized");
    }
}
