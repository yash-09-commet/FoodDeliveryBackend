package dao;
import util.DatabaseConnection;
import model.Order;
import java.sql.*;
public class OrderDAO {
    public int placeOrder(Order order) {
        String sql = "INSERT INTO orders (customerId, restaurantId, totalAmount, orderStatus) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection()) {

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
