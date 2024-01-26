package com.server.ecommerce.product;

import com.server.ecommerce.infra.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    private String description;

    private float price;

    @Column(name = "img_url")
    private String imgUrl;

    private int quantity;

}
