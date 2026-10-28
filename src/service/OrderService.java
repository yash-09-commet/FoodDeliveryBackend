package service;

import dao.CartDAO;
import dao.CartItemDAO;
import dao.MenuItemDAO;
import dao.OrderDAO;

import model.Cart;
import model.CartItem;
import model.MenuItem;
import model.Order;
import model.OrderItem;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {


    private CartDAO cartDAO = new CartDAO();
    private CartItemDAO cartItemDAO = new CartItemDAO();
    private MenuItemDAO menuItemDAO = new MenuItemDAO();
    private OrderDAO orderDAO = new OrderDAO();

    public boolean checkout(int customerId) {

        // Step 1: Get the customer's cart
        Cart cart = cartDAO.getCartByCustomerId(customerId);

        if (cart == null) {
            System.out.println("Cart not found.");
            return false;
        }

        // Step 2: Get the items in the cart
        List<CartItem> cartItems =
                cartItemDAO.getCartItemsByCartId(cart.getCartId());

        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return false;
        }

        // Step 3: Validate menu items and calculate total
        double totalAmount = 0.0;

        List<MenuItem> menuItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            MenuItem menuItem =
                    menuItemDAO.getMenuItemById(cartItem.getMenuItemId());

            if (menuItem == null || !menuItem.isAvailable()) {
                System.out.println(
                        "A menu item in your cart is unavailable."
                );
                return false;
            }

            if (cartItem.getQuantity() <= 0) {
                System.out.println(
                        "Invalid quantity for a menu item."
                );
                return false;
            }

            double subtotal =
                    menuItem.getPrice() * cartItem.getQuantity();

            totalAmount += subtotal;

            // Store the menu item so we don't need to fetch it again
            menuItems.add(menuItem);
        }

        // Step 4: Prepare the order
        Order order = new Order();

        order.setCustomerId(customerId);
        order.setRestaurantId(cart.getRestaurantId());
        order.setTotalAmount(totalAmount);
        order.setOrderStatus("PLACED");

        // Step 5: Start the database transaction
        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                // Insert the order and retrieve its generated ID
                int orderId = orderDAO.placeOrder(con, order);

                if (orderId == -1) {
                    throw new SQLException(
                            "Failed to create the order."
                    );
                }

                // Step 6: Insert every order item
                for (int i = 0; i < cartItems.size(); i++) {

                    CartItem cartItem = cartItems.get(i);
                    MenuItem menuItem = menuItems.get(i);

                    OrderItem orderItem = new OrderItem();

                    orderItem.setOrderId(orderId);
                    orderItem.setMenuItemId(
                            cartItem.getMenuItemId()
                    );
                    orderItem.setQuantity(
                            cartItem.getQuantity()
                    );
                    orderItem.setItemPrice(
                            menuItem.getPrice()
                    );

                    boolean itemAdded =
                            orderDAO.addOrderItem(con, orderItem);

                    if (!itemAdded) {
                        throw new SQLException(
                                "Failed to add an order item."
                        );
                    }
                }

                // Step 7: Clear the cart using the same connection
                boolean cartCleared =
                        cartItemDAO.clearCart(con, cart.getCartId());

                if (!cartCleared) {
                    throw new SQLException(
                            "Failed to clear the cart."
                    );
                }

                // Step 8: Commit all database changes
                con.commit();

                System.out.println("Order placed successfully!");
                System.out.println("Order ID: " + orderId);
                System.out.printf(
                        "Total Amount: ₹%.2f%n",
                        totalAmount
                );

                return true;

            } catch (SQLException | RuntimeException e) {

                // Roll back the order, order items, and cart changes
                try {
                    con.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                System.out.println(
                        "Checkout failed. Database changes were rolled back."
                );

                e.printStackTrace();

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error occurred during checkout."
            );

            e.printStackTrace();

            return false;
        }
    }
}
