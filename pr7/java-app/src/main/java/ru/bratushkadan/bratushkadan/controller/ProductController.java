package ru.bratushkadan.bratushkadan.controller;

import java.util.List;

import ru.bratushkadan.bratushkadan.model.Product;
import ru.bratushkadan.bratushkadan.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/all")
    public List<Product> getProducts(@RequestParam(name = "product_type", required = false) String productType) {
        System.out.printf("[Product Controller]: getProducts | product_type=%s\n", productType);

        if (productType == null) {
            return productRepository.findAll();
        }
        return productRepository.findAllByProductType(productType);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    public Product addProduct(@RequestBody Product product) {
        System.out.println(new Gson().toJson(product));
        return productRepository.save(product);
    }

    @PutMapping("/")
    public Product updateProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
