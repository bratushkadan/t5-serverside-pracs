package ru.bratushkadan.bratushkadan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bratushkadan.bratushkadan.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAll();

    void deleteById(Long id);

    Cart findByClientIdAndProductId(Long clientId, Long productId);

    List<Cart> findAllByClientId(Long clientId);
}
