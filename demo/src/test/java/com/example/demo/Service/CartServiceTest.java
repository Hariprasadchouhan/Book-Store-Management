package com.example.demo.Service;

import com.example.demo.Entity.Book;
import com.example.demo.Entity.Cart;
import com.example.demo.Entity.CartItem;
import com.example.demo.Entity.OrderHistory;
import com.example.demo.Repository.BookRepositoru;
import com.example.demo.Repository.CartItemRepository;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.OrderHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private BookRepositoru bookRepositoru;

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToCart_BookFoundAndSufficientStock() {
        // Arrange
        Long bookId = 1L;
        int quantity = 2;
        Book book = new Book();
        book.setId(bookId);
        book.setPrice(100);
        book.setStock(10);

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());

        when(bookRepositoru.findById(bookId)).thenReturn(Optional.of(book));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Cart result = cartService.addToCart(bookId, quantity);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCartItems().size());
        CartItem cartItem = result.getCartItems().get(0);
        assertEquals(book, cartItem.getBook());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(book.getPrice(), cartItem.getUnitPrice());
        assertEquals(quantity * book.getPrice(), cartItem.getSubTotal());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void addToCart_BookNotFound() {
        // Arrange
        Long bookId = 1L;
        int quantity = 2;

        when(bookRepositoru.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> cartService.addToCart(bookId, quantity), "Book not found");
    }

    @Test
    void addToCart_QuantityExceedsStock() {
        // Arrange
        Long bookId = 1L;
        int quantity = 15;
        Book book = new Book();
        book.setId(bookId);
        book.setPrice(100);
        book.setStock(10);

        when(bookRepositoru.findById(bookId)).thenReturn(Optional.of(book));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> cartService.addToCart(bookId, quantity), "Quantity exceeds stock available");
    }


    @Test
    void removeFromCart_BookExists() {
        // Arrange
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        Cart cart = new Cart();
        cart.setCartItems(cartItems);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Cart result = cartService.removeFromCart(bookId);

        // Assert
        assertNotNull(result);
        assertTrue(result.getCartItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void removeFromCart_BookDoesNotExist() {
        // Arrange
        Long bookId = 1L;
        Long nonExistentBookId = 2L;
        Book book = new Book();
        book.setId(bookId);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        Cart cart = new Cart();
        cart.setCartItems(cartItems);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Cart result = cartService.removeFromCart(nonExistentBookId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCartItems().size());
        assertEquals(bookId, result.getCartItems().get(0).getBook().getId());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void removeFromCart_CartNotFound() {
        // Arrange
        Long bookId = 1L;

        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> cartService.removeFromCart(bookId), "Cart not found");
    }

    @Test
    void viewCart() {
        // Arrange
        Cart cart = new Cart(); // Assuming Cart has a default constructor
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        // Act
        Cart result = cartService.viewCart();

        // Assert
        assertNotNull(result);
        verify(cartRepository, times(1)).findById(1L);
    }


    @Test
    void placeOrder() {
        // Arrange
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        Book book = new Book();
        book.setStock(10);
        cartItem.setBook(book);
        cartItem.setQuantity(1);
        cartItem.setUnitPrice(100.0);
        cartItem.setSubTotal(100.0);
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        Cart result = cartService.placeOrder();

        // Assert
        assertNotNull(result);
        assertTrue(result.getCartItems().isEmpty());
        verify(orderHistoryRepository, times(1)).save(any(OrderHistory.class));
        verify(bookRepositoru, times(1)).save(any(Book.class));
        verify(cartItemRepository, times(1)).deleteAll(cartItems);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void getOrderHistory() {
        // Arrange
        List<OrderHistory> orders = Collections.emptyList();
        when(orderHistoryRepository.findAll()).thenReturn(orders);

        // Act
        List<OrderHistory> result = cartService.getOrderHistory();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(orderHistoryRepository, times(1)).findAll();
    }
}