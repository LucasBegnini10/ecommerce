package com.server.ecommerce.product;

import com.server.ecommerce.infra.RestResponseHandler;
import com.server.ecommerce.product.dto.ProductCreateDTO;
import com.server.ecommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
