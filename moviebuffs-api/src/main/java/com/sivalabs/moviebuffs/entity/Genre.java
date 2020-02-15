package com.sivalabs.moviebuffs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "genres")
@Setter
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
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
    private Set<Movie> movies;
}
