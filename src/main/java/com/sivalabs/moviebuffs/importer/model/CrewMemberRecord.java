package com.sivalabs.moviebuffs.importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sivalabs.moviebuffs.entity.Movie;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
public class CrewMemberRecord implements Serializable
{

    @JsonProperty("credit_id")
    private String creditId;

    private String department;

    private String gender;

    private String id;

    private String job;

    private String name;

    @JsonProperty("profile_path")
    private String profilePath;

}
