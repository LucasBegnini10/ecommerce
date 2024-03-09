package com.server.ecommerce.product.repository;

import com.server.ecommerce.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByIsEnabledTrue(Pageable pageable);
}
