package com.chubb.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties("items")
public class Order1 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String item; 

    @Min(1)
    private float price;

    @Min(1)
    private float quantity;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "order_item",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    @JsonManagedReference 
    private Set<Item> items = new HashSet<>();


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public float getQuantity() { return quantity; }
    public void setQuantity(float quantity) { this.quantity = quantity; }

    public Set<Item> getItems() { return items; }

    public void setItems(Set<Item> items) {
        this.items.clear();
        if (items != null) {
            items.forEach(this::addItem);
        }
    }

    
    public void addItem(Item item) {
        this.items.add(item);
        item.getOrders().add(this);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
        item.getOrders().remove(this);
    }
}