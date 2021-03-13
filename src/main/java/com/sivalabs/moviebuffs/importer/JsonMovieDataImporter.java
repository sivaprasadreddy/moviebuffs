package com.sivalabs.moviebuffs.importer;

import static com.sivalabs.moviebuffs.core.utils.TimeUtils.millisToLongDHMS;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.moviebuffs.core.entity.Genre;
import com.sivalabs.moviebuffs.core.entity.Movie;
import com.sivalabs.moviebuffs.core.service.MovieService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonMovieDataImporter {

    private final MovieService movieService;

    private final MovieRowMapperUtils movieRowMapperUtils;

    private final DataImportProperties dataImportProperties;

    @Async
    public void importDataAsync() throws IOException {
        importDataInternal();
    }

    public void importData() throws IOException {
        importDataInternal();
    }

    private void importDataInternal() throws IOException {
        deleteExistingMovieData();
        importMoviesMetaData();
    }

    private void deleteExistingMovieData() {
        movieService.cleanupMovieData();
    }

    private void importMoviesMetaData() throws IOException {
        log.info(
                "Initializing movies database from files: {}",
                dataImportProperties.getMoviesDataFiles());
        long start = System.currentTimeMillis();
        long recordCount = 0;
        for (String dataFile : dataImportProperties.getMoviesDataFiles()) {
            recordCount += importMoviesFromJsonFile(dataFile, recordCount);
        }
        long end = System.currentTimeMillis();
        log.debug("Time took for importing movie metadata : {} ", millisToLongDHMS(end - start));
    }

    private long importMoviesFromJsonFile(String fileName, long recordCount) throws IOException {
        log.info("Importing movies from file: {}", fileName);
        ClassPathResource file = new ClassPathResource(fileName, this.getClass());
        List<String> lines = IOUtils.readLines(file.getInputStream(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper =
                new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Map<String, Genre> genresMap =
                movieService.findAllGenres(Sort.by("name")).stream()
                        .collect(Collectors.toMap(Genre::getName, g -> g));
        List<Movie> moviesBatch = new ArrayList<>();

        log.info("Line count:{}", lines.size());
        for (String line : lines) {
            MovieJsonRecord movieJsonRecord = objectMapper.readValue(line, MovieJsonRecord.class);
            Movie movie = movieRowMapperUtils.mapToMovieEntity(movieJsonRecord);
            movie.setGenres(saveGenres(genresMap, movie.getGenres()));
            moviesBatch.add(movie);
            recordCount++;
            if (dataImportProperties.getMaxSize() > 0
                    && recordCount >= dataImportProperties.getMaxSize()) {
                break;
            }
            if (moviesBatch.size() >= dataImportProperties.getBatchSize()) {
                movieService.createMovies(moviesBatch);
                log.debug("Imported {} movies so far", recordCount);
                moviesBatch = new ArrayList<>();
            }
        }

        if (!moviesBatch.isEmpty()) {
            movieService.createMovies(moviesBatch);
            log.debug("Imported {} movies so far", recordCount);
        }
        log.info("Imported movies with {} records from file {}", recordCount, fileName);
        return recordCount;
    }

    private Set<Genre> saveGenres(Map<String, Genre> existingGenres, Set<Genre> genres) {
        Set<Genre> genreList = new HashSet<>();
        for (Genre genre : genres) {
            Genre existingGenre = existingGenres.get(genre.getName());
            if (existingGenre != null) {
                genreList.add(existingGenre);
            } else {
                Genre savedGenre = movieService.saveGenre(genre);
                genreList.add(savedGenre);
                existingGenres.put(savedGenre.getName(), savedGenre);
            }
        }
        return genreList;
    }
}
