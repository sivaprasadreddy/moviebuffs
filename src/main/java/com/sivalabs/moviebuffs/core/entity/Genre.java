package com.sivalabs.moviebuffs.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "genres")
@Setter
@Getter
@EqualsAndHashCode(
        of = {"id"},
        callSuper = false)
public class Genre implements Serializable {

    @Id
    @SequenceGenerator(
            name = "genre_id_generator",
            sequenceName = "genre_id_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "genre_id_generator")
    private Long id;

    private Long tmdbId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;
}
