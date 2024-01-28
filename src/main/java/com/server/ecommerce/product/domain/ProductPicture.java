package com.server.ecommerce.product.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.server.ecommerce.infra.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class ProductPicture extends BaseEntity {

    public ProductPicture(){}

    public ProductPicture(Product product, String pictureUrl, int order, String key){
        this.product = product;
        this.pictureUrl = pictureUrl;
        this.pictureOrder = order;
        this.pictureKey = key;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Column(name = "picture_url", unique = true)
    private String pictureUrl;

    @Column(name = "picture_key", unique = true)
    public String pictureKey;

    private int pictureOrder;
}
