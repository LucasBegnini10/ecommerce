package com.server.ecommerce.product.service;

import com.server.ecommerce.product.domain.ProductInventory;
import com.server.ecommerce.product.exception.ProductNotFoundException;
import com.server.ecommerce.product.repository.ProductRepository;
import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.product.dto.ProductDTO;
import com.server.ecommerce.utils.StringUtils;
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

    public Product create(ProductDTO productDTO){
        return productRepository.save(buildProduct(new Product(), productDTO));
    }

    public Product update(UUID id, ProductDTO productDTO){
        Product product = getProductById(id);

        buildProduct(product, productDTO);

        return productRepository.save(product);
    }

    private Product buildProduct(Product product, ProductDTO productDTO){
        if(StringUtils.isNotNullAndNotBlank(productDTO.name())){
            product.setName(productDTO.name());
        }

        if(StringUtils.isNotNullAndNotBlank(productDTO.description())){
            product.setDescription(productDTO.description());
        }

        if(productDTO.price() != null){
            product.setPrice(productDTO.price());
        }

        if(productDTO.isEnabled() != null){
            product.setEnabled(productDTO.isEnabled());
        }

        ProductInventory productInventory = null;

        if(product.getProductInventory() != null){
            productInventory = product.getProductInventory();
        } else {
            productInventory = new ProductInventory();
        }

        productInventory.setProduct(product);

        if(productDTO.quantity() != null){
            productInventory.setAmount(productDTO.quantity());
        }

        product.setProductInventory(productInventory);

        return product;
    }
}
