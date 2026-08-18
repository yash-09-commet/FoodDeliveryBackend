package menu;

import dao.CartDAO;
import dao.CartItemDAO;
import model.Customer;
import java.util.List;
import dao.MenuItemDAO;
import model.Restaurant;
import model.MenuItem;
import java.util.Scanner;
import validation.CommonValidation;

public class MenuItemMenu {

    public static void manageMenu(
            Scanner sc,
            MenuItemDAO menuItemDAO,
            Restaurant loggedInRestaurant) {

        while (true) {

            System.out.println();
            System.out.println("========== MANAGE MENU ==========");
            System.out.println("1. Add Menu Item");
            System.out.println("2. View Menu");
            System.out.println("3. Update Menu Item");
            System.out.println("4. Delete Menu Item");
            System.out.println("5. Back");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addMenuItem(sc, menuItemDAO, loggedInRestaurant);
                    break;

                case 2:
                    viewMenu(menuItemDAO, loggedInRestaurant);
                    break;

                case 3:
                    updateMenuItem(sc, menuItemDAO, loggedInRestaurant);
                    break;

                case 4:
                    deleteMenuItem(sc, menuItemDAO, loggedInRestaurant);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void addMenuItem(
            Scanner sc,
            MenuItemDAO menuItemDAO,
            Restaurant loggedInRestaurant) {

        System.out.println();
        System.out.println("========== ADD MENU ITEM ==========");

        System.out.print("Enter Item Name: ");
        String itemName = CommonValidation.getValidName(sc);

        String description = CommonValidation.getValidNonEmptyText(sc, "Enter Description");

        System.out.print("Enter Price: ");
        double price;

        while (true) {

            System.out.print("Enter Price: ");

            if (!sc.hasNextDouble()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            price = sc.nextDouble();
            sc.nextLine();

            if (price <= 0) {
                System.out.println("Price must be greater than 0.");
                continue;
            }

            break;
        }

        String category = CommonValidation.getValidNonEmptyText(sc, "Enter Category");
        String foodType;

        while (true) {

            System.out.println("Select Food Type:");
            System.out.println("1. VEG");
            System.out.println("2. NON-VEG");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                foodType = "VEG";
                break;
            }

            if (choice == 2) {
                foodType = "NON_VEG";
                break;
            }

            System.out.println("Invalid choice. Please try again.");
        }

        MenuItem menuItem = new MenuItem(loggedInRestaurant.getRestaurantId(), itemName, description, price, category,foodType);

        boolean added = menuItemDAO.addMenuItem(menuItem);

        if (added) {
            System.out.println("Menu item added successfully!");
        } else {
            System.out.println("Failed to add menu item.");
        }
    }

    public static void viewMenu(
            MenuItemDAO menuItemDAO,
            Restaurant loggedInRestaurant) {

        System.out.println();
        System.out.println("========== MY MENU ==========");

        List<MenuItem> menuItems =
                menuItemDAO.getMenuItemsByRestaurantId(
                        loggedInRestaurant.getRestaurantId()
                );

        if (menuItems.isEmpty()) {
            System.out.println("No menu items found.");
            return;
        }

        for (MenuItem menuItem : menuItems) {

            System.out.println("--------------------------------");
            System.out.println("ID          : " + menuItem.getMenuItemId());
            System.out.println("Item        : " + menuItem.getItemName());
            System.out.println("Description : " + menuItem.getDescription());
            System.out.println("Price       : ₹" + menuItem.getPrice());
            System.out.println("Category    : " + menuItem.getCategory());
            System.out.println("Food Type   : " + menuItem.getFoodType());
            System.out.println("Available   : " +
                    (menuItem.isAvailable() ? "Yes" : "No"));
        }

        System.out.println("--------------------------------");
    }


    public static void updateMenuItem(Scanner sc, MenuItemDAO menuItemDAO, Restaurant loggedInRestaurant) {

        System.out.println();
        System.out.println("========== UPDATE MENU ITEM ==========");

        List<MenuItem> menuItems = menuItemDAO.getMenuItemsByRestaurantId(loggedInRestaurant.getRestaurantId());

        if (menuItems.isEmpty()) {
            System.out.println("You don't have any menu items.");
            return;
        }

        // Display existing menu items
        for (int i = 0; i < menuItems.size(); i++) {

            MenuItem item = menuItems.get(i);

            System.out.println((i + 1) + ". " + item.getItemName() + " - ₹" + item.getPrice() + " - " + item.getFoodType());
        }

        System.out.print("Select the item you want to update: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > menuItems.size()) {
            System.out.println("Invalid item selection.");
            return;
        }

        MenuItem selectedItem = menuItems.get(choice - 1);

        System.out.println();
        System.out.println("Updating: " + selectedItem.getItemName());

        System.out.println("Enter New Item Name:");
        String itemName = CommonValidation.getValidName(sc);

        System.out.print("Enter New Description: ");
        String description = CommonValidation.getValidNonEmptyText(sc ,"Enter Description");

        System.out.print("Enter New Price: ");
            double price ;
        while (true) {

            System.out.print("Enter Price: ");

            if (!sc.hasNextDouble()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            price = sc.nextDouble();
            sc.nextLine();

            if (price <= 0) {
                System.out.println("Price must be greater than 0.");
                continue;
            }

            break;
        }


        String category = CommonValidation.getValidNonEmptyText(sc, "Enter Category");

        String foodType;

        while (true) {

            System.out.println("Select Food Type:");
            System.out.println("1. VEG");
            System.out.println("2. NON-VEG");

            System.out.print("Enter choice: ");
            int foodChoice = sc.nextInt();
            sc.nextLine();

            if (foodChoice == 1) {
                foodType = "VEG";
                break;
            }

            if (foodChoice == 2) {
                foodType = "NON_VEG";
                break;
            }

            System.out.println("Invalid choice. Please try again.");
        }

        System.out.println("Is this item available?");
        System.out.println("1. Yes");
        System.out.println("2. No");

        System.out.print("Enter choice: ");
        int availabilityChoice = sc.nextInt();
        sc.nextLine();

        boolean isAvailable;

        if (availabilityChoice == 1) {
            isAvailable = true;
        } else if (availabilityChoice == 2) {
            isAvailable = false;
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        selectedItem.setItemName(itemName);
        selectedItem.setDescription(description);
        selectedItem.setPrice(price);
        selectedItem.setCategory(category);
        selectedItem.setFoodType(foodType);
        selectedItem.setAvailable(isAvailable);

        boolean updated = menuItemDAO.updateMenuItem(selectedItem);

        if (updated) {
            System.out.println("Menu item updated successfully!");
        } else {
            System.out.println("Failed to update menu item.");
        }
    }

    public static void deleteMenuItem(
            Scanner sc,
            MenuItemDAO menuItemDAO,
            Restaurant loggedInRestaurant) {

        System.out.println();
        System.out.println("========== DELETE MENU ITEM ==========");

        List<MenuItem> menuItems =
                menuItemDAO.getMenuItemsByRestaurantId(
                        loggedInRestaurant.getRestaurantId()
                );

        if (menuItems.isEmpty()) {
            System.out.println("You don't have any menu items.");
            return;
        }

        // Display menu items
        for (int i = 0; i < menuItems.size(); i++) {

            MenuItem item = menuItems.get(i);

            System.out.println(
                    (i + 1) + ". " +
                            item.getItemName() +
                            " - ₹" +
                            item.getPrice() +
                            " - " +
                            item.getFoodType()
            );
        }

        System.out.print("Select the item you want to delete: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > menuItems.size()) {
            System.out.println("Invalid item selection.");
            return;
        }

        MenuItem selectedItem = menuItems.get(choice - 1);

        System.out.println();
        System.out.println("You selected: " + selectedItem.getItemName());

        System.out.print("Are you sure you want to delete this item? (Y/N): ");
        String confirmation = sc.nextLine();

        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Delete cancelled.");
            return;
        }

        boolean deleted = menuItemDAO.deleteMenuItem(
                selectedItem.getMenuItemId(),
                loggedInRestaurant.getRestaurantId()
        );

        if (deleted) {
            System.out.println("Menu item deleted successfully!");
        } else {
            System.out.println("Failed to delete menu item.");
        }
    }

    public static void viewCustomerMenu(
            Scanner sc,
            MenuItemDAO menuItemDAO,
            Restaurant selectedRestaurant,
            CartDAO cartDAO,
            CartItemDAO cartItemDAO,
            Customer loggedInCustomer) {

        System.out.println();
        System.out.println(
                "========== " +
                        selectedRestaurant.getRestaurantName() +
                        " MENU =========="
        );

        List<MenuItem> menuItems =
                menuItemDAO.getMenuItemsByRestaurantId(
                        selectedRestaurant.getRestaurantId()
                );

        if (menuItems.isEmpty()) {
            System.out.println("No menu items are currently available.");
            System.out.println("--------------------------------");
            return;
        }

        // Create a separate list containing ONLY available items
        List<MenuItem> availableItems = new java.util.ArrayList<>();

        for (MenuItem menuItem : menuItems) {

            if (menuItem.isAvailable()) {
                availableItems.add(menuItem);
            }
        }

        // If restaurant has menu items but none are available
        if (availableItems.isEmpty()) {

            System.out.println(
                    "No menu items are currently available."
            );

            System.out.println("--------------------------------");
            return;
        }

        // Display ONLY available items
        for (int i = 0; i < availableItems.size(); i++) {

            MenuItem menuItem = availableItems.get(i);

            System.out.println("--------------------------------");
            System.out.println(
                    (i + 1) + ". " +
                            menuItem.getItemName()
            );

            System.out.println(
                    "   Description : " +
                            menuItem.getDescription()
            );

            System.out.println(
                    "   Price       : ₹" +
                            menuItem.getPrice()
            );

            System.out.println(
                    "   Category    : " +
                            menuItem.getCategory()
            );

            System.out.println(
                    "   Food Type   : " +
                            menuItem.getFoodType()
            );
        }

        System.out.println("--------------------------------");
        System.out.println("0. Done");

        // Keep allowing customer to select multiple items
        while (true) {

            System.out.print("Select an item: ");

            int choice = sc.nextInt();
            sc.nextLine();

            // Done
            if (choice == 0) {
                return;
            }

            // Validate selection against AVAILABLE items
            if (choice < 1 || choice > availableItems.size()) {

                System.out.println(
                        "Invalid item selection. Please try again."
                );

                continue;
            }

            // IMPORTANT:
            // Get item from availableItems, NOT menuItems
            MenuItem selectedItem =
                    availableItems.get(choice - 1);

            System.out.println();
            System.out.println(
                    "Selected: " +
                            selectedItem.getItemName()
            );

            System.out.println(
                    "Price: ₹" +
                            selectedItem.getPrice()
            );

            System.out.print("Enter quantity: ");

            int quantity = sc.nextInt();
            sc.nextLine();

            if (quantity <= 0) {

                System.out.println(
                        "Quantity must be greater than 0."
                );

                continue;
            }

            CustomerMenu.addItemToCart(
                    sc,
                    cartDAO,
                    cartItemDAO,
                    loggedInCustomer,
                    selectedRestaurant,
                    selectedItem,
                    quantity
            );

            System.out.println();
        }
    }
}

