package dao;
import java.sql.Date;

import java.util.*;
import model.Restaurant;
import util.DatabaseConnection;
import java.sql.*;

public class RestaurantDAO {

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
            String sql = " SELECT * FROM restaurant WHERE email = ? AND password = ?";
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

    public boolean emailExists(String email){
        try{
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT 1 FROM restaurant WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                preparedStatement.close();
                resultSet.close();
                connection.close();
                return true ;
            }else{
                preparedStatement.close();
                resultSet.close();
                connection.close();
                return false ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false ;
    }

    public boolean updatePhoneNumber(int restaurantId, String phoneNumber) {

        try {
            Connection connection = DatabaseConnection.getConnection();

            String sql = "UPDATE restaurant SET phoneNumber = ? WHERE restaurantId = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, phoneNumber);
            ps.setInt(2, restaurantId);

            int rowsAffected = ps.executeUpdate();

            ps.close();
            connection.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateAddress(int restaurantId, String address) {

        try {
            Connection connection = DatabaseConnection.getConnection();

            String sql = "UPDATE restaurant SET address = ? WHERE restaurantId = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, address);
            ps.setInt(2, restaurantId);

            int rowsAffected = ps.executeUpdate();

            ps.close();
            connection.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Restaurant> getAllRestaurants() {

        List<Restaurant> restaurants = new ArrayList<>();

        try {

            Connection connection = DatabaseConnection.getConnection();

            String sql = "SELECT restaurantId, restaurantName, address " +
                    "FROM restaurant WHERE isActive = true";

            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setRestaurantId(
                        rs.getInt("restaurantId")
                );

                restaurant.setRestaurantName(
                        rs.getString("restaurantName")
                );

                restaurant.setAddress(
                        rs.getString("address")
                );

                restaurants.add(restaurant);
            }

            rs.close();
            ps.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurants;
    }



}
