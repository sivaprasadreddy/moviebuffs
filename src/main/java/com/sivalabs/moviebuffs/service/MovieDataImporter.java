package com.sivalabs.moviebuffs.service;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.moviebuffs.config.ApplicationProperties;
import com.sivalabs.moviebuffs.model.MovieCsvRecord;
import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.mappers.MovieCsvRecordToMovieEntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
@Transactional
public class MovieDataImporter {

    private final ApplicationProperties properties;
    private final MovieService movieService;
    private final MovieCsvRecordToMovieEntityMapper movieCsvRecordToMovieEntityMapper;

    @Autowired
    public MovieDataImporter(ApplicationProperties properties,
                             MovieService movieService,
                             MovieCsvRecordToMovieEntityMapper movieCsvRecordToMovieEntityMapper) {
        this.properties = properties;
        this.movieService = movieService;
        this.movieCsvRecordToMovieEntityMapper = movieCsvRecordToMovieEntityMapper;
    }

    @Async
    public void importData() throws IOException, CsvValidationException {
        log.info("Initializing movies database from file: {}", properties.getMoviesDataFile());
        File file = new ClassPathResource(properties.getMoviesDataFile(), this.getClass()).getFile();
        CSVReader csvReader = new CSVReader(new FileReader(file));
        csvReader.skip(1);
        CSVIterator iterator = new CSVIterator(csvReader);

        long count = 0L;
        while(iterator.hasNext()){
            String[] nextLine = iterator.next();
            MovieCsvRecord record = parseMovieRecord(nextLine);
            Movie movie = movieCsvRecordToMovieEntityMapper.map(record);
            movieService.createMovie(movie);
            count++;
        }
        log.info("Initialized movies database with {} records", count);
    }

    private MovieCsvRecord parseMovieRecord(String[] nextLine) {
        return MovieCsvRecord.builder()
                .adult(nextLine[0])
                .belongs_to_collection(nextLine[1])
                .budget(nextLine[2])
                .genres(nextLine[3])
                .homepage(nextLine[4])
                .id(nextLine[5])
                .imdb_id(nextLine[6])
                .original_language(nextLine[7])
                .original_title(nextLine[8])
                .overview(nextLine[9])
                .popularity(nextLine[10])
                .poster_path(nextLine[11])
                .production_companies(nextLine[12])
                .production_countries(nextLine[13])
                .release_date(nextLine[14])
                .revenue(nextLine[15])
                .runtime(nextLine[16])
                .spoken_languages(nextLine[17])
                .status(nextLine[18])
                .tagline(nextLine[19])
                .title(nextLine[20])
                .video(nextLine[21])
                .vote_average(nextLine[22])
                .vote_count(nextLine[23])
                .build();
    }
}




