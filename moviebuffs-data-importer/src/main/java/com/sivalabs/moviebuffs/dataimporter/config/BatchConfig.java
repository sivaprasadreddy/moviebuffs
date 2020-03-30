package com.sivalabs.moviebuffs.dataimporter.config;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classpath:/data/movies_metadata_small.csv")
    private Resource inputResource;

    @Bean
    public Job importMovieDataJob() throws IOException, CsvValidationException {
        return jobBuilderFactory
                .get("importMovieDataJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }

    @Bean
    public Step step() throws IOException, CsvValidationException {
        return stepBuilderFactory
                .get("step")
                .<MovieCsvRecord, MovieCsvRecord>chunk(5)
                .reader(reader())
                //.processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemReader<MovieCsvRecord> reader() throws IOException, CsvValidationException {
        return new OpenCsvItemReader(inputResource, 1);
    }

    @Bean
    public ItemWriter<MovieCsvRecord> writer() {
        return items -> {
            for (MovieCsvRecord record : items) {
                System.out.println(record);
            }
        };
    }
}
