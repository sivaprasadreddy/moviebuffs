package com.sivalabs.moviebuffs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "genres")
@Data
public class Genre implements Serializable {
    @Id
    @SequenceGenerator(name = "genre_id_generator", sequenceName = "genre_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "genre_id_generator")
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String slug;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies;
}