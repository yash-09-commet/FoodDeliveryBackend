package dao;

import model.Order;
import model.OrderItem;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseConnection;
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

        public List<Order> getOrdersByCustomerId(int customerId) {

            List<Order> orders = new ArrayList<>();

            String sql = "SELECT * FROM orders WHERE customerId = ? ORDER BY orderDate DESC";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, customerId);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    Order order = new Order();

                    order.setOrderId(rs.getInt("orderId"));
                    order.setCustomerId(rs.getInt("customerId"));
                    order.setRestaurantId(rs.getInt("restaurantId"));
                    order.setTotalAmount(rs.getDouble("totalAmount"));
                    order.setOrderStatus(rs.getString("orderStatus"));

                    order.setOrderDate(
                            rs.getTimestamp("orderDate").toLocalDateTime()
                    );

                    orders.add(order);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return orders;
        }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {

        List<OrderItem> orderItems = new ArrayList<>();

        String sql = "SELECT * FROM order_item WHERE orderId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                OrderItem orderItem = new OrderItem();

                orderItem.setOrderItemId(rs.getInt("orderItemId"));
                orderItem.setOrderId(rs.getInt("orderId"));
                orderItem.setMenuItemId(rs.getInt("menuItemId"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setItemPrice(rs.getDouble("itemPrice"));

                orderItems.add(orderItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    public boolean cancelOrder(int orderId, int customerId) {

        String sql = "UPDATE orders SET orderStatus = 'CANCELLED' " +
                "WHERE orderId = ? AND customerId = ? " +
                "AND orderStatus IN ('PLACED', 'CONFIRMED')";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, customerId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

}


