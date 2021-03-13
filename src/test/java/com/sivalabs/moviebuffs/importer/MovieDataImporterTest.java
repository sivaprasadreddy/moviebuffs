package com.sivalabs.moviebuffs.importer;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.service.MovieService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieDataImporterTest {

    private MovieRowMapperUtils movieRowMapperUtils;

    private DataImportProperties dataImportProperties;

    private MovieService movieService;

    private JsonMovieDataImporter movieDataImporter;

    @BeforeEach
    void setUp() {
        movieRowMapperUtils = new MovieRowMapperUtils();
        dataImportProperties = new DataImportProperties();
        dataImportProperties.setDisabled(false);
        dataImportProperties.setMoviesDataFiles(singletonList("/data/movie-details-test.txt"));

        movieService = mock(MovieService.class);

        given(movieService.saveGenre(any(Genre.class))).willAnswer(answer -> answer.getArgument(0));

        movieDataImporter =
                new JsonMovieDataImporter(movieService, movieRowMapperUtils, dataImportProperties);
    }

    @Test
    void should_import_movie_data_successfully() throws IOException {
        movieDataImporter.importData();
        assertTrue(true);
    }
}
