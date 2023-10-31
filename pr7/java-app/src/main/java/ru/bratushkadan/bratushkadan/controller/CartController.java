package ru.bratushkadan.bratushkadan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.bratushkadan.bratushkadan.model.Cart;
import ru.bratushkadan.bratushkadan.service.CartService;

@RequestMapping("/cart")
@RestController
public class CartController {

    @Autowired
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/")
    public void addToCart(@RequestParam(name = "client_id") Long clientId,
            @RequestParam(name = "product_id") Long productId, @RequestParam(name = "quantity") Integer quantity) {
        System.out.printf("clientId=%d productId=%d quantity=%d\n", clientId, productId, quantity);
        cartService.addToCart(clientId, productId, quantity);
    }

    @DeleteMapping("/")
    public void removeFromCart(@RequestParam(name = "client_id") Long clientId, @RequestParam(name = "product_id") Long productId) {
        cartService.removeFromCart(clientId, productId);
    }

    @PatchMapping("/")
    public void updateQuantity(@RequestParam(name = "client_id") Long clientId, @RequestParam(name = "product_id") Long productId,
            @RequestParam Integer quantity) {
        cartService.updateQuantity(clientId, productId, quantity);
    }

    @GetMapping("/")
    public List<Cart> getAllItemsInCart(@RequestParam(name = "client_id", required = true) Long clientId) {
        return cartService.getAllItemsInCart(clientId);
    }

    @PostMapping("/checkout")
    public ResponseEntity checkoutAndClearCart(@RequestParam(name = "client_id") Long clientId) {
        String err = cartService.validateCart(clientId);

        if (err != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Gson().toJson(new RestError(err)));
        }
        
        cartService.checkoutAndClearCart(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(new RestEmptyResponse()));
    }
}

@Data
class RestEmptyResponse {
    private final String message = "ok";
}

@AllArgsConstructor
@Data
class RestError {
    private final boolean error = true;
    private final String message;
}