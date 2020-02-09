package com.sivalabs.moviebuffs.importer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.moviebuffs.config.ApplicationProperties;
import com.sivalabs.moviebuffs.entity.CastMember;
import com.sivalabs.moviebuffs.entity.CrewMember;
import com.sivalabs.moviebuffs.importer.mappers.CsvRowMapperUtils;
import com.sivalabs.moviebuffs.importer.model.CastMemberRecord;
import com.sivalabs.moviebuffs.importer.model.CreditsCsvRecord;
import com.sivalabs.moviebuffs.importer.model.CrewMemberRecord;
import com.sivalabs.moviebuffs.importer.model.MovieCsvRecord;
import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class MovieDataImporter {

    private final ApplicationProperties properties;
    private final MovieService movieService;
    private final CsvRowMapperUtils csvRowMapperUtils;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public MovieDataImporter(ApplicationProperties properties,
                             MovieService movieService,
                             CsvRowMapperUtils csvRowMapperUtils) {
        this.properties = properties;
        this.movieService = movieService;
        this.csvRowMapperUtils = csvRowMapperUtils;

        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    @Async
    public void importData() throws IOException, CsvValidationException {
        importMoviesMetaData();
        //importCreditsData();
    }

    private void importMoviesMetaData() throws IOException, CsvValidationException {
        log.info("Initializing movies database from file: {}", properties.getMoviesDataFile());
        ClassPathResource file = new ClassPathResource(properties.getMoviesDataFile(), this.getClass());
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(inputStreamReader);
        csvReader.skip(1);
        CSVIterator iterator = new CSVIterator(csvReader);

        long count = 0L;
        while(iterator.hasNext()){
            String[] nextLine = iterator.next();
            MovieCsvRecord record = parseMovieRecord(nextLine);
            Movie movie = csvRowMapperUtils.mapToMovieEntity(record);
            movieService.createMovie(movie);
            count++;
        }
        log.info("Initialized movies database with {} records", count);
    }

    private void importCreditsData() throws IOException, CsvValidationException {
        log.info("Initializing movies credits from file: {}", properties.getMovieCreditsFile());
        File file = new ClassPathResource(properties.getMovieCreditsFile(), this.getClass()).getFile();
        CSVReader csvReader = new CSVReader(new FileReader(file));
        csvReader.skip(1);
        CSVIterator iterator = new CSVIterator(csvReader);

        long count = 0L;
        while(iterator.hasNext()){
            String[] nextLine = iterator.next();
            CreditsCsvRecord record = parseCreditsRecord(nextLine);
            Movie movie = movieService.findMovieById(Long.valueOf(record.getId())).orElse(null);
            List<CastMemberRecord> castMemberRecords = getCastMembers(record.getCast());
            for (CastMemberRecord castMemberRecord : castMemberRecords) {
                CastMember castMember = csvRowMapperUtils.mapToCastMemberEntity(castMemberRecord);
                castMember.setMovie(movie);
                movieService.save(castMember);
            }

            List<CrewMemberRecord> crewMemberRecords = getCrewMembers(record.getCrew());
            for (CrewMemberRecord crewMemberRecord : crewMemberRecords) {
                CrewMember crewMember = csvRowMapperUtils.mapToCrewMemberEntity(crewMemberRecord);
                crewMember.setMovie(movie);
                movieService.save(crewMember);
            }
            count++;
        }
        log.info("Initialized movies credits with {} records", count);
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

    private List<CastMemberRecord> getCastMembers(String castArrayJson) throws JsonProcessingException {
        CastMemberRecord[] castMembers = objectMapper.readValue(castArrayJson, CastMemberRecord[].class);
        return Arrays.asList(castMembers);
    }

    private List<CrewMemberRecord> getCrewMembers(String crewArrayJson) throws JsonProcessingException {
        CrewMemberRecord[] crewMembers = objectMapper.readValue(crewArrayJson, CrewMemberRecord[].class);
        return Arrays.asList(crewMembers);
    }

    private CreditsCsvRecord parseCreditsRecord(String[] nextLine) {
        return CreditsCsvRecord.builder()
                .cast(nextLine[0])
                .crew(nextLine[1])
                .id(nextLine[2])
                .build();
    }

}




