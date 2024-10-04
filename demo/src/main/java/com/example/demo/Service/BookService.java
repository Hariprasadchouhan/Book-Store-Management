package com.example.demo.Service;

import com.example.demo.Entity.Book;
import com.example.demo.Entity.Author;

import com.example.demo.Entity.Genre;
import com.example.demo.Repository.BookRepositoru;
import com.example.demo.Repository.AuthorRepository;
import com.example.demo.Repository.GenreRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class BookService{

    @Autowired
    private BookRepositoru bookRepositoru;
    @Autowired
    private AuthorRepository  authorRepository;
    @Autowired
    private GenreRepository genreRepository;


    public ArrayList<Book> getBooks() {
        List<Book> bookList = bookRepositoru.findAll();
        ArrayList<Book> books = new ArrayList<>(bookList);
        return books;
    }


    public Book getBookById(Long bookId) {
        try {
            Book book = bookRepositoru.findById(bookId).get();
            return book;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public Book addBook(Book book) {
        // Handle authors: Check if they exist, otherwise save them
        List<Author> existingAuthors = new ArrayList<>();
        for (Author author : book.getAuthors()) {
            Author existingAuthor = authorRepository.findByAuthorName(author.getAuthorName());
            if (existingAuthor != null) {
                existingAuthors.add(existingAuthor);
            } else {
                existingAuthors.add(authorRepository.save(author)); // Save new author
            }
        }
        book.setAuthors(existingAuthors);  // Now authors are persisted

        // Handle genres: Check if they exist, otherwise save them
        List<Genre> existingGenres = new ArrayList<>();
        for (Genre genre : book.getGenres()) {
            Genre existingGenre = genreRepository.findByGenreName(genre.getGenreName());
            if (existingGenre != null) {
                existingGenres.add(existingGenre);
            } else {
                existingGenres.add(genreRepository.save(genre)); // Save new genre
            }
        }
        book.setGenres(existingGenres);  // Now genres are persisted

        // Check if a book with the same title, authors, and genres exists
        Book existingBook = bookRepositoru.findByTitleAndAuthorsInAndGenresIn(
                book.getTitle(), book.getAuthors(), book.getGenres());

        if (existingBook != null) {
            // If the book exists, update the stock by adding the new stock
            existingBook.setStock(existingBook.getStock() + book.getStock());
            return bookRepositoru.save(existingBook);
        }

        // Save and return the new book
        return bookRepositoru.save(book);
    }
    public Book updateBook(Long bookId, Book book) {

        try {
            Book newBook = bookRepositoru.findById(bookId).get();
            if (book.getTitle() != null) {
                newBook.setTitle(book.getTitle());
            }
            if (book.getAuthors() != null) {
                newBook.setAuthors(book.getAuthors());
            }
            bookRepositoru.save(newBook);
            return newBook;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteBook(Long bookId) {
        try {
            bookRepositoru.deleteById(bookId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    public List<Author> getBookAuthors(Long bookId) {
        try{
            Book book = bookRepositoru.findById(bookId).get();
            return book.getAuthors();
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public List<Genre> getBookGenres(Long bookId) {
        try{
            Book book = bookRepositoru.findById(bookId).get();
            return book.getGenres();
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}