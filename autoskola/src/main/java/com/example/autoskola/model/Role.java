package com.example.autoskola.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="roles")
@Getter @Setter
public class Role implements GrantedAuthority {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;


    public Role(String name) {
        this.name = name;
    }

    public Role() {

    }


    @Override
    @JsonIgnore
    public String getAuthority() {
        return name;
    }
}
