package model;

import java.time.LocalDateTime;

public class Cart {

    private int cartId;
    private int customerId;
    private int restaurantId;
    private LocalDateTime createdAt;

    public Cart() {
    }

    public Cart(int customerId, int restaurantId) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}