package ru.bratushkadan.bratushkadan.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Data
@Entity
@Table(name="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @JsonProperty("client_id")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Getter
    @Setter
    @JsonProperty("product_id")
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Getter
    @Setter
    @Column(name = "quantity", nullable = false)
    @ColumnDefault("0")
    private Integer quantity;
}
