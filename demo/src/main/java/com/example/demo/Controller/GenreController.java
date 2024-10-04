package com.example.demo.Controller;
import com.example.demo.Entity.*;
import com.example.demo.Service.AuthorService;
import com.example.demo.Service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
@CrossOrigin

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping()
    public ArrayList<Genre> getGenres(){
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") Long id){
        return genreService.getGenreById(id);
    }

    @PostMapping("")
    public Genre addGenre(@RequestBody Genre genre){
        return genreService.addGenre(genre);
    }

    @PutMapping("/{id}")
    public Genre updateGenre(@RequestBody Genre genre, @PathVariable("id") Long id) {
        return genreService.updateGenre(id, genre);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable("id") Long id) {
        genreService.deleteGenre(id);
    }
}
