package main;
import model.Customer;
import dao.CustomerDAO;
public class Main {
    public static void main(String[] args) {
    Customer customer = new Customer(
            "Yash Rastogi","ram@gmail.com",
            "9992872562","ram.34.santa@","Delhi"
    );
    CustomerDAO customerDAO = new CustomerDAO();
    customerDAO.addCustomer(customer);
    }
}
