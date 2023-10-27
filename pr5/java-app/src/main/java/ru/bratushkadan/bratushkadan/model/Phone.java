package ru.bratushkadan.bratushkadan.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "telephone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String manufacturer;
    
    @Column(name = "battery_capacity")
    private int batteryCapacity;
    
    @Column(name = "seller_id")
    private String sellerId;
    
    @Column(name = "product_type")
    private String productType;
    
    private double price;
    
    private String title;
}