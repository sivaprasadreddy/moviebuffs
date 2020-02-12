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
import com.sivalabs.moviebuffs.entity.Movie;
import com.sivalabs.moviebuffs.importer.mappers.CsvRowMapperUtils;
import com.sivalabs.moviebuffs.importer.model.CastMemberRecord;
import com.sivalabs.moviebuffs.importer.model.CreditsCsvRecord;
import com.sivalabs.moviebuffs.importer.model.CrewMemberRecord;
import com.sivalabs.moviebuffs.importer.model.MovieCsvRecord;
import com.sivalabs.moviebuffs.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    public void importDataAsync() throws IOException, CsvValidationException {
        importDataInternal();
    }

    public void importData() throws IOException, CsvValidationException {
        importDataInternal();
    }

    public void importDataInternal() throws IOException, CsvValidationException {
        importMoviesMetaData();
        importCreditsData();
    }

    private void importMoviesMetaData() throws IOException, CsvValidationException {
        log.info("Initializing movies database from files: {}", properties.getMoviesDataFiles());
        long start = System.currentTimeMillis();
        for (String dataFile : properties.getMoviesDataFiles()) {
            importMoviesMetaDataFile(dataFile);
        }
        long end = System.currentTimeMillis();
        log.debug("Time took for importing movie metadata : {} seconds", (end-start)/1000);
    }

    private void importMoviesMetaDataFile(String fileName) throws IOException, CsvValidationException {
        log.info("Importing movies from file: {}", fileName);
        CSVIterator iterator = getCsvIteratorFromClassPathResource(fileName);

        long count = 0L;
        List<Movie> moviesBatch = new ArrayList<>();
         while(iterator.hasNext()){
            String[] nextLine = iterator.next();
            MovieCsvRecord record = parseMovieRecord(nextLine);
            Movie movie = csvRowMapperUtils.mapToMovieEntity(record);
             moviesBatch.add(movie);
             if(moviesBatch.size() >= 100) {
                 movieService.createMovies(moviesBatch);
                 moviesBatch = new ArrayList<>();
             }
            count++;
        }
        if(moviesBatch.size() > 0) {
            movieService.createMovies(moviesBatch);
            count += moviesBatch.size();
        }

        log.info("Imported movies with {} records from file {}", count, fileName);
    }

    private void importCreditsData() throws IOException, CsvValidationException {
        log.info("Importing movies credits from files: {}", properties.getMovieCreditsFiles());
        for (String dataFile : properties.getMovieCreditsFiles()) {
            importCreditsDataFile(dataFile);
        }
    }

    private void importCreditsDataFile(String fileName) throws IOException, CsvValidationException {
        log.info("Importing movies credits from file: {}", fileName);
        CSVIterator iterator = getCsvIteratorFromClassPathResource(fileName);

        long count = 0L;
        while(iterator.hasNext()){
            String[] nextLine = iterator.next();
            CreditsCsvRecord record = parseCreditsRecord(nextLine);
            Movie movie = movieService.findMovieById(Long.valueOf(record.getId())).orElse(null);
            List<CastMemberRecord> castMemberRecords = getCastMembers(record.getCast());
            List<CastMember> castMembersBatch = new ArrayList<>();
            for (CastMemberRecord castMemberRecord : castMemberRecords) {
                CastMember castMember = csvRowMapperUtils.mapToCastMemberEntity(castMemberRecord);
                castMember.setMovie(movie);
                castMembersBatch.add(castMember);
                if(castMembersBatch.size() >= 100) {
                    movieService.saveAllCastMembers(castMembersBatch);
                    castMembersBatch = new ArrayList<>();
                }
            }
            if(castMembersBatch.size() > 0) {
                movieService.saveAllCastMembers(castMembersBatch);
            }

            List<CrewMemberRecord> crewMemberRecords = getCrewMembers(record.getCrew());
            List<CrewMember> crewMembersBatch = new ArrayList<>();
            for (CrewMemberRecord crewMemberRecord : crewMemberRecords) {
                CrewMember crewMember = csvRowMapperUtils.mapToCrewMemberEntity(crewMemberRecord);
                crewMember.setMovie(movie);
                crewMembersBatch.add(crewMember);
                if(crewMembersBatch.size() >= 100) {
                    movieService.saveAllCrewMembers(crewMembersBatch);
                    crewMembersBatch = new ArrayList<>();
                }
            }
            if(crewMembersBatch.size() > 0) {
                movieService.saveAllCrewMembers(crewMembersBatch);
            }
            count++;
        }
        log.info("Initialized movies credits with {} records", count);
    }

    private CSVIterator getCsvIteratorFromClassPathResource(String fileName) throws IOException, CsvValidationException {
        ClassPathResource file = new ClassPathResource(fileName, this.getClass());
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReader(inputStreamReader);
        csvReader.skip(1);
        return new CSVIterator(csvReader);
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




