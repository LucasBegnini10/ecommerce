package com.server.ecommerce.cart;

import com.server.ecommerce.product.service.ProductService;
import com.server.ecommerce.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartService {

    private final CartRepository cartRepository;

    private final UserService userService;

    private final ProductService productService;


    @Autowired
    public CartService(
            CartRepository cartRepository,
            UserService userService,
            ProductService productService
    ){
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
    }



}
