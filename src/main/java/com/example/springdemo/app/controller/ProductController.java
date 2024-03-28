package com.example.springdemo.app.controller;

import com.example.springdemo.persistence2.model.Product;
import com.example.springdemo.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController// to tell spring it's a controller
@RequestMapping(path = "/products") // all method in this class add the path
public class ProductController {

    @Autowired
    ProductService productService;

    // curl -s -X GET localhost:8080/products/B1
    @GetMapping("/{id}") // /products/{id}
    public ResponseEntity<Product> getProduct(@PathVariable("id") String productId) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"name":"Android Development (Java)","price":380}'
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"id":"B1","name":"Android Development (Java)","price":380}'
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d '{"id":"B6","name":"JAX meow meow","price":999}'
    // windows need replace " to \"s
    // curl -s -X POST localhost:8080/products -H "Content-Type: application/json" -d "{\"id\":\"B6\",\"name\":\"JAX meow meow\",\"price\":999}"
    @PostMapping // /products
    public ResponseEntity<Product> createProduct(@RequestBody Product request) {
        // [NOTE] @RequestBody let spring auto set product fields
        // (by setXXX method, and XXX would be field name in JSON) using request JSON data

        Product product = productService.createProduct(request);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri() // 進來的uri
                .path("/{id}") // uri再串上path
                .build(Map.of("id", product.getId())); // 填入{id}
        // HttpHeaders headers = new HttpHeaders();
        // headers.setLocation(uri);
        //return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();// 201
        return ResponseEntity.created(uri).body(product);
    }

    // simple test to get header
    //    @GetMapping("/test")
    //    public ResponseEntity<String> getFoo(
    //            @RequestHeader(HttpHeaders.IF_MODIFIED_SINCE) String ifModifiedSince
    //    ) {
    //        return ResponseEntity.status(HttpStatus.OK).body(ifModifiedSince);
    //    }

    @PutMapping("/{id}") // /products/{id}
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String productId, @RequestBody Product requestProduct) {
        Product product = productService.replaceProduct(productId, requestProduct);
        return ResponseEntity.ok(product);
    }

    // curl -s -X DELETE localhost:8080/products/B1
    @DeleteMapping("/{id}")// /products/{id}
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

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
//    @GetMapping
//    public ResponseEntity<List<Product>> getProduct(
//            // @RequestParam(value = "searchKey", required = false, defaultValue = "") String keyword,
//            // @RequestParam(value = "sortField", required = false) String sortField,
//            // @RequestParam(value = "sortDir", required = false) String sortDir
//            @ModelAttribute ProductRequestParameter parameter // 建立 param class 自動對應
//    ) {
//        List<Product> pList = productService.getProducts(parameter);
//        return ResponseEntity.ok(pList);
//    }
}
