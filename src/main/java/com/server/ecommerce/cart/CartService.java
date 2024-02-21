package com.server.ecommerce.cart;

import com.server.ecommerce.cart.domain.Cart;
import com.server.ecommerce.cart.domain.CartItem;
import com.server.ecommerce.cart.dto.CartAddProductDTO;
import com.server.ecommerce.cart.dto.CartUpdateQuantityDTO;
import com.server.ecommerce.cart.exception.CartNotFoundException;
import com.server.ecommerce.cart.exception.ProductInventoryExceededException;
import com.server.ecommerce.cart.exception.ProductNotFoundInCartException;
import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.product.service.ProductService;
import com.server.ecommerce.user.User;
import com.server.ecommerce.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


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

    public void addProductToCart(CartAddProductDTO cartAddProductDTO){
        User user = userService.findUserById(cartAddProductDTO.userId());
        Product product = productService.getProductById(cartAddProductDTO.productId());

        if(!productService.hasProductInInventory(product))
            throw new ProductInventoryExceededException();

        setProductInCart(user, product);
    }

    private void setProductInCart(User user, Product product){
        try {
            Cart cart = getCart(user);
            setProductInCart(cart, product, 1);

        } catch (CartNotFoundException ex) {
            createNewCart(user, product);
        }
    }

    private void setProductInCart(Cart cart, Product product, long quantity){
        if(cartHasProduct(cart, product)) {
            updateQuantityProduct(cart, product, quantity);
        } else{
            setNewProductInCart(cart, product, quantity);
        }
    }

    private void setNewProductInCart(Cart cart, Product product, long quantity){
        Set<CartItem> items = cart.getItems();
        items.add(new CartItem(product, quantity));

        saveCart(cart);
    }

    public Cart getCart(UUID userId){
        User user = userService.findUserById(userId);
        return getCart(user);
    }

    public Cart getCart(User user){
        return cartRepository
                .findById(user.getId().toString())
                .orElseThrow(CartNotFoundException::new);
    }


    public void saveCart(Cart cart){
        cartRepository.save(cart);
    }

    private boolean cartHasProduct(Cart cart, Product product){
        return cart.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));
    }

    public void createNewCart(User user, Product product){
        Cart cart = new Cart();

        LocalDateTime now = LocalDateTime.now();
        cart.setUserId(user.getId().toString());

        Set<CartItem> items = new HashSet<>();
        CartItem item = new CartItem(product);

        items.add(item);

        cart.setCreatedAt(now);
        cart.setItems(items);
        cart.setViewedAt(now);
        cart.setUpdatedAt(now);

        saveCart(cart);
    }

    public void updateQuantityProduct(CartUpdateQuantityDTO cartUpdateQuantityDTO){
        User user = userService.findUserById(cartUpdateQuantityDTO.userId());
        Product product = productService.getProductById(cartUpdateQuantityDTO.productId());

        if(!productService.hasProductInInventory(product, cartUpdateQuantityDTO.quantity())){
            throw new ProductInventoryExceededException();
        }

        Cart cart = getCart(user);

        updateQuantityProduct(cart, product, cartUpdateQuantityDTO.quantity());
    }

    public void updateQuantityProduct(Cart cart, Product product, long quantity){
        try {
            CartItem item = getCartItemByProduct(cart, product);
            item.setQuantity(quantity);

            saveCart(cart);
        } catch (ProductNotFoundInCartException ex){
            setProductInCart(cart, product, quantity);
        }
    }

    private CartItem getCartItemByProduct(Cart cart, Product product){
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(ProductNotFoundInCartException::new);
    }

    public void deleteProductCart(UUID userId, UUID productId){
        User user = userService.findUserById(userId);
        Product product = productService.getProductById(productId);

        Cart cart = getCart(user);

        Set<CartItem> items = cart.getItems();

        items.remove(getCartItemByProduct(cart, product));

        saveCart(cart);
    }

    public void clearCart(UUID userId){
        User user = userService.findUserById(userId);
        cartRepository.delete(getCart(user));
    }

}
