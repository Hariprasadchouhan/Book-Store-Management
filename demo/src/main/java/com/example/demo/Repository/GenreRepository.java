package com.example.demo.Repository;


import com.example.demo.Entity.Author;
import com.example.demo.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByGenreName(String genreName); // Change to match 'genreName' property
}
