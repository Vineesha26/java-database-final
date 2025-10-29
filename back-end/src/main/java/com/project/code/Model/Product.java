package com.project.code.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "product",
    uniqueConstraints = @UniqueConstraint(columnNames = "sku")
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;   // auto increment

    @NotNull(message = "Name cannot be null")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Category cannot be null")
    @Column(nullable = false)
    private String category;

    @NotNull(message = "Price cannot be null")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "SKU cannot be null")
    @Column(nullable = false, unique = true)
    private String sku;

    // A product can appear in multiple inventory rows (different stores)
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference("inventory-product")
    private List<Inventory> inventoryList = new ArrayList<>();

    // ---- Constructors ----
    public Product() {}

    public Product(String name, String category, Double price, String sku) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.sku = sku;
    }

    // ---- Getters & Setters ----
    public long getId() {
        return id;
    }

    public void setId(long id) {  // typically not set externally; included for completeness
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }
}


