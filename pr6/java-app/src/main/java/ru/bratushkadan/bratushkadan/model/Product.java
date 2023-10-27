package ru.bratushkadan.bratushkadan.model;

import lombok.*;

import ru.bratushkadan.bratushkadan.model.Product;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @JsonProperty("seller_id")
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;
    
    @Getter
    @Setter
    @JsonProperty("product_type")
    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @Getter
    @Setter
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    @ColumnDefault("'{}'")
    private Map<String, AdditionalField> additional = new HashMap<>();
}

@Data
class AdditionalField {
    private String presentation;
    private String value;
}