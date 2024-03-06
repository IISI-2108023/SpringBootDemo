package com.example.springdemo.rest.controller;

import com.example.springdemo.rest.model.Product;
import com.example.springdemo.rest.param.ProductRequestParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController// to tell spring it's a controller
@RequestMapping(path = "/products") // all method in this class add the path
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

    // ch3
    // curl -s -X GET localhost:8080/products/B1
    @GetMapping("/{id}") // /products/{id}
    public ResponseEntity<Product> getProduct(@PathVariable("id") String productId) {
        Product product = productMap.get(productId);
        return product == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(product);
    }

    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"name":"Android Development (Java)","price":380}'
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"id":"B1","name":"Android Development (Java)","price":380}'
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"id":"B6","name":"JAX meow meow","price":999}'
    // windows need replace " to \"s
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d "{\"id\":\"B6\",\"name\":\"JAX meow meow\",\"price\":999}"
    @PostMapping // /products
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        // [NOTE] @RequestBody let spring auto set product fields
        // (by setXXX method, and XXX would be field name in JSON) using request JSON data

        if (product.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (productMap.containsKey(product.getId())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();// 422
        }
        productMap.put(product.getId(), product);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri() // 進來的uri
                .path("/{id}") // uri再串上path
                .build(Map.of("id", product.getId())); // 填入{id}
        // HttpHeaders headers = new HttpHeaders();
        // headers.setLocation(uri);
        //return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();// 201
        return ResponseEntity.created(uri).build();
    }

    // simple test to get header
    @GetMapping("/test")
    public ResponseEntity<String> getFoo(
            @RequestHeader(HttpHeaders.IF_MODIFIED_SINCE) String ifModifiedSince
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(ifModifiedSince);
    }

    @PutMapping("/{id}") // /products/{id}
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
    @DeleteMapping("/{id}")// /products/{id}
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String productId) {
        if (!productMap.containsKey(productId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        productMap.remove(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ch4
    /* [NOTE]
    @RequestParam
        value
            alias for 'name'.
            對應 query string 上的參數名稱，若與變數名稱相同可以不用設定
        name
            The name of the request parameter to bind to.
            功能同 value
            可以代換 @RequestParam(name = "searchKey") 或是 @RequestParam("searchKey")
        required 是否必填
        defaultValue 預設值
     */
    @GetMapping
    public ResponseEntity<List<Product>> getProduct(
            // @RequestParam(value = "searchKey", required = false, defaultValue = "") String keyword,
            // @RequestParam(value = "sortField", required = false) String sortField,
            // @RequestParam(value = "sortDir", required = false) String sortDir
            @ModelAttribute ProductRequestParameter parameter // 建立 param class 自動對應
            ) {

        String sortField = parameter.getSortedField();
        String sortDir = parameter.getSortDir();
        String keyword = parameter.getSearchKey();

        // sortField：用來排序的欄位。本文支援「name」與「price」。
        Comparator<Product> comparator;
        if ("name".equals(sortField)) {
            comparator = Comparator.comparing(x -> x.getName().toLowerCase());
        } else if ("price".equals(sortField)) {
            comparator = Comparator.comparing(Product::getPrice);
        } else {
            comparator = (a, b) -> 0;
        }
        //  sortDir：排序的方向。僅支援「asc」（遞增）與「desc」（遞減）。
        if ("desc".equals(sortDir)) {
            comparator = comparator.reversed();
        }
        // searchKey：搜尋的關鍵字。本文支援搜尋「id」與「name」欄位。
        List<Product> productList = productMap.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) || p.getId().toLowerCase().contains(keyword.toLowerCase()))
                .sorted(comparator)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }
}
