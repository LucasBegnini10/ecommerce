package com.server.ecommerce.product.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.server.ecommerce.infra.BaseEntity;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table
@Setter
public class ProductPriceTracker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @Column(name = "old_price")
    private long oldPrice;

    @Column(name = "new_price")
    private long newPrice;

}
