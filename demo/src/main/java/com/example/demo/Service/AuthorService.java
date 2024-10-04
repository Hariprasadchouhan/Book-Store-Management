package com.example.demo.Service;


import com.example.demo.Entity.Author;
import com.example.demo.Repository.AuthorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public ArrayList<Author> getAuthors() {
        List<Author> authorList = authorRepository.findAll();
        ArrayList<Author> authors = new ArrayList<>(authorList);
        return authors;
    }

    public Author getAuthorById(Long authorId) {
        try {
            Author author = authorRepository.findById(authorId).get();
            return author;
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Author addAuthor(Author author) {
        authorRepository.save(author);
        return author;
    }


    public Author updateAuthor(Long authorId, Author author) {
        try{
            Author new_author = authorRepository.findById(authorId).get();
            if(author.getAuthorName()!=null)new_author.setAuthorName(author.getAuthorName());
            authorRepository.save(new_author);
            return new_author;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteAuthor(Long authorId) {
        try {
            authorRepository.deleteById(authorId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
