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
import com.sivalabs.moviebuffs.entity.Genre;
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
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.sivalabs.moviebuffs.utils.TimeUtils.millisToLongDHMS;

@Slf4j
@Component
public class MovieDataImporter {

    private final MovieService movieService;
    private final CsvRowMapperUtils csvRowMapperUtils;
    private final ApplicationProperties properties;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public MovieDataImporter(MovieService movieService,
                             CsvRowMapperUtils csvRowMapperUtils,
                             ApplicationProperties properties) {
        this.movieService = movieService;
        this.csvRowMapperUtils = csvRowMapperUtils;
        this.properties = properties;

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

    private void importDataInternal() throws IOException, CsvValidationException {
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
        log.debug("Time took for importing movie metadata : {} ", millisToLongDHMS(end - start));
    }

    private void importMoviesMetaDataFile(String fileName) throws IOException, CsvValidationException {
        log.info("Importing movies from file: {}", fileName);
        CSVIterator iterator = getCsvIteratorFromClassPathResource(fileName);
        Map<String, Genre> genresMap = movieService.findAllGenres(Sort.by("name"))
                .stream().collect(Collectors.toMap(Genre::getName, g -> g));
        long count = 0L;
        List<Movie> moviesBatch = new ArrayList<>();
        while (iterator.hasNext()) {
            String[] nextLine = iterator.next();
            MovieCsvRecord record = parseMovieRecord(nextLine);
            Movie movie = csvRowMapperUtils.mapToMovieEntity(record);
            movie.setGenres(saveGenres(genresMap, movie.getGenres()));
            moviesBatch.add(movie);
            count++;
            if (moviesBatch.size() >= properties.getImportTmdbDataBatchSize()) {
                movieService.createMovies(moviesBatch);
                log.trace("Imported {} movies so far", count);
                moviesBatch = new ArrayList<>();
            }
        }
        if (!moviesBatch.isEmpty()) {
            movieService.createMovies(moviesBatch);
            count += moviesBatch.size();
        }
        log.info("Imported movies with {} records from file {}", count, fileName);
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

    private void importCreditsData() throws IOException, CsvValidationException {
        log.info("Importing movies credits from files: {}", properties.getMovieCreditsFiles());
        long start = System.currentTimeMillis();
        for (String dataFile : properties.getMovieCreditsFiles()) {
            importCreditsDataFile(dataFile);
        }
        long end = System.currentTimeMillis();
        log.debug("Time took for importing movie credits data : {}", millisToLongDHMS(end - start));
    }

    private void importCreditsDataFile(String fileName) throws IOException, CsvValidationException {
        log.info("Importing movies credits from file: {}", fileName);
        CSVIterator iterator = getCsvIteratorFromClassPathResource(fileName);

        long count=0L;
        long castCount = 0L;
        long crewCount = 0L;
        while (iterator.hasNext()) {
            String[] nextLine = iterator.next();
            CreditsCsvRecord record = parseCreditsRecord(nextLine);
            Movie movie = movieService.findByTmdbId(record.getId()).orElse(null);
            if(movie == null) {
                log.warn("Got a movie credit record with movie_id: {}, which doesn't exist", record.getId());
                continue;
            }
            List<CastMemberRecord> castMemberRecords = getCastMembers(record.getCast());
            List<CastMember> castMembers = new ArrayList<>();
            for (CastMemberRecord castMemberRecord : castMemberRecords) {
                CastMember castMember = csvRowMapperUtils.mapToCastMemberEntity(castMemberRecord);
                castMember.setMovie(movie);
                castMembers.add(castMember);
                castCount++;
            }
            if (!castMembers.isEmpty()) {
                movieService.saveAllCastMembers(castMembers);
            }
            log.trace("Imported {} movie cast records so far", castCount);

            List<CrewMemberRecord> crewMemberRecords = getCrewMembers(record.getCrew());
            List<CrewMember> crewMembers = new ArrayList<>();
            for (CrewMemberRecord crewMemberRecord : crewMemberRecords) {
                CrewMember crewMember = csvRowMapperUtils.mapToCrewMemberEntity(crewMemberRecord);
                crewMember.setMovie(movie);
                crewMembers.add(crewMember);
                crewCount++;
            }
            if (!crewMembers.isEmpty()) {
                movieService.saveAllCrewMembers(crewMembers);
            }
            log.trace("Imported {} movie crew records so far", crewCount);
            count++;
            log.trace("Imported {} movie credits records so far", count);
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
                .belongsToCollection(nextLine[1])
                .budget(nextLine[2])
                .genres(nextLine[3])
                .homepage(nextLine[4])
                .id(nextLine[5])
                .imdbId(nextLine[6])
                .originalLanguage(nextLine[7])
                .originalTitle(nextLine[8])
                .overview(nextLine[9])
                .popularity(nextLine[10])
                .posterPath(nextLine[11])
                .productionCompanies(nextLine[12])
                .productionCountries(nextLine[13])
                .releaseDate(nextLine[14])
                .revenue(nextLine[15])
                .runtime(nextLine[16])
                .spokenLanguages(nextLine[17])
                .status(nextLine[18])
                .tagline(nextLine[19])
                .title(nextLine[20])
                .video(nextLine[21])
                .voteAverage(nextLine[22])
                .voteCount(nextLine[23])
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




