package main;

import menu.MainMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MainMenu.mainMenu(sc);
        sc.close();
    }
}
