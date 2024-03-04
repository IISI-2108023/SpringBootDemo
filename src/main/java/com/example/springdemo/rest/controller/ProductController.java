package com.example.springdemo.rest.controller;

import com.example.springdemo.rest.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RestController// to tell spring it's a controller
public class ProductController {

    // [temp] init data for product
    private static final Map<String, Product> productMap = new HashMap<>();

    static {
        Stream.of(
                Product.of("B1", "Android Development (Java)", 380),
                Product.of("B2", "Android Development (Kotlin)", 420),
                Product.of("B3", "Data Structure (Java)", 250),
                Product.of("B4", "Finance Management", 450),
                Product.of("B5", "Human Resource Management", 330)
        ).forEach(p -> productMap.put(p.getId(), p));
    }

    // curl -s -X GET localhost:8080/products/B1
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String productId) {
        Product product = productMap.get(productId);
        return product == null ?
                ResponseEntity.status(400).build() :
                ResponseEntity.status(200).body(product);
    }

    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"name":"Android Development (Java)","price":380}'
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"id":"B1","name":"Android Development (Java)","price":380}'
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"id":"B6","name":"JAX meow meow","price":999}'
    // windows need replace " to \"
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d "{\"id\":\"B6\",\"name\":\"JAX meow meow\",\"price\":999}"
    @PostMapping("/products")
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        // @RequestBody let spring auto set product fields
        // (by setXXX method, and XXX would be field name in JSON) using request JSON data

        if (product.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (productMap.containsKey(product.getId())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();// 422
        }

        productMap.put(product.getId(), product);
        return ResponseEntity.status(HttpStatus.CREATED).build();// 201
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") String productId, @RequestBody Product requestProduct) {
        Product product = productMap.get(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        product.setName(requestProduct.getName());
        product.setPrice(requestProduct.getPrice());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // curl -s -X DELETE localhost:8080/products/B1
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String productId) {
        if (!productMap.containsKey(productId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        productMap.remove(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
