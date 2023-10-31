package ru.bratushkadan.bratushkadan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.bratushkadan.bratushkadan.model.Cart;
import ru.bratushkadan.bratushkadan.model.Product;
import ru.bratushkadan.bratushkadan.repository.CartRepository;
import ru.bratushkadan.bratushkadan.repository.ProductRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public void addToCart(Long clientId, Long productId, Integer quantity) {
        Cart existingCart = cartRepository.findByClientIdAndProductId(clientId, productId);
        if (existingCart != null) {
            existingCart.setQuantity(quantity);
            cartRepository.save(existingCart);
        } else {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                Cart cart = new Cart();
                cart.setClientId(clientId);
                cart.setProductId(productId);
                cart.setQuantity(quantity);
                cartRepository.save(cart);
            } else {
                throw new IllegalArgumentException("Product not found");
            }
        }
    }

    public void removeFromCart(Long clientId, Long productId) {
        Cart cart = cartRepository.findByClientIdAndProductId(clientId, productId);
        if (cart != null) {
            cartRepository.deleteById(cart.getId());
        }
    }

    public void updateQuantity(Long clientId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByClientIdAndProductId(clientId, productId);
        if (cart != null) {
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        }
    }

    public List<Cart> getAllItemsInCart(Long clientId) {
        return cartRepository.findAllByClientId(clientId);
    }

    public String validateCart(Long clientId) {
        List<Cart> itemsInCart = cartRepository.findAllByClientId(clientId);

        for (Cart cart : itemsInCart) {
            Product product = productRepository.findById(cart.getProductId()).orElse(null);
            if (product == null) {
              return "В корзине есть несуществующий товар";
            } else if (product.getQuantity() < cart.getQuantity()) {
              return String.format("В корзине больше товаров (%d), чем осталось у продавца (%d)", cart.getQuantity(), product.getQuantity());
            }
        }

        return null;
    }

    public void checkoutAndClearCart(Long clientId) {
        List<Cart> itemsInCart = cartRepository.findAllByClientId(clientId);
        
        System.out.println(itemsInCart);

        for (Cart cart : itemsInCart) {
            Product product = productRepository.findById(cart.getProductId()).orElse(null);
            if (product != null) {
                int newQuantity = product.getQuantity() - cart.getQuantity();
                product.setQuantity(newQuantity);
                productRepository.save(product);
            }
        }
        
        cartRepository.deleteAll(itemsInCart);
    }
}