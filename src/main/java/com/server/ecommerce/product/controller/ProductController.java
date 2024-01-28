package com.server.ecommerce.product.controller;

import com.server.ecommerce.infra.RestResponseHandler;
import com.server.ecommerce.product.dto.ProductCreateDTO;
import com.server.ecommerce.product.service.ProductService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
                HttpStatus.OK,
                productService.getProductById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Object> getAllActive(
            @RequestParam("page") int pageNumber,
            @RequestParam("size") int pageSize,
            @Nullable @RequestParam("direction") Sort.Direction direction,
            @Nullable @RequestParam("sort") String sort
            ){
        return RestResponseHandler.generateResponse(
                "Product found!",
                HttpStatus.OK,
                productService.findAllActive(pageNumber, pageSize, direction, sort)
        );
    }
}
