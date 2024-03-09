package com.server.ecommerce.cart;

import com.server.ecommerce.cart.domain.Cart;
import com.server.ecommerce.cart.domain.CartEventType;
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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class CartService {

    private static final long TIME_EXPIRATION_CART = 60L * 60L * 24L * 7L; //7 days
    private static final int MIN_QUANTITY_TO_ADD_IN_CART = 1;

    private final CartRepository cartRepository;

    private final UserService userService;

    private final ProductService productService;

    public CartService(
            CartRepository cartRepository,
            UserService userService,
            ProductService productService
    ) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
    }

    public void addProductToCart(CartAddProductDTO cartAddProductDTO) {
        User user = userService.findUserById(cartAddProductDTO.userId());
        Product product = productService.getProductById(cartAddProductDTO.productId());

        Cart cart = getOrCreateCartByUser(user);

        setProductToCart(cart, product);

        saveCart(cart);
    }

    private Cart getOrCreateCartByUser(User user) {
        try {
            return getCart(user);
        } catch (CartNotFoundException ex) {
            return createNewCart(user);
        }
    }

    public Cart getCart(User user) {
        return cartRepository
                .findById(user.getId())
                .orElseThrow(CartNotFoundException::new);
    }

    public Cart createNewCart(User user) {
        Cart cart = new Cart();

        cart.setUserId(user.getId());

        cart.setItems(new HashSet<>());
        setDateTimeMetricsByEvent(cart, CartEventType.CREATE_CART);

        return cart;
    }

    private void setProductToCart(Cart cart, Product product) {
        if (cartHasProduct(cart, product)) {
            updateProductQuantityInCart(cart, product, MIN_QUANTITY_TO_ADD_IN_CART);
        } else {
            addNewProductToCart(cart, product);
        }
    }

    private boolean cartHasProduct(Cart cart, Product product) {
        return cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(product.getId()));
    }

    private void updateProductQuantityInCart(Cart cart, Product product, long quantity) {
        validateInventoryOfProduct(product, quantity);

        try {
            CartItem item = getCartItemByProduct(cart, product);
            item.setQuantity(quantity);
            setDateTimeMetricsByEvent(cart, CartEventType.UPDATE_PRODUCT);
        } catch (ProductNotFoundInCartException ex) {
            addNewProductToCart(cart, product);
        }
    }

    private void validateInventoryOfProduct(Product product, long quantity) {
        if (quantity <= 0) {
            throw new ProductQuantityInvalidException();
        }

        if (!productService.hasProductInInventory(product, quantity)) {
            throw new ProductInventoryExceededException();
        }
    }

    private CartItem getCartItemByProduct(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst()
                .orElseThrow(ProductNotFoundInCartException::new);
    }

    private void addNewProductToCart(Cart cart, Product product) {
        Set<CartItem> items = cart.getItems();
        items.add(new CartItem(product.getId()));
        setDateTimeMetricsByEvent(cart, CartEventType.ADD_PRODUCT);
    }

    public Cart getCartByUserId(String userId) {
        User user = userService.findUserById(userId);
        try {
            Cart cart = getCart(user);

            setDateTimeMetricsByEvent(cart, CartEventType.GET_CART);
            saveCart(cart);

            List<String> productIds = getProductIdsFromCart(cart);
            List<Product> products = productService.getProductsByIds(productIds);

            Set<CartItem> updatedItems = enrichCartItemsWithProducts(cart.getItems(), products);
            cart.setItems(updatedItems);

            return cart;
        } catch (CartNotFoundException ex){
            return createNewCart(user);
        }
    }

    private List<String> getProductIdsFromCart(Cart cart) {
        return cart.getItems().stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());
    }

    private Set<CartItem> enrichCartItemsWithProducts(Set<CartItem> items, List<Product> products) {
        return items.stream()
                .map(item -> updateCartItemWithProduct(item, products))
                .collect(Collectors.toSet());
    }

    private CartItem updateCartItemWithProduct(CartItem item, List<Product> products) {
        products.stream()
                .filter(product -> product.getId().equals(item.getProductId()))
                .findFirst()
                .ifPresent(item::setProduct);
        return item;
    }

    public void saveCart(Cart cart) {
        if (cartIsEmpty(cart)) {
            clearCart(cart);
        } else {
            cartRepository.save(cart);
        }
    }

    public void updateQuantityProduct(CartUpdateQuantityDTO cartUpdateQuantityDTO) {
        User user = userService.findUserById(cartUpdateQuantityDTO.userId());
        Product product = productService.getProductById(cartUpdateQuantityDTO.productId());

        Cart cart = getOrCreateCartByUser(user);

        updateProductQuantityInCart(cart, product, cartUpdateQuantityDTO.quantity());

        saveCart(cart);
    }

    public void deleteProductCart(String userId, String productId) {
        User user = userService.findUserById(userId);
        Product product = productService.getProductById(productId);

        Cart cart = getCart(user);

        removeProductToCart(cart, product);

        saveCart(cart);
    }

    private void removeProductToCart(Cart cart, Product product) {
        Set<CartItem> items = cart.getItems();
        items.remove(getCartItemByProduct(cart, product));
        setDateTimeMetricsByEvent(cart, CartEventType.DELETE_PRODUCT);
    }

    private boolean cartIsEmpty(Cart cart) {
        return cart.getItems().isEmpty();
    }

    public void clearCart(String userId) {
        User user = userService.findUserById(userId);
        clearCart(getCart(user));
    }

    private void clearCart(Cart cart) {
        cartRepository.delete(cart);
    }

    private void setDateTimeMetricsByEvent(Cart cart, CartEventType event) {
        switch (event) {
            case ADD_PRODUCT -> {
                cart.setViewedAt();
                cart.setUpdatedAt();
                resetExpirationCart(cart);
            }
            case GET_CART -> {
                cart.setViewedAt();
                resetExpirationCart(cart);
            }
            case CREATE_CART -> {
                cart.setCreatedAt();
                cart.setUpdatedAt();
                cart.setUpdatedAt();
                resetExpirationCart(cart);
            }
            case DELETE_PRODUCT, UPDATE_PRODUCT -> {
                cart.setUpdatedAt();
                cart.setViewedAt();
                resetExpirationCart(cart);
            }
        }
    }

    private void resetExpirationCart(Cart cart) {
        cart.setExpiration(TIME_EXPIRATION_CART);
    }
}
