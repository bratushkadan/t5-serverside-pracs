package ru.bratushkadan.bratushkadan.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "washing_machine")
public class WashingMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String manufacturer;
    
    @Column(name = "tank_capacity")
    private int tankCapacity;
    
    @Column(name = "seller_id")
    private String sellerId;
    
    @Column(name = "product_type")
    private String productType;
    
    private double price;
    
    private String title;
}