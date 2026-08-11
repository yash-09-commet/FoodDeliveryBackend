package menu;
import java.util.Scanner;
import dao.CustomerDAO;
import model.Customer;
import validation.customerValidation;


public class CustomerMenu {

    public static void signup(Scanner sc, CustomerDAO customerDAO) {


        String fullName = customerValidation.getValidName(sc);

        String email = customerValidation.getValidEmail(sc, customerDAO);

        String phoneNumber = customerValidation.getValidPhoneNumber(sc);

        String password = customerValidation.getValidPassword(sc);

        String address = customerValidation.getValidAddress(sc);

        Customer customer = new Customer(fullName, email, phoneNumber, password, address);
        customerDAO.addCustomer(customer);

    }


    public static void login(Scanner sc, CustomerDAO customerDAO) {

        System.out.print("Enter Email: ");
        String emailid = sc.nextLine();

        System.out.print("Enter Password: ");
        String Password = sc.nextLine();

        Customer loggedInCustomer = customerDAO.login(emailid, Password);

        if (loggedInCustomer != null) {
            System.out.println(" Login Successful ! ");
            System.out.println("WELCOME " + loggedInCustomer.getFullName());
            showCustomerDashboard(sc, customerDAO, loggedInCustomer);
        } else {
            System.out.println(" Invalid email or password ");
        }
    }


    public static void showCustomerDashboard(Scanner sc, CustomerDAO customerDAO, Customer loggedInCustomer) {

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("=========CUSTOMER DASHBOARD==========");
            System.out.println("WELCOME " + loggedInCustomer.getFullName());
            System.out.println("1. View My Profile");
            System.out.println("2. Update Phone Number");
            System.out.println("3. Update Address");
            System.out.println("4. Change Password");
            System.out.println("5. Logout");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n========== MY PROFILE ==========");
                    System.out.println("Customer ID       : " + loggedInCustomer.getCustomerId());
                    System.out.println("Full Name         : " + loggedInCustomer.getFullName());
                    System.out.println("Email             : " + loggedInCustomer.getEmail());
                    System.out.println("Phone Number      : " + loggedInCustomer.getPhoneNumber());
                    System.out.println("Address           : " + loggedInCustomer.getAddress());
                    System.out.println("Registration Date : " + loggedInCustomer.getRegistrationDate());
                    System.out.println("Account Status    : "
                            + (loggedInCustomer.isActive() ? "Active" : "Inactive"));
                    break;
                case 2:
                    System.out.println(" Enter new phone number ");
                    String newPhoneNumber = sc.nextLine();
                    boolean updated = customerDAO.updatePhoneNumber(loggedInCustomer.getCustomerId(), newPhoneNumber);
                    if(updated){

                        loggedInCustomer.setPhoneNumber(newPhoneNumber);

                        System.out.println("Phone Number Updated Successfully!");

                    }
                    else{

                        System.out.println("Something went wrong!");

                    }
                    break;
                case 3:
                    System.out.println(" Enter new address ");
                    String newAddress = sc.nextLine();
                    boolean updateaddress = customerDAO.updateAddress(loggedInCustomer.getCustomerId(), newAddress);
                    if(updateaddress){

                        loggedInCustomer.setAddress(newAddress);

                        System.out.println("Address Updated Successfully!");

                    }
                    else{

                        System.out.println("Something went wrong!");

                    }

                    break;
                case 4:
                    System.out.println(" Enter the current password ");
                    String currentPassword = sc.nextLine();
                    if(currentPassword.equals(loggedInCustomer.getPassword()))
                    {
                        System.out.println("Enter the new password:");
                        String newpassword = customerValidation.getValidPassword(sc);
                        boolean updatepassword = customerDAO.updatePassword(loggedInCustomer.getCustomerId(),newpassword);
                        if(updatepassword){
                            loggedInCustomer.setPassword(newpassword);
                            System.out.println("Password updated successfully !");

                        }
                        else{
                            System.out.println(" Something went wrong ");
                        }
                    }else{
                        System.out.println("enter the correct current password to update the password ");
                    }
                    break;
                case 5:
                    loggedIn = false;
                    System.out.println(" Logged out successfuly !");
                    break;
                default:
                    System.out.println("Invalid choice !");
            }
        }
    }
}
