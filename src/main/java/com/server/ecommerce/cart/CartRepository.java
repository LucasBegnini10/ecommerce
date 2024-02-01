package com.server.ecommerce.cart;

import com.server.ecommerce.cart.domain.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String> {
}
