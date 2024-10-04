package com.example.demo.Service;

import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepositoru bookRepositoru;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    // Add book to cart
    public Cart addToCart(Long bookId, int quantity) {
        Optional<Book> optionalBook = bookRepositoru.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            // Check current stock against the requested quantity
            if (book.getStock() >= quantity) {
                Cart cart = cartRepository.findById(1L).orElse(new Cart());  // Single cart instance
                CartItem cartItem = cart.getCartItems().stream()
                        .filter(item -> item.getBook().getId().equals(bookId))
                        .findFirst()
                        .orElse(new CartItem());
                if (cartItem.getBook() == null) {
                    cartItem.setBook(book);
                }
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.setUnitPrice(book.getPrice());
                cartItem.setSubTotal(cartItem.getQuantity() * book.getPrice());
                cartItem.setCart(cart);

                // If the book is already in the cart, we only update the quantity and subtotal
                if (!cart.getCartItems().contains(cartItem)) {
                    cart.getCartItems().add(cartItem);
                }
                updateCartTotals(cart);

                return cartRepository.save(cart);
            } else {
                throw new IllegalStateException("Quantity exceeds stock available");
            }
        } else {
            throw new IllegalStateException("Book not found");
        }
    }

    // Remove book from cart
    public Cart removeFromCart(Long bookId) {
        Cart cart = cartRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Cart not found"));
        cart.getCartItems().removeIf(item -> item.getBook().getId().equals(bookId));
        updateCartTotals(cart);
        return cartRepository.save(cart);
    }

    // View cart items
    public Cart viewCart() {
        Cart cart = cartRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Cart not found"));
        updateCartTotals(cart);
        return cart;   }

    // Place order
    public Cart placeOrder() {
        Cart cart = cartRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Cart not found"));

        // Calculate totals
        double total = cart.getCartItems().stream().mapToDouble(CartItem::getSubTotal).sum();
        double cgst = total * 0.18;
        double sgst = total * 0.18;
        double grandTotal = total + cgst + sgst;

        // Save order to history (deep copy the cart items)
        OrderHistory orderHistory = new OrderHistory();
        List<CartItem> copiedItems = new ArrayList<>();

        // Deep copy the items in the cart to avoid shared references
        for (CartItem item : cart.getCartItems()) {
            CartItem copiedItem = new CartItem();
            copiedItem.setBook(item.getBook());
            copiedItem.setQuantity(item.getQuantity());
            copiedItem.setUnitPrice(item.getUnitPrice());
            copiedItem.setSubTotal(item.getSubTotal());
            copiedItems.add(copiedItem);
        }

        orderHistory.setOrderedItems(copiedItems);
        orderHistory.setTotal(total);
        orderHistory.setCgst(cgst);
        orderHistory.setSgst(sgst);
        orderHistory.setGrandTotal(grandTotal);
        orderHistory.setOrderDate(LocalDateTime.now());
        orderHistoryRepository.save(orderHistory);

        // Update book stock
        for (CartItem item : cart.getCartItems()) {
            Book book = item.getBook();
            book.setStock(book.getStock() - item.getQuantity());  // Update the actual stock
            bookRepositoru.save(book);
        }

        // Clear the cart items explicitly
        cartItemRepository.deleteAll(cart.getCartItems());  // This removes cart items from the database
        cart.getCartItems().clear();  // Clear the items in memory

        // Save the empty cart
        return cartRepository.save(cart);
    }
    public List<OrderHistory> getOrderHistory(){
        return orderHistoryRepository.findAll();
    }
    private void updateCartTotals(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        double cgst = total * 0.18;
        double sgst = total * 0.18;
        double grandTotal = total + cgst + sgst;

        cart.setTotal(total);
        cart.setCgst(cgst);
        cart.setSgst(sgst);
        cart.setGrandTotal(grandTotal);
    }


}