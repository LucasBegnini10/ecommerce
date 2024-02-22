package com.server.ecommerce.cart;

import com.server.ecommerce.cart.domain.Cart;
import com.server.ecommerce.cart.domain.CartItem;
import com.server.ecommerce.cart.dto.CartAddProductDTO;
import com.server.ecommerce.cart.dto.CartUpdateQuantityDTO;
import com.server.ecommerce.cart.exception.CartNotFoundException;
import com.server.ecommerce.cart.exception.ProductInventoryExceededException;
import com.server.ecommerce.cart.exception.ProductNotFoundInCartException;
import com.server.ecommerce.cart.exception.ProductQuantityInvalidException;
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

        Cart cart = getOrCreateCartByUser(user);

        setProductToCart(cart, product);

        saveCart(cart);
    }

    private Cart getOrCreateCartByUser(User user){
        try {
            return getCart(user);
        } catch (CartNotFoundException ex){
            return createNewCart(user);
        }
    }

    public Cart getCart(User user){
        return cartRepository
                .findById(user.getId().toString())
                .orElseThrow(CartNotFoundException::new);
    }

    public Cart createNewCart(User user){
        Cart cart = new Cart();

        LocalDateTime now = LocalDateTime.now();
        cart.setUserId(user.getId().toString());

        Set<CartItem> items = new HashSet<>();

        cart.setItems(items);
        cart.setCreatedAt(now);

        return cart;
    }

    private void setProductToCart(Cart cart, Product product){
        if(cartHasProduct(cart, product)) {
            updateQuantityProduct(cart, product, 1);
        } else{
            addNewProductToCart(cart, product);
        }
    }

    private boolean cartHasProduct(Cart cart, Product product){
        return cart.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));
    }

    private void updateQuantityProduct(Cart cart, Product product, long quantity){
        validateInventoryOfProduct(product, quantity);

        try {
            CartItem item = getCartItemByProduct(cart, product);
            item.setQuantity(quantity);
        } catch (ProductNotFoundInCartException ex){
            addNewProductToCart(cart, product);
        }
    }

    private void validateInventoryOfProduct(Product product, long quantity){
        if(quantity <= 0){
            throw new ProductQuantityInvalidException();
        }

        if(!productService.hasProductInInventory(product, quantity)){
            throw new ProductInventoryExceededException();
        }
    }

    private CartItem getCartItemByProduct(Cart cart, Product product){
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(ProductNotFoundInCartException::new);
    }

    private void addNewProductToCart(Cart cart, Product product){
        Set<CartItem> items = cart.getItems();
        items.add(new CartItem(product));
    }

    public Cart getCartByUserId(UUID userId){
        User user = userService.findUserById(userId);
        return getCart(user);
    }

    public void saveCart(Cart cart){
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setViewedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    public void updateQuantityProduct(CartUpdateQuantityDTO cartUpdateQuantityDTO){
        User user = userService.findUserById(cartUpdateQuantityDTO.userId());
        Product product = productService.getProductById(cartUpdateQuantityDTO.productId());

        Cart cart = getOrCreateCartByUser(user);

        updateQuantityProduct(cart, product, cartUpdateQuantityDTO.quantity());

        saveCart(cart);
    }

    public void deleteProductCart(UUID userId, UUID productId){
        User user = userService.findUserById(userId);
        Product product = productService.getProductById(productId);

        Cart cart = getCart(user);

        removeProductToCart(cart, product);

        if(cartIsEmpty(cart)){
            clearCart(cart);
            return;
        }

        saveCart(cart);
    }

    private void removeProductToCart(Cart cart, Product product){
        Set<CartItem> items = cart.getItems();
        items.remove(getCartItemByProduct(cart, product));
    }

    private boolean cartIsEmpty(Cart cart){
        return cart.getItems().isEmpty();
    }

    public void clearCart(UUID userId){
        User user = userService.findUserById(userId);
        clearCart(getCart(user));
    }

    private void clearCart(Cart cart){
        cartRepository.delete(cart);
    }

}
