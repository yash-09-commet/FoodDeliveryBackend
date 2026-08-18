package dao;
import java.sql.*;
import java.util.*;
import model.Customer;
import util.DatabaseConnection;
import dao.*;

public class CustomerDAO {

    public Customer login (String email , String password ){
    try{
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
                Customer customer = new Customer ();
                customer.setCustomerId(resultSet.getInt("customerId"));
                customer.setFullName(resultSet.getString("fullName"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhoneNumber(resultSet.getString("phoneNumber"));
                customer.setPassword(resultSet.getString("password"));
                customer.setAddress(resultSet.getString("address"));
                customer.setRegistrationDate(resultSet.getDate("registrationDate").toLocalDate());
                customer.setActive(resultSet.getBoolean("isActive"));
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return customer;
            }
        else {
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return null;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return null ;
    }


    public void addCustomer(Customer customer){
        try{
            Connection connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO customer (fullName,email,phoneNumber,password,address)VALUES(?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,customer.getFullName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getPassword());
            ps.setString(5,customer.getAddress());
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0 ){
                System.out.println("Customer added successfully !!");
            }
            ps.close();
            connection.close();
        }
        catch(SQLException e ) {
            e.printStackTrace();
        }
    }


    public List<Customer> viewAllCustomers() {
    List<Customer> customers = new ArrayList<>();
    try {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM customer " ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Customer customer = new Customer();
            customer.setCustomerId(resultSet.getInt("customerid"));
            customer.setFullName(resultSet.getString("fullName"));
            customer.setEmail(resultSet.getString("email"));
            customer.setPhoneNumber(resultSet.getString("phoneNumber"));
            customer.setPassword(resultSet.getString("password"));
            customer.setAddress(resultSet.getString("address"));
            customer.setRegistrationDate(resultSet.getDate("registrationDate").toLocalDate());
            customer.setActive(resultSet.getBoolean("isActive"));
            customers.add(customer);
        }
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return customers;

        } catch (SQLException e) {
        e.printStackTrace();
        return customers ;
        }
    }


    public boolean updatePhoneNumber(int customerId , String phoneNumber){
        try{
            Connection connection = DatabaseConnection.getConnection();
            String sql = " UPDATE customer SET phoneNumber = ? WHERE customerId = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,phoneNumber);
            preparedStatement.setInt(2,customerId);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            return rowsAffected > 0 ;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false ;
    }

    public boolean updateAddress(int customerId , String address ){
        try{
            Connection connection = DatabaseConnection.getConnection();
            String sql = " UPDATE customer SET address = ? WHERE customerId = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,address);
            preparedStatement.setInt(2,customerId);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false ;
    }

    public boolean updatePassword(int customerId , String password ){
        try{
            Connection connection = DatabaseConnection.getConnection();
            String sql = " UPDATE customer SET password = ? WHERE customerId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,password);
            preparedStatement.setInt(2,customerId);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean emailExists(String email){
        try{
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT 1 FROM customer WHERE email = ?";
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






}
