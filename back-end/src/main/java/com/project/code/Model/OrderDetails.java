package com.project.code.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                      // auto increment PK

    // Customer who placed the order
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonManagedReference
    private Customer customer;

    // Store where the order was placed
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonManagedReference
    private Store store;

    // Total price of the order
    @Column(nullable = false)
    private Double totalPrice;

    // Date/time when the order was placed
    @Column(nullable = false)
    private LocalDateTime date;

    // List of items within this order
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference("order-orderItems")
    private List<OrderItem> orderItems = new ArrayList<>();

    // --- Constructors ---
    public OrderDetails() {}

    public OrderDetails(Customer customer, Store store, Double totalPrice, LocalDateTime date) {
        this.customer = customer;
        this.store = store;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) { // generally not set externally, kept for completeness
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
