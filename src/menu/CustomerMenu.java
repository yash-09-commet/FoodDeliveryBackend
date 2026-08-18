package menu;

import java.util.List;
import java.util.Scanner;
import dao.*;
import model.*;
import validation.CommonValidation;



public class CustomerMenu {


    public static void signup(Scanner sc, CustomerDAO customerDAO) {


        String fullName = CommonValidation.getValidName(sc);

        String email = CommonValidation.getValidEmail(sc);

        while (customerDAO.emailExists(email)) {
            System.out.println("Email already registered.");
            email = CommonValidation.getValidEmail(sc);
        }


        String phoneNumber = CommonValidation.getValidPhoneNumber(sc);

        String password = CommonValidation.getValidPassword(sc);

        String address = CommonValidation.getValidAddress(sc);

        Customer customer = new Customer(fullName, email, phoneNumber, password, address);
        customerDAO.addCustomer(customer);

    }

    public static void login(
            Scanner sc, CustomerDAO customerDAO ,
            RestaurantDAO restaurantDAO, MenuItemDAO menuItemDAO ,CartDAO cartDAO , CartItemDAO cartItemDAO ) {

        System.out.print("Enter Email: ");
        String emailid = sc.nextLine();

        System.out.print("Enter Password: ");
        String Password = sc.nextLine();

        Customer loggedInCustomer = customerDAO.login(emailid, Password);

        if (loggedInCustomer != null) {
            System.out.println(" Login Successful ! ");
            System.out.println("WELCOME " + loggedInCustomer.getFullName());
            showCustomerDashboard(sc, customerDAO, loggedInCustomer, restaurantDAO, menuItemDAO, cartDAO, cartItemDAO);
        } else {
            System.out.println(" Invalid email or password ");
        }
    }

    public static void browseRestaurants(Scanner sc, RestaurantDAO restaurantDAO,
                                         MenuItemDAO menuItemDAO, CartDAO cartDAO,
                                         CartItemDAO cartItemDAO, Customer loggedInCustomer) {

        System.out.println();
        System.out.println("========== RESTAURANTS ==========");

        List<Restaurant> restaurants =
                restaurantDAO.getAllRestaurants();

        if (restaurants.isEmpty()) {
            System.out.println("No restaurants are currently available.");
            return;
        }

        for (int i = 0; i < restaurants.size(); i++) {

            Restaurant restaurant = restaurants.get(i);

            System.out.println(
                    (i + 1) + ". " +
                            restaurant.getRestaurantName() +
                            " - " +
                            restaurant.getAddress()
            );
        }

        System.out.println("0. Back");

        System.out.print("Select a restaurant: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 0) {
            return;
        }

        if (choice < 1 || choice > restaurants.size()) {
            System.out.println("Invalid restaurant selection.");
            return;
        }

        Restaurant selectedRestaurant =
                restaurants.get(choice - 1);

        System.out.println();
        System.out.println(
                "Selected Restaurant: " +
                        selectedRestaurant.getRestaurantName()
        );

        MenuItemMenu.viewCustomerMenu(sc, menuItemDAO, selectedRestaurant, cartDAO, cartItemDAO, loggedInCustomer);
    }

    public static void addItemToCart(
            Scanner sc,
            CartDAO cartDAO,
            CartItemDAO cartItemDAO,
            Customer loggedInCustomer,
            Restaurant selectedRestaurant,
            MenuItem selectedMenuItem , int quantity  ) {

        int customerId = loggedInCustomer.getCustomerId();
        int restaurantId = selectedRestaurant.getRestaurantId();

        // Get customer's existing cart
        Cart cart = cartDAO.getCartByCustomerId(customerId);

        // No cart exists
        if (cart == null) {

            cart = cartDAO.createCart(
                    customerId,
                    restaurantId
            );

            if (cart == null) {
                System.out.println("Unable to create cart.");
                return;
            }
        }

        // Cart belongs to a different restaurant
        else if (cart.getRestaurantId() != restaurantId) {

            System.out.println();
            System.out.println(
                    "Your cart contains items from another restaurant."
            );

            System.out.println(
                    "Current restaurant: " +
                            cart.getRestaurantId()
            );

            System.out.println(
                    "Selected restaurant: " +
                            restaurantId
            );

            System.out.println();
            System.out.println("1. Clear current cart and add this item");
            System.out.println("2. Keep current cart");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {

                boolean cleared =
                        cartItemDAO.clearCart(
                                cart.getCartId()
                        );

                if (!cleared) {
                    System.out.println(
                            "Unable to clear current cart."
                    );
                    return;
                }

                boolean restaurantUpdated =
                        cartDAO.updateCartRestaurant(
                                cart.getCartId(),
                                restaurantId
                        );

                if (!restaurantUpdated) {
                    System.out.println(
                            "Unable to change restaurant for cart."
                    );
                    return;
                }

                // Update our Java object as well
                cart.setRestaurantId(restaurantId);

            } else if (choice == 2) {

                System.out.println(
                        "Current cart has been kept."
                );

                return;

            } else {

                System.out.println(
                        "Invalid choice. Item was not added."
                );

                return;
            }
        }

        // Ask quantity
        CartItem cartItem =
                new CartItem(
                        cart.getCartId(),
                        selectedMenuItem.getMenuItemId(),
                        quantity
                );

        boolean added =
                cartItemDAO.addToCart(cartItem);

        if (added) {

            System.out.println(
                    selectedMenuItem.getItemName() +
                            " added to cart successfully!"
            );

        } else {

            System.out.println(
                    "Unable to add item to cart."
            );
        }
    }




    public static void mainMenu(Scanner sc, CustomerDAO customerDAO) {
        RestaurantDAO restaurantDAO = new RestaurantDAO();
        MenuItemDAO menuItemDAO = new MenuItemDAO();
        CartDAO cartDAO = new CartDAO();
        CartItemDAO cartItemDAO = new CartItemDAO();

        while (true) {

            System.out.println();
            System.out.println("========== CUSTOMER ==========");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Back");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    signup(sc, customerDAO);
                    break;

                case 2:
                    login(sc, customerDAO, restaurantDAO , menuItemDAO , cartDAO , cartItemDAO);
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    ;

    public static void showCustomerDashboard(
            Scanner sc, CustomerDAO customerDAO,
            Customer loggedInCustomer, RestaurantDAO restaurantDAO,
            MenuItemDAO menuItemDAO, CartDAO cartDAO , CartItemDAO cartItemDAO) {

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("=========CUSTOMER DASHBOARD==========");
            System.out.println("WELCOME " + loggedInCustomer.getFullName());
            System.out.println("1. View My Profile");
            System.out.println("2. Update Phone Number");
            System.out.println("3. Update Address");
            System.out.println("4. Change Password");
            System.out.println("5. Browse Restaurant");
            System.out.println("6. My Cart");
            System.out.println("7. Logout");

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
                    String newPhoneNumber =
                            CommonValidation.getValidPhoneNumber(sc);
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
                    String newAddress =
                            CommonValidation.getValidAddress(sc);
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
                        String newpassword = CommonValidation.getValidPassword(sc);
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
                    browseRestaurants(
                            sc,
                            restaurantDAO,
                            menuItemDAO,
                            cartDAO,
                            cartItemDAO,
                            loggedInCustomer
                    );
                    break;
                case 6:
                    cartMenu(
                            sc,
                            cartDAO,
                            cartItemDAO,
                            menuItemDAO,
                            loggedInCustomer
                    );
                    break;
                case 7:
                    loggedIn = false;
                    System.out.println(" Logged out successfuly !");
                    break;
                default:
                    System.out.println("Invalid choice !");
            }
        }
    }


    public static void showMyCart(
            CartDAO cartDAO,
            CartItemDAO cartItemDAO,
            MenuItemDAO menuItemDAO,
            Customer loggedInCustomer) {

        System.out.println();
        System.out.println("========== MY CART ==========");

        int customerId =
                loggedInCustomer.getCustomerId();

        // Find customer's cart
        Cart cart =
                cartDAO.getCartByCustomerId(customerId);

        if (cart == null) {

            System.out.println("Your cart is empty.");
            return;
        }

        // Get items inside cart
        List<CartItem> cartItems =
                cartItemDAO.getCartItemsByCartId(
                        cart.getCartId()
                );

        if (cartItems.isEmpty()) {

            System.out.println("Your cart is empty.");
            return;
        }

        double total = 0;

        System.out.println(
                "Restaurant ID: " +
                        cart.getRestaurantId()
        );

        System.out.println("--------------------------------");

        for (int i = 0; i < cartItems.size(); i++) {

            CartItem cartItem =
                    cartItems.get(i);

            MenuItem menuItem =
                    menuItemDAO.getMenuItemById(
                            cartItem.getMenuItemId()
                    );

            if (menuItem == null) {
                continue;
            }

            double itemTotal =
                    menuItem.getPrice() *
                            cartItem.getQuantity();

            total += itemTotal;

            System.out.println(
                    (i + 1) + ". " +
                            menuItem.getItemName()
            );

            System.out.println(
                    "   Price    : ₹" +
                            menuItem.getPrice()
            );

            System.out.println(
                    "   Quantity : " +
                            cartItem.getQuantity()
            );

            System.out.println(
                    "   Total    : ₹" +
                            itemTotal
            );

            System.out.println("--------------------------------");
        }

        System.out.println(
                "GRAND TOTAL : ₹" + total
        );
    }

    public static void updateCartItemQuantity(
            Scanner sc,
            CartDAO cartDAO,
            CartItemDAO cartItemDAO,
            MenuItemDAO menuItemDAO,
            Customer loggedInCustomer) {

        System.out.println();
        System.out.println("========== UPDATE CART QUANTITY ==========");

        Cart cart =
                cartDAO.getCartByCustomerId(
                        loggedInCustomer.getCustomerId()
                );

        if (cart == null) {
            System.out.println("Your cart is empty.");
            return;
        }

        List<CartItem> cartItems =
                cartItemDAO.getCartItemsByCartId(
                        cart.getCartId()
                );

        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        // Display cart items
        for (int i = 0; i < cartItems.size(); i++) {

            CartItem cartItem = cartItems.get(i);

            MenuItem menuItem =
                    menuItemDAO.getMenuItemById(
                            cartItem.getMenuItemId()
                    );

            if (menuItem == null) {
                continue;
            }

            System.out.println(
                    (i + 1) + ". " +
                            menuItem.getItemName() +
                            " - ₹" +
                            menuItem.getPrice() +
                            " × " +
                            cartItem.getQuantity()
            );
        }

        System.out.print("Select item to update: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > cartItems.size()) {
            System.out.println("Invalid item selection.");
            return;
        }

        CartItem selectedItem =
                cartItems.get(choice - 1);

        MenuItem menuItem =
                menuItemDAO.getMenuItemById(
                        selectedItem.getMenuItemId()
                );

        System.out.println(
                "Selected: " +
                        menuItem.getItemName()
        );

        System.out.println(
                "Current quantity: " +
                        selectedItem.getQuantity()
        );

        System.out.print("Enter new quantity: ");

        int quantity = sc.nextInt();
        sc.nextLine();

        if (quantity <= 0) {
            System.out.println(
                    "Quantity must be greater than 0."
            );
            return;
        }

        boolean updated =
                cartItemDAO.updateQuantity(
                        selectedItem.getCartItemId(),
                        quantity
                );

        if (updated) {
            System.out.println(
                    "Quantity updated successfully!"
            );
        } else {
            System.out.println(
                    "Failed to update quantity."
            );
        }
    }




    public static void clearCustomerCart(
            Scanner sc,
            CartDAO cartDAO,
            CartItemDAO cartItemDAO,
            Customer loggedInCustomer) {

        System.out.println();
        System.out.println("========== CLEAR CART ==========");

        Cart cart =
                cartDAO.getCartByCustomerId(
                        loggedInCustomer.getCustomerId()
                );

        if (cart == null) {
            System.out.println("Your cart is empty.");
            return;
        }

        List<CartItem> cartItems =
                cartItemDAO.getCartItemsByCartId(
                        cart.getCartId()
                );

        if (cartItems.isEmpty()) {
            System.out.println("Your cart is already empty.");
            return;
        }

        System.out.print(
                "Are you sure you want to clear your cart? (Y/N): "
        );

        String confirmation = sc.nextLine();

        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Clear cart cancelled.");
            return;
        }

        boolean cleared =
                cartItemDAO.clearCart(
                        cart.getCartId()
                );

        if (cleared) {
            System.out.println("Cart cleared successfully!");
        } else {
            System.out.println("Failed to clear cart.");
        }
    }

    public static void cartMenu(
            Scanner sc,
            CartDAO cartDAO,
            CartItemDAO cartItemDAO,
            MenuItemDAO menuItemDAO,
            Customer loggedInCustomer) {

        while (true) {

            System.out.println();
            System.out.println("========== MY CART ==========");
            System.out.println("1. View Cart");
            System.out.println("2. Update Quantity");
            System.out.println("3. Clear Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Back");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    showMyCart(
                            cartDAO,
                            cartItemDAO,
                            menuItemDAO,
                            loggedInCustomer
                    );
                    break;

                case 2:
                    updateCartItemQuantity(
                            sc,
                            cartDAO,
                            cartItemDAO,
                            menuItemDAO,
                            loggedInCustomer
                    );
                    break;

                case 3:
                    clearCustomerCart(
                            sc,
                            cartDAO,
                            cartItemDAO,
                            loggedInCustomer
                    );
                    break;

                case 4:
                    // checkout(cartDAO, cartItemDAO, menuItemDAO, loggedInCustomer);
                    System.out.println("Checkout - Coming next");
                    break;

                case 5:
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }



}
