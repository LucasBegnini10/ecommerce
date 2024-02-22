package com.server.ecommerce.cart.domain;

import com.server.ecommerce.utils.PriceUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

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

    @TimeToLive
    private Long expiration;


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

    public void setUpdatedAt(){
        this.setUpdatedAt(LocalDateTime.now());
    }

    public void setCreatedAt(){
        this.setCreatedAt(LocalDateTime.now());
    }

    public void setViewedAt(){
        this.setViewedAt(LocalDateTime.now());
    }

}
