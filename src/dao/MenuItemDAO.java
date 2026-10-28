package dao;

import model.MenuItem;
import util.DatabaseConnection;
import java.util.*;
import java.sql.*;

public class MenuItemDAO {

    public boolean addMenuItem(MenuItem menuItem) {

        try {

            Connection connection = DatabaseConnection.getConnection();

            String sql = "INSERT INTO menu_item " +
                    "(restaurantId, itemName, description, price, category, foodType, isAvailable) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, menuItem.getRestaurantId());
            ps.setString(2, menuItem.getItemName());
            ps.setString(3, menuItem.getDescription());
            ps.setDouble(4, menuItem.getPrice());
            ps.setString(5, menuItem.getCategory());
            ps.setString(6,menuItem.getFoodType());
            ps.setBoolean(7, menuItem.isAvailable());

            int rowsAffected = ps.executeUpdate();

            ps.close();
            connection.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public MenuItem getMenuItemById(int menuItemId) {

        String sql =
                "SELECT menuItemId, restaurantId, itemName, " +
                        "description, price, category, foodType, isAvailable " +
                        "FROM menu_item " +
                        "WHERE menuItemId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, menuItemId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    MenuItem menuItem = new MenuItem();

                    menuItem.setMenuItemId(
                            rs.getInt("menuItemId")
                    );

                    menuItem.setRestaurantId(
                            rs.getInt("restaurantId")
                    );

                    menuItem.setItemName(
                            rs.getString("itemName")
                    );

                    menuItem.setDescription(
                            rs.getString("description")
                    );

                    menuItem.setPrice(
                            rs.getDouble("price")
                    );

                    menuItem.setCategory(
                            rs.getString("category")
                    );

                    menuItem.setFoodType(
                            rs.getString("foodType")
                    );

                    menuItem.setAvailable(
                            rs.getBoolean("isAvailable")
                    );

                    return menuItem;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<MenuItem> getMenuItemsByRestaurantId(int restaurantId) {

        List<MenuItem> menuItems = new ArrayList<>();

        try {

            Connection connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM menu_item WHERE restaurantId = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, restaurantId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                MenuItem menuItem = new MenuItem();

                menuItem.setMenuItemId(rs.getInt("menuItemId"));
                menuItem.setRestaurantId(rs.getInt("restaurantId"));
                menuItem.setItemName(rs.getString("itemName"));
                menuItem.setDescription(rs.getString("description"));
                menuItem.setPrice(rs.getDouble("price"));
                menuItem.setCategory(rs.getString("category"));
                menuItem.setFoodType(rs.getString("foodType"));
                menuItem.setAvailable(rs.getBoolean("isAvailable"));

                menuItems.add(menuItem);
            }

            rs.close();
            ps.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuItems;
    }

    public boolean updateMenuItem(MenuItem menuItem) {

        try {

            Connection connection = DatabaseConnection.getConnection();

            String sql = "UPDATE menu_item SET " +
                    "itemName = ?, " +
                    "description = ?, " +
                    "price = ?, " +
                    "category = ?, " +
                    "foodType = ?, " +
                    "isAvailable = ? " +
                    "WHERE menuItemId = ? AND restaurantId = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, menuItem.getItemName());
            ps.setString(2, menuItem.getDescription());
            ps.setDouble(3, menuItem.getPrice());
            ps.setString(4, menuItem.getCategory());
            ps.setString(5, menuItem.getFoodType());
            ps.setBoolean(6, menuItem.isAvailable());
            ps.setInt(7, menuItem.getMenuItemId());
            ps.setInt(8, menuItem.getRestaurantId());

            int rowsAffected = ps.executeUpdate();

            ps.close();
            connection.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteMenuItem(int menuItemId, int restaurantId) {

        try {

            Connection connection = DatabaseConnection.getConnection();

            String sql = "DELETE FROM menu_item " +
                    "WHERE menuItemId = ? AND restaurantId = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, menuItemId);
            ps.setInt(2, restaurantId);

            int rowsAffected = ps.executeUpdate();

            ps.close();
            connection.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}