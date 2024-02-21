package com.server.ecommerce.product.repository;

import com.server.ecommerce.product.domain.ProductPriceTracker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceTrackerRepository extends JpaRepository<ProductPriceTracker, Long> {
}
