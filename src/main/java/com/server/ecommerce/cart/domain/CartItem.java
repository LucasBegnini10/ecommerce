package com.server.ecommerce.cart.domain;

import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.utils.PriceUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class CartItem {

    public CartItem(){}

    public CartItem(String productId){
        this(productId, 1);
    }

    public CartItem(String productId, long quantity){
        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private String id;

    private String productId;

    private long quantity;

    private Product product;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Transient
    public Map<String, Object> priceDetail;

    public Map<String, Object> getPriceDetails(){
        return PriceUtils.buildPriceDetails(this.product.getPrice());
    }
}
