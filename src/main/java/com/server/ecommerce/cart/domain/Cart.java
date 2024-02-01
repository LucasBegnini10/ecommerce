package com.server.ecommerce.cart.domain;

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

    @Id
    private String userId;

    private User user;

    private Set<CartItem> items;

    private LocalDateTime viewedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public long getTotal() {
        return this.items.stream()
                .mapToLong(item -> item.getProduct().getPrice())
                .sum();
    }

    public Map<String, Object> getTotalDetails(){
        return PriceUtils.buildPriceDetails(this.getTotal());
    }
}
