package dao;
import model.Customer;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
public class CustomerDAO {
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
        catch(SQLException e ){
            e.printStackTrace();
        }

    }
}
