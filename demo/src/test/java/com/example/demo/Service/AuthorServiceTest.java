package com.example.demo.Service;

import com.example.demo.Entity.Author;
import com.example.demo.Entity.Book;
import com.example.demo.Repository.BookRepositoru;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private BookRepositoru bookRepositoru;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAuthors() {
        // Arrange
        Long bookId = 1L;
        Author author1 = new Author("Author1");
        Author author2 = new Author("Author2");
        Book book = new Book();
        book.setId(bookId);
        book.setAuthors(Arrays.asList(author1, author2));

        when(bookRepositoru.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        List<Author> authors = bookService.getBookAuthors(bookId);

        // Assert
        assertNotNull(authors);
        assertEquals(2, authors.size());
        assertEquals("Author1", authors.get(0).getAuthorName());
        assertEquals("Author2", authors.get(1).getAuthorName());

        verify(bookRepositoru, times(1)).findById(bookId);
    }

    @Test
    void getAuthors_BookNotFound() {
        // Arrange
        Long bookId = 99L; // Non-existent book ID
        when(bookRepositoru.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            bookService.getBookAuthors(bookId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(bookRepositoru, times(1)).findById(bookId);
    }
}