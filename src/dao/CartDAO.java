package dao;
import model.Restaurant;
import model.Customer;

import model.Cart;
import util.DatabaseConnection;

import java.sql.*;

public class CartDAO {
    public Cart getCartByCustomerId(int customerId) {

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            String sql =
                    "SELECT * FROM cart WHERE customerId = ?";

            PreparedStatement ps =
                    connection.prepareStatement(sql);

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Cart cart = new Cart();

                cart.setCartId(
                        rs.getInt("cartId")
                );

                cart.setCustomerId(
                        rs.getInt("customerId")
                );

                cart.setRestaurantId(
                        rs.getInt("restaurantId")
                );

                Timestamp timestamp =
                        rs.getTimestamp("createdAt");

                if (timestamp != null) {
                    cart.setCreatedAt(
                            timestamp.toLocalDateTime()
                    );
                }

                rs.close();
                ps.close();
                connection.close();

                return cart;
            }

            rs.close();
            ps.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Cart createCart(
            int customerId,
            int restaurantId) {

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            String sql =
                    "INSERT INTO cart (customerId, restaurantId) " +
                            "VALUES (?, ?)";

            PreparedStatement ps =
                    connection.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    );

            ps.setInt(1, customerId);
            ps.setInt(2, restaurantId);

            int rowsAffected =
                    ps.executeUpdate();

            if (rowsAffected == 0) {
                ps.close();
                connection.close();
                return null;
            }

            ResultSet generatedKeys =
                    ps.getGeneratedKeys();

            if (generatedKeys.next()) {

                int cartId =
                        generatedKeys.getInt(1);

                Cart cart =
                        new Cart(
                                customerId,
                                restaurantId
                        );

                cart.setCartId(cartId);

                generatedKeys.close();
                ps.close();
                connection.close();

                return cart;
            }

            generatedKeys.close();
            ps.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateCartRestaurant(
            int cartId,
            int restaurantId) {

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            String sql =
                    "UPDATE cart SET restaurantId = ? " +
                            "WHERE cartId = ?";

            PreparedStatement ps =
                    connection.prepareStatement(sql);

            ps.setInt(1, restaurantId);
            ps.setInt(2, cartId);

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
