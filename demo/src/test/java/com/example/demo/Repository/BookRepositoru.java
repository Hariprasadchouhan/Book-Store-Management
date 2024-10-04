package com.example.demo.Repository;

import com.example.demo.Entity.Author;
import com.example.demo.Entity.Book;
import com.example.demo.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepositoru extends JpaRepository<Book,Long> {
    Book findByTitleAndAuthorsAndGenres(String title, List<Author> authors, List<Genre> genres);
    Book findByTitleAndAuthorsInAndGenresIn(String title, List<Author> authors, List<Genre> genres);


}
