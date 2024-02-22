package com.server.ecommerce.cart.domain;

import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.user.User;
import com.server.ecommerce.utils.PriceUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@RedisHash("cart")
@Getter
@Setter
public class Cart implements Serializable {

    public Cart(){

    }

    @Id
    private String userId;

    private Set<CartItem> items;

    private LocalDateTime viewedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public long getTotal() {
        return this.items.stream()
                .mapToLong(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public Map<String, Object> getTotalDetails(){
        return PriceUtils.buildPriceDetails(this.getTotal());
    }

    public int getCartLength(){
        return this.items.size();
    }
}
