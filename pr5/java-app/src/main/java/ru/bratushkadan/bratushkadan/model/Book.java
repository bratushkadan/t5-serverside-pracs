package ru.bratushkadan.bratushkadan.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "product_type")
    private String productType;

    private double price;

    private String title;
}