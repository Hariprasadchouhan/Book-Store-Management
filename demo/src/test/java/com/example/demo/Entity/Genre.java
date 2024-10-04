package com.example.demo.Entity;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "genre")
@Data
public class Genre {
    @Id
    @Column(name = "genreid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int genreId;

    @Column(name = "genrename")
    private String genreName;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private List<Book> books = new ArrayList<>();

    // Constructor with genreName
    public Genre(String genreName) {
        this.genreName = genreName;
    }

    // Default constructor
    public Genre() {
    }
}