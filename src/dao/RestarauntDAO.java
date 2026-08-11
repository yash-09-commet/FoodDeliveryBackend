package dao;
import java.sql.Date;
import java.util.*;
import model.Restaurant;
import util.DatabaseConnection;
import java.sql.*;

public class RestarauntDAO {

    public void addRestaurant(Restaurant restaurant) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = " INSERT INTO restaurant " +
                    "(restaurantName, email, phoneNumber, password, address, registrationDate, isActive) " +
                    "VALUES(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, restaurant.getRestaurantName());
            preparedStatement.setString(2, restaurant.getEmail());
            preparedStatement.setString(3, restaurant.getPhoneNumber());
            preparedStatement.setString(4, restaurant.getPassword());
            preparedStatement.setString(5, restaurant.getAddress());
            preparedStatement.setDate(6, Date.valueOf(restaurant.getRegistrationDate()));
            preparedStatement.setBoolean(7, restaurant.isActive());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Restaurant added successfully!");
            }
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Restaurant login(String email, String password) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = " SELECT * FROM restaurant WHERE email = ? , password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setRestaurantId(resultSet.getInt("restaurantId"));
                restaurant.setRestaurantName(resultSet.getString("restaurantName"));
                restaurant.setEmail(resultSet.getString("email"));
                restaurant.setPhoneNumber(resultSet.getString("phoneNumber"));
                restaurant.setPassword(resultSet.getString("password"));
                restaurant.setAddress(resultSet.getString("address"));
                restaurant.setRegistrationDate(resultSet.getDate("registrationDate").toLocalDate());
                restaurant.setActive(resultSet.getBoolean("isActive"));

                resultSet.close();
                preparedStatement.close();
                connection.close();

                return restaurant;
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
