package menu;

import dao.MenuItemDAO;
import dao.RestaurantDAO;
import model.Restaurant;
import validation.CommonValidation;
import java.util.Scanner;

public class RestaurantMenu {

    public static void mainMenu(Scanner sc, RestaurantDAO restaurantDAO) {
        MenuItemDAO menuItemDAO = new MenuItemDAO();

        while (true) {

            System.out.println();
            System.out.println("========== Restaurant ==========");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    signup(sc, restaurantDAO);
                    break;

                case 2:
                    login(sc, restaurantDAO , menuItemDAO);
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public static void signup(Scanner sc, RestaurantDAO restaurantDAO) {

        System.out.println("========== RESTAURANT SIGN UP ==========");

        String restaurantName = CommonValidation.getValidName(sc);
        String email = CommonValidation.getValidEmail(sc);

        while (restaurantDAO.emailExists(email)) {
            System.out.println("Email already registered. Please enter another email.");
            email = CommonValidation.getValidEmail(sc);
        }

        String phoneNumber = CommonValidation.getValidPhoneNumber(sc);

        String password = CommonValidation.getValidPassword(sc);

        String address = CommonValidation.getValidAddress(sc);

        Restaurant restaurant = new Restaurant(
                restaurantName,
                email,
                phoneNumber,
                password,
                address
        );

        restaurantDAO.addRestaurant(restaurant);
    }

    public static void login( Scanner sc , RestaurantDAO restaurantDAO , MenuItemDAO menuItemDAO){
        System.out.print("Enter Email: ");
        String emailid = sc.nextLine();

        System.out.print("Enter Password: ");
        String Password = sc.nextLine();

        Restaurant loggedInRestaurant  = restaurantDAO.login(emailid, Password);

        if (loggedInRestaurant != null) {
            System.out.println(" Login Successful ! ");
            System.out.println("WELCOME " + loggedInRestaurant.getRestaurantName());
            showRestaurantDashboard(sc, restaurantDAO, menuItemDAO, loggedInRestaurant);
        } else {
            System.out.println(" Invalid email or password ");
        }
    }


    public static void showRestaurantDashboard(Scanner sc, RestaurantDAO restaurantDAO,MenuItemDAO menuItemDAO, Restaurant loggedInRestaurant ) {
        while(true){
            System.out.println();
            System.out.println("========== RESTAURANT DASHBOARD ==========");
            System.out.println("Welcome " + loggedInRestaurant.getRestaurantName());
            System.out.println("1. View My Profile");
            System.out.println("2. Update Restaurant Details");
            System.out.println("3. Manage Menu");
            System.out.println("4. Logout");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    showRestaurantProfile(loggedInRestaurant);
                    break;

                case 2:
                    updateRestaurantDetails(sc, restaurantDAO, loggedInRestaurant);
                    break;

                case 3:
                    MenuItemMenu.manageMenu(sc, menuItemDAO, loggedInRestaurant);
                    break;

                case 4:
                    System.out.println("Logged out successfully!");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void showRestaurantProfile(Restaurant restaurant) {

        System.out.println();
        System.out.println("========== RESTAURANT PROFILE ==========");

        System.out.println("Restaurant ID       : " + restaurant.getRestaurantId());
        System.out.println("Restaurant Name     : " + restaurant.getRestaurantName());
        System.out.println("Email               : " + restaurant.getEmail());
        System.out.println("Phone Number        : " + restaurant.getPhoneNumber());
        System.out.println("Address             : " + restaurant.getAddress());
        System.out.println("Registration Date   : " + restaurant.getRegistrationDate());
        System.out.println("Account Status      : " +
                (restaurant.isActive() ? "Active" : "Inactive"));

    }

    public static void updateRestaurantDetails(Scanner sc, RestaurantDAO restaurantDAO, Restaurant loggedInRestaurant) {

        while (true) {

            System.out.println();
            System.out.println("========== UPDATE RESTAURANT DETAILS ==========");
            System.out.println("1. Update Phone Number");
            System.out.println("2. Update Address");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    String phoneNumber =
                            CommonValidation.getValidPhoneNumber(sc);

                    boolean phoneUpdated = restaurantDAO.updatePhoneNumber(
                            loggedInRestaurant.getRestaurantId(),
                            phoneNumber
                    );

                    if (phoneUpdated) {
                        loggedInRestaurant.setPhoneNumber(phoneNumber);
                        System.out.println("Phone number updated successfully!");
                    } else {
                        System.out.println("Failed to update phone number.");
                    }
                    break;

                case 2:
                    String address =
                            CommonValidation.getValidAddress(sc);

                    boolean addressUpdated = restaurantDAO.updateAddress(
                            loggedInRestaurant.getRestaurantId(),
                            address
                    );

                    if (addressUpdated) {
                        loggedInRestaurant.setAddress(address);
                        System.out.println("Address updated successfully!");
                    } else {
                        System.out.println("Failed to update address.");
                    }
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


}
