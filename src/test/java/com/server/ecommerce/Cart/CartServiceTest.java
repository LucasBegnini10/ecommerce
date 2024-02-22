package com.server.ecommerce.Cart;

import com.server.ecommerce.cart.CartRepository;
import com.server.ecommerce.cart.CartService;
import com.server.ecommerce.cart.domain.Cart;
import com.server.ecommerce.cart.domain.CartItem;
import com.server.ecommerce.cart.dto.CartAddProductDTO;
import com.server.ecommerce.cart.dto.CartUpdateQuantityDTO;
import com.server.ecommerce.cart.exception.ProductInventoryExceededException;
import com.server.ecommerce.cart.exception.ProductNotFoundInCartException;
import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.product.domain.ProductInventory;
import com.server.ecommerce.product.service.ProductService;
import com.server.ecommerce.user.User;
import com.server.ecommerce.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProductToCart_NewUser_NewCartCreated() {
        // Arrange
        CartAddProductDTO cartAddProductDTO = new CartAddProductDTO(UUID.randomUUID(), UUID.randomUUID());
        User user = new User();
        user.setId(cartAddProductDTO.userId());
        Product product = new Product();
        Cart cart = new Cart();

        when(userService.findUserById(cartAddProductDTO.userId())).thenReturn(user);
        when(productService.getProductById(cartAddProductDTO.productId())).thenReturn(product);
        when(cartRepository.findById(cartAddProductDTO.userId().toString())).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenReturn(cart);

        // Act
        cartService.addProductToCart(cartAddProductDTO);

        // Assert
        verify(cartRepository).save(any());
    }

    @Test
    void addProductToCart_ExistingUser_CartUpdated() {
        // Arrange
        CartAddProductDTO cartAddProductDTO = new CartAddProductDTO(UUID.randomUUID(), UUID.randomUUID());
        User user = new User();
        user.setId(cartAddProductDTO.userId());
        Product product = new Product();
        product.setId(cartAddProductDTO.productId());
        Cart cart = new Cart();
        cart.setItems(new HashSet<>());
        cart.getItems().add(new CartItem(product));

        when(userService.findUserById(cartAddProductDTO.userId())).thenReturn(user);
        when(productService.getProductById(cartAddProductDTO.productId())).thenReturn(product);
        when(productService.hasProductInInventory(any(), anyLong())).thenReturn(true);
        when(cartRepository.findById(cartAddProductDTO.userId().toString())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        // Act
        cartService.addProductToCart(cartAddProductDTO);

        // Assert
        verify(cartRepository).save(any());
    }

    @Test
    void updateQuantityProduct_ProductNotFoundInCart_ExceptionThrown() {
        // Arrange
        CartUpdateQuantityDTO cartUpdateQuantityDTO = new CartUpdateQuantityDTO(UUID.randomUUID(), UUID.randomUUID(), 2);
        User user = new User();
        user.setId(cartUpdateQuantityDTO.userId());
        Product product = new Product();
        ProductInventory productInventory = new ProductInventory();
        productInventory.setAmount(1);
        product.setProductInventory(productInventory);
        Cart cart = new Cart();
        cart.setItems(new HashSet<>());

        when(userService.findUserById(cartUpdateQuantityDTO.userId())).thenReturn(user);
        when(productService.getProductById(cartUpdateQuantityDTO.productId())).thenReturn(product);
        when(cartRepository.findById(cartUpdateQuantityDTO.userId().toString())).thenReturn(Optional.of(cart));

        // Act + Assert
        assertThrows(ProductInventoryExceededException.class, () -> cartService.updateQuantityProduct(cartUpdateQuantityDTO));
    }
}