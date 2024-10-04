package com.example.demo.Service;


import com.example.demo.Entity.Genre;
import com.example.demo.Repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public ArrayList<Genre> getGenres() {
        List<Genre> genreList = genreRepository.findAll();
        ArrayList<Genre> genres = new ArrayList<>(genreList);
        return genres;
    }

    public Genre getGenreById(Long authorId) {
        try {
            Genre genre = genreRepository.findById(authorId).get();
            return genre;
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Genre addGenre(Genre genre) {
        genreRepository.save(genre);
        return genre;
    }


    public Genre updateGenre(Long genreId, Genre genre) {
        try{
            Genre new_genre = genreRepository.findById(genreId).get();
            if(genre.getGenreName()!=null)new_genre.setGenreName(genre.getGenreName());
            genreRepository.save(new_genre);
            return new_genre;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteGenre(Long genreId) {
        try {
            genreRepository.deleteById(genreId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
