package com.project.code.Model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                         // auto increment PK

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be blank")
    @Column(nullable = false)
    private String address;

    // A store can have multiple inventory entries
    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference("inventory-store")
    private List<Inventory> inventoryList = new ArrayList<>();

    // --- Constructors ---
    public Store() {}

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // --- Getters & Setters ---
    public long getId() {
        return id;
    }

    public void setId(long id) { // typically not set externally; kept for completeness
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }
 }
