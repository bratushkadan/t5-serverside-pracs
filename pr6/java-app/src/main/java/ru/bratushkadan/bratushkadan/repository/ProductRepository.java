package ru.bratushkadan.bratushkadan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bratushkadan.bratushkadan.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySellerId(Long sellerId);
    List<Product> findAllByProductType(String productType);
}