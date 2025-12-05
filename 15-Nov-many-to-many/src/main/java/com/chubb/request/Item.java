package com.chubb.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties("orders")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String name;

    @Min(1)
    private float price;

    @Min(1)
    private float quantity;

    @ManyToMany(mappedBy = "items")
    @JsonBackReference
    private Set<Order1> orders = new HashSet<>();


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public float getQuantity() { return quantity; }
    public void setQuantity(float quantity) { this.quantity = quantity; }

    public Set<Order1> getOrders() { return orders; }
    public void setOrders(Set<Order1> orders) {
        this.orders = orders;
    }
}