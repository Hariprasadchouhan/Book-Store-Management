package com.example.demo.Controller;

import com.example.demo.Entity.Author;
import com.example.demo.Entity.Book;
import com.example.demo.Entity.Genre;
import com.example.demo.Service.BookService;

import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/books")
@CrossOrigin
class BookController {
    @Autowired
    public BookService bookService;

    @GetMapping()
    public ArrayList<Book> getBooks() {
        return bookService.getBooks();
    }
    @CrossOrigin
    @PostMapping
    public Book addBook(@RequestBody Book book){
        return bookService.addBook(book);
    }
    @GetMapping("/{bookId}")
    public Book getBookById(@PathVariable("bookId") Long bookId) {
        return bookService.getBookById(bookId);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
    }

    @GetMapping("/{bookId}/authors")
    public List<Author> getBookAuthors(@PathVariable("bookId") Long bookId) {
        return bookService.getBookAuthors(bookId);
    }
    @GetMapping("/{bookId}/genres")
    public List<Genre> getBookGenres(@PathVariable("bookId") Long bookId) {
        return bookService.getBookGenres(bookId);
    }
}