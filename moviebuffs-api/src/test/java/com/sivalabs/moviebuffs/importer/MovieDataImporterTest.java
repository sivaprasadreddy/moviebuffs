package com.sivalabs.moviebuffs.importer;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.moviebuffs.config.ApplicationProperties;
import com.sivalabs.moviebuffs.entity.Genre;
import com.sivalabs.moviebuffs.importer.mappers.CsvRowMapperUtils;
import com.sivalabs.moviebuffs.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class MovieDataImporterTest {

    private CsvRowMapperUtils csvRowMapperUtils;
    private ApplicationProperties properties;
    private MovieService movieService;
    private MovieDataImporter movieDataImporter;

    @BeforeEach
    void setUp() {
        csvRowMapperUtils = new CsvRowMapperUtils();
        properties = new ApplicationProperties();
        properties.setImportTmdbData(true);
        properties.setMoviesDataFiles(singletonList("/data/movies_metadata-test.csv"));
        properties.setMovieCreditsFiles(singletonList("/data/credits-test.csv"));

        movieService = mock(MovieService.class);

        given(movieService.saveGenre(any(Genre.class))).willAnswer(answer -> answer.getArgument(0));

        movieDataImporter = new MovieDataImporter(movieService, csvRowMapperUtils, properties);
    }

    @Test
    void should_import_movie_data_successfully() throws IOException, CsvValidationException {
        movieDataImporter.importData();
        assertTrue(true);
    }
}
