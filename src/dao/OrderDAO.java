package dao;

import model.Order;
import model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDAO {

    public int placeOrder(Connection con, Order order) throws SQLException {

        String sql = "INSERT INTO orders (customerId, restaurantId, totalAmount, orderStatus) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getCustomerId());
            ps.setInt(2, order.getRestaurantId());
            ps.setDouble(3, order.getTotalAmount());
            ps.setString(4, order.getOrderStatus());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

            return -1;
        }
    }

    public boolean addOrderItem(Connection con, OrderItem orderItem) throws SQLException {

        String sql = "INSERT INTO order_item (orderId, menuItemId, quantity, itemPrice) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuItemId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setDouble(4, orderItem.getItemPrice());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }
    }

}
