package com.server.ecommerce.product.domain;

import com.server.ecommerce.infra.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table
public class ProductPicture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Column(name = "picture_url")
    private String pictureUrl;

    private int pictureOrder;
}
