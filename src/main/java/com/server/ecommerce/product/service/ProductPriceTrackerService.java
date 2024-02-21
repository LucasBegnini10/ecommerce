package com.server.ecommerce.product.service;

import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.product.domain.ProductPriceTracker;
import com.server.ecommerce.product.repository.ProductPriceTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceTrackerService {

    private final ProductPriceTrackerRepository productPriceTrackerRepository;

    @Autowired
    public ProductPriceTrackerService(ProductPriceTrackerRepository productPriceTrackerRepository){
        this.productPriceTrackerRepository = productPriceTrackerRepository;
    }

    public void set(Product product, long newPrice){
        ProductPriceTracker productPriceTracker = new ProductPriceTracker();
        productPriceTracker.setProduct(product);
        productPriceTracker.setOldPrice(product.getPrice());
        productPriceTracker.setNewPrice(newPrice);
        productPriceTrackerRepository.save(productPriceTracker);
    }


}
