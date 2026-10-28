package dao;

import model.CartItem;
import util.DatabaseConnection;
import java.util.*;
import java.sql.*;

public class CartItemDAO {

    public boolean addToCart(CartItem cartItem) {

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            String sql =
                    "INSERT INTO cart_item " +
                            "(cartId, menuItemId, quantity) " +
                            "VALUES (?, ?, ?)";

            PreparedStatement ps =
                    connection.prepareStatement(sql);

            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getMenuItemId());
            ps.setInt(3, cartItem.getQuantity());

            int rowsAffected =
                    ps.executeUpdate();

            ps.close();
            connection.close();

            return rowsAffected > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    public boolean clearCart(int cartId) {
        try (Connection con = DatabaseConnection.getConnection()) {
            return clearCart(con, cartId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearCart(Connection con, int cartId) throws SQLException {


        String sql = "DELETE FROM cart_item WHERE cartId = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }


    }


    public List<CartItem> getCartItemsByCartId(int cartId) {

        List<CartItem> cartItems = new ArrayList<>();

        String sql = "SELECT cartItemId, cartId, menuItemId, quantity " +
                        "FROM cart_item " +
                        "WHERE cartId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CartItem cartItem = new CartItem(
                        rs.getInt("cartId"),
                        rs.getInt("menuItemId"),
                        rs.getInt("quantity")
                );

                cartItem.setCartItemId(
                        rs.getInt("cartItemId")
                );

                cartItems.add(cartItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    public boolean updateQuantity(int cartItemId, int quantity) {

        String sql = "UPDATE cart_item SET quantity = ? WHERE cartItemId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean removeFromCart(int cartItemId) {

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            String sql =
                    "DELETE FROM cart_item " +
                            "WHERE cartItemId = ?";

            PreparedStatement ps =
                    connection.prepareStatement(sql);

            ps.setInt(1, cartItemId);

            int rowsAffected =
                    ps.executeUpdate();

            ps.close();
            connection.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}