package validation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import dao.CustomerDAO;
import util.DatabaseConnection;

public class customerValidation {
    public static String getValidName(Scanner sc){
        while (true ){
            System.out.println(" Enter full name ");
            String name = sc.nextLine().trim();
            if(name.isEmpty()) {
                System.out.println(" Name can not be empty");
                continue;
            }
            if(name.length() < 3 ){
                System.out.println(" Name is too short must contain atleast 3 letters ");
                continue;
            }
            return name ;
        }
    }


    public static String getValidEmail(Scanner sc , CustomerDAO customerDAO){
            while (true) {
                System.out.println(" Enter email ");
                String email = sc.nextLine().trim();

                if (email.isEmpty()) {
                    System.out.println("Email cannot be empty.");
                    continue;
                }

                if (!email.contains("@") || !email.contains(".")) {
                    System.out.println("Invalid email format.");
                    continue;
                }

                if (customerDAO.emailExists(email)) {
                    System.out.println("Email already registered.");
                    continue;
                }

                return email;
            }
        }


        public static String getValidPhoneNumber(Scanner sc ){
        while (true){
            System.out.println(" Enter phoneNumber ");
            String number = sc.nextLine().trim();
                if(number.isEmpty()){
                System.out.println("Phone Number can not be empty ");
                continue;
                }
                if(number.length()!= 10){
                System.out.println(" Phone number must contain exactly 10 digits ");
                continue;
                }
                boolean valid    = true ;
                for (int i = 0; i < number.length(); i++) {
                    if (!Character.isDigit(number.charAt(i))) {
                    valid = false;
                    break;
                    }
                }
                if(! valid ){
                System.out.println(" Phone Number must contain only digits ");
                continue ;
                }
            return number;
            }
        }

        public static String getValidPassword(Scanner sc){
            while (true) {

                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                boolean hasUpper = false;
                boolean hasLower = false;
                boolean hasDigit = false;
                boolean hasSpecial = false;

                for (char ch : password.toCharArray()) {
                    if (Character.isUpperCase(ch)) {
                        hasUpper = true;
                    } else if (Character.isLowerCase(ch)) {
                        hasLower = true;
                    } else if (Character.isDigit(ch)) {
                        hasDigit = true;
                    } else {
                        hasSpecial = true;
                    }
                }

                if (!hasUpper) {
                    System.out.println("Password must contain at least one uppercase letter.");
                    continue;
                }

                if (!hasLower) {
                    System.out.println("Password must contain at least one lowercase letter.");
                    continue;
                }

                if (!hasDigit) {
                    System.out.println("Password must contain at least one digit.");
                    continue;
                }

                if (!hasSpecial) {
                    System.out.println("Password must contain at least one special character.");
                    continue;
                }
                return password;
            }


        }

    public static String getValidAddress(Scanner sc ){

        while (true){
            System.out.println("Enter the address");
            String address = sc.nextLine();
            if(address.isEmpty()){
                System.out.println(" address can not be empty ");
                continue;
            }
            if(address.length()<3){
                System.out.println("address is too small to");
                continue;
            }
            return address;
        }

    }
    }

