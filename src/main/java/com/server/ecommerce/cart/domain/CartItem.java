package com.server.ecommerce.cart.domain;

import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.utils.PriceUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class CartItem {

    private String id;

    private Product product;

    private long quantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Transient
    public Map<String, Object> priceDetail;

    public Map<String, Object> getPriceDetails(){
        return PriceUtils.buildPriceDetails(this.product.getPrice());
    }
}
