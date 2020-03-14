package com.sivalabs.moviebuffs.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Setter
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class User extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_id_generator")
    private Long id;

    @Column(nullable=false)
    @NotEmpty()
    private String name;

    @Column(nullable=false, unique=true)
    @NotEmpty
    @Email(message="Invalid email")
    private String email;

    @Column(nullable=false)
    @NotEmpty
    @Size(min=4)
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinTable(
            name="user_role",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    private List<Role> roles = new ArrayList<>();

}
