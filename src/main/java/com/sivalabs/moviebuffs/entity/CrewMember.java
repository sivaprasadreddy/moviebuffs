package com.sivalabs.moviebuffs.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="movie_crew")
@Setter
@Getter
public class CrewMember implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "crew_id_generator", sequenceName = "crew_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "crew_id_generator")
    private Long id;

    @JsonProperty("credit_id")
    @Column(name = "credit_id")
    private String creditId;

    private String department;

    private String gender;

    private String uid;

    private String job;

    private String name;

    @JsonProperty("profile_path")
    @Column(name = "profile_path")
    private String profilePath;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
