package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "order_history")
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> orderedItems;

    private double total;
    private double cgst;
    private double sgst;
    private double grandTotal;

    private LocalDateTime orderDate;  // To track when the order was placed

    public OrderHistory(Long id, List<CartItem> orderedItems, double total, double cgst, double sgst, double grandTotal, LocalDateTime orderDate) {
        this.id = id;
        this.orderedItems = orderedItems;
        this.total = total;
        this.cgst = cgst;
        this.sgst = sgst;
        this.grandTotal = grandTotal;
        this.orderDate = orderDate;
    }

    public OrderHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<CartItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}