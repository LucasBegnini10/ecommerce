package com.server.ecommerce.product.controller;

import com.server.ecommerce.infra.RestResponseHandler;
import com.server.ecommerce.product.dto.ProductCreateDTO;
import com.server.ecommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductCreateDTO productCreateDTO){
        return RestResponseHandler.generateResponse(
                "Product Created",
                HttpStatus.CREATED,
                productService.create(productCreateDTO)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getProductById(@PathVariable UUID id){
        return RestResponseHandler.generateResponse(
                "Product found!",
                HttpStatus.CREATED,
                productService.getProductById(id)
        );
    }
}
