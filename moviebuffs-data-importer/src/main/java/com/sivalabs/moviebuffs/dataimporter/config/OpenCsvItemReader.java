package com.sivalabs.moviebuffs.dataimporter.config;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;

public class OpenCsvItemReader implements ItemReader<MovieCsvRecord> {
    private final Resource inputResource;
    private CSVIterator csvIterator;

    public OpenCsvItemReader(Resource inputResource, int skipLines) throws IOException, CsvValidationException {
        this.inputResource = inputResource;
        InputStreamReader inputStreamReader = new InputStreamReader(inputResource.getInputStream());
        CSVReader csvReader = new CSVReader(inputStreamReader);
        csvReader.skip(skipLines);
        this.csvIterator = new CSVIterator(csvReader);
    }

    @Override
    public MovieCsvRecord read() {
        if(csvIterator.hasNext()) {
            String[] nextLine = csvIterator.next();
            return parseMovieRecord(nextLine);
        }
        return null;
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
}
