package com.server.ecommerce.product.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.server.ecommerce.infra.BaseEntity;
import com.server.ecommerce.product.utils.PriceUtils;
import com.server.ecommerce.utils.MoneyUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
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

    private long price;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductPicture> pictures;

    @JsonManagedReference
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductInventory productInventory;

    @Transient
    public Map<String, Object> priceDetail;

    public Map<String, Object> getPriceDetail(){
        Map<String, Object> priceDetail = new HashMap<String, Object>();

        double priceDouble = PriceUtils.formatLongToDecimal(this.price);

        priceDetail.put("priceInt", this.price);
        priceDetail.put("priceDouble", priceDouble);
        priceDetail.put("priceString", MoneyUtils.formatCurrency(priceDouble));
        priceDetail.put("currency", "BRL");
        priceDetail.put("currency_symbol", "R$");

        return priceDetail;
    }

}
