package com.server.ecommerce.product.repository;

import com.server.ecommerce.product.domain.ProductPicture;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductPictureRepository extends JpaRepository<ProductPicture, Long> {


    @Query(value = "UPDATE product_picture pp " +
            "SET pp.picture_order = pp.picture_order - 1 " +
            "WHERE pp.product_id = ?1 " +
            "AND pp.picture_order > ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateIndexPicturesByProductIdAndDeletedIndex(UUID productId, int deletedIndex);
}
