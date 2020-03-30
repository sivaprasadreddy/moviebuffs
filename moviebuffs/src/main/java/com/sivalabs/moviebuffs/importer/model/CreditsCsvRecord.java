package com.sivalabs.moviebuffs.importer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditsCsvRecord {

	private String cast;

	private String crew;

	private String id;

}
