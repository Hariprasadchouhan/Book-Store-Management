package com.example.demo.Controller;

import com.example.demo.Entity.Cart;
import com.example.demo.Entity.OrderHistory;
import com.example.demo.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add/{bookId}")
    public Cart addToCart(@PathVariable Long bookId, @RequestParam int quantity) {
        return cartService.addToCart(bookId, quantity);
    }

    @DeleteMapping("/remove/{bookId}")
    public Cart removeFromCart(@PathVariable Long bookId) {
        return cartService.removeFromCart(bookId);
    }

    @GetMapping()
    public Cart viewCart() {
        return cartService.viewCart();
    }

    @PostMapping("/purchase")
    public Cart placeOrder() {
        return cartService.placeOrder();
    }

    @GetMapping("/history")
    public List<OrderHistory> getOrderHistory() {
        return cartService.getOrderHistory();
    }
}