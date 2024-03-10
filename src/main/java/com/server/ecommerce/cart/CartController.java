package com.server.ecommerce.cart;

import com.server.ecommerce.cart.dto.CartAddProductDTO;
import com.server.ecommerce.cart.dto.CartUpdateQuantityDTO;
import com.server.ecommerce.infra.RestResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }


    @GetMapping("user/{userId}")
    public ResponseEntity<Object> getCart(@PathVariable String userId){
        return RestResponseHandler.generateResponse(
                "Cart by userId " + userId,
                HttpStatus.OK,
                cartService.getCartByUserId(userId)
        );
    }

    @PostMapping()
    public ResponseEntity<Object> addProductToCart(@RequestBody CartAddProductDTO cartAddProductDTO){
        cartService.addProductToCart(cartAddProductDTO);

        return RestResponseHandler.generateResponse(
                "Product added to cart",
                HttpStatus.OK,
                null
        );
    }

    @PatchMapping()
    public ResponseEntity<Object> updateQuantityProduct(@RequestBody CartUpdateQuantityDTO cartUpdateQuantityDTO){
        cartService.updateQuantityProduct(cartUpdateQuantityDTO);

        return RestResponseHandler.generateResponse(
                "Quantity updated!",
                HttpStatus.OK,
                null
        );
    }

    @DeleteMapping("user/{userId}/product/{productId}")
    public ResponseEntity<Object> deleteProductToCart(@PathVariable String userId, @PathVariable String productId){
        cartService.deleteProductCart(userId, productId);

        return RestResponseHandler.generateResponse(
                "Product deleted!",
                HttpStatus.OK,
                null
        );
    }

    @DeleteMapping("user/{userId}")
    public ResponseEntity<Object> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);

        return RestResponseHandler.generateResponse(
                "Cart cleaned!",
                HttpStatus.OK,
                null
        );
    }

    @GetMapping("/user/{userId}/product/{productId}/exists")
    public ResponseEntity<Object> productExistsInCart(@PathVariable String userId, @PathVariable String productId){
        return RestResponseHandler.generateResponse(
                "",
                HttpStatus.OK,
                cartService.productIsInCartOfUser(userId, productId)
        );
    }


}
