package com.example.demo.Service;

import com.example.demo.Entity.Book;
import com.example.demo.Entity.Author;
import com.example.demo.Entity.Genre;
import com.example.demo.Repository.BookRepositoru;
import com.example.demo.Repository.AuthorRepository;
import com.example.demo.Repository.GenreRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepositoru bookRepositoru;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBooks() {
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepositoru.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getBooks();

        assertEquals(2, books.size());
        verify(bookRepositoru, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        when(bookRepositoru.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        verify(bookRepositoru, times(1)).findById(1L);
    }

   @Test
    void testAddBook() {
        Book book = new Book();
        book.setAuthors(Arrays.asList(new Author("Author1")));
        book.setGenres(Arrays.asList(new Genre("Genre1")));

        when(bookRepositoru.save(any(Book.class))).thenReturn(book);
        when(authorRepository.findByAuthorName("Author1")).thenReturn(null);
        when(genreRepository.findByGenreName("Genre1")).thenReturn(null);

        Book savedBook = bookService.addBook(book);

        assertNotNull(savedBook);
        verify(bookRepositoru, times(1)).save(any(Book.class));
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepositoru).deleteById(1L);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));

        verify(bookRepositoru, times(1)).deleteById(1L);
    }
}