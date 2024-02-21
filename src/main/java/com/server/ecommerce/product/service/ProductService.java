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

    private final ProductPriceTrackerService productPriceTrackerService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductPriceTrackerService productPriceTrackerService){
        this.productRepository = productRepository;
        this.productPriceTrackerService = productPriceTrackerService;
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

        setProductPriceTracker(product, productDTO);

        buildProduct(product, productDTO);
        return productRepository.save(product);
    }

    private void setProductPriceTracker(Product product, ProductDTO productDTO){
        if(productDTO.price() != null && product.getPrice() == productDTO.price()){
            productPriceTrackerService.set(product, productDTO.price());
        }
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

    public void deleteProduct(UUID id){
        Product product = getProductById(id);

        productRepository.delete(product);
    }

    public boolean hasProductInInventory(Product product){
        return hasProductInInventory(product, 1);
    }

    public boolean hasProductInInventory(Product product, long quantity){
        ProductInventory productInventory = product.getProductInventory();
        return productInventory.getAmount() >= quantity;
    }
}
