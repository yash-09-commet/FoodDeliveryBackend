package menu;
import dao.CustomerDAO;

import java.util.*;
public class MainMenu {
    public static void start(){
        Scanner sc = new Scanner(System.in);
        CustomerDAO customerDAO = new CustomerDAO();
        while (true) {
            System.out.println("=========== Food Delivery Backend ==========");
            System.out.println(" 1. Sign up ");
            System.out.println(" 2. Log in ");
            System.out.println(" 3. Exit ");
            System.out.println("Enter your choice : ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                CustomerMenu.signup(sc , customerDAO);
                    break;
                case 2:
                    CustomerMenu.login(sc,customerDAO);
                    break;
                case 3:
                    System.out.println(" Thank ypu for using Food Delivery Backend! ");
                    sc.close();
                    return;
                default:
                    System.out.println(" Invalid Choice ");
            }
        }
    }
}
