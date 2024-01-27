package com.server.ecommerce.product.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.server.ecommerce.infra.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@Setter
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    private String description;

    private int price;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductPicture> pictures;

    @JsonManagedReference
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductInventory productInventory;

}
