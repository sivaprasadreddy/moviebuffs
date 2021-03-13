package com.sivalabs.moviebuffs.importer;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.dataimport")
@Getter
@Setter
public class DataImportProperties {

    private boolean disabled;

    private int maxSize;

    private int batchSize;

    private boolean async;

    private List<String> moviesDataFiles;
}
