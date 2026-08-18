package menu;
import dao.*;

import java.util.*;
public class MainMenu {

    public static void mainMenu(Scanner sc ){
        CustomerDAO customerDAO = new CustomerDAO();
        RestaurantDAO restaurantDAO = new RestaurantDAO();
        while (true) {
            System.out.println("=========== Food Delivery Backend ==========");
            System.out.println(" 1. Customer ");
            System.out.println(" 2. Restaurant ");
            System.out.println(" 3. Exit ");
            System.out.println("Enter your choice : ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                CustomerMenu.mainMenu(sc , customerDAO);
                    break;
                case 2:
                    RestaurantMenu.mainMenu(sc,restaurantDAO);
                    break;
                case 3:
                    System.out.println(" Thank ypu for using Food Delivery Backend! ");
                    sc.close();
                    return;
                default:
                    System.out.println(" Invalid Choice , Please try again. ");
            }
        }
    }
}
