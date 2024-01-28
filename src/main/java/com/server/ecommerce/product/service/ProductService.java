package com.server.ecommerce.product.service;

import com.server.ecommerce.product.domain.ProductInventory;
import com.server.ecommerce.product.exception.ProductNotFoundException;
import com.server.ecommerce.product.repository.ProductRepository;
import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.product.dto.ProductCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }


    public Product create(ProductCreateDTO productCreateDTO){
        return productRepository.save(buildProduct(productCreateDTO));
    }

    private Product buildProduct(ProductCreateDTO productCreateDTO){
        Product product = new Product();

        product.setName(productCreateDTO.name());
        product.setDescription(productCreateDTO.description());
        product.setPrice(productCreateDTO.price());
        product.setEnabled(productCreateDTO.isEnabled());

        ProductInventory productInventory = new ProductInventory();

        productInventory.setProduct(product);
        productInventory.setAmount(productCreateDTO.quantity());

        product.setProductInventory(productInventory);

        return product;
    }

    public Product getProductById(UUID id){
        Optional<Product> productById = productRepository.findById(id);

        return productById.orElseThrow(ProductNotFoundException::new);
    }

    public Page<Product> findAllActive(
            int pageNumber,
            int pageSize,
            Sort.Direction direction,
            String sort
    ){
        Pageable pageable = null;

        if(direction != null && sort != null){
            pageable = PageRequest.of(pageNumber, pageSize, direction, sort);
        } else{
            pageable = PageRequest.of(pageNumber, pageSize);
        }

        return productRepository.findByIsEnabledTrue(pageable);
    }
}
