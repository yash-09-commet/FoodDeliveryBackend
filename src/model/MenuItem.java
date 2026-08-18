package model;

public class MenuItem {

        private int menuItemId;
        private int restaurantId;
        private String itemName;
        private String description;
        private double price;
        private String category;
        private String foodType;
        private boolean isAvailable;

        public MenuItem(
                int restaurantId,
                String itemName,
                String description,
                double price,
                String category,
                String foodType) {

            this.restaurantId = restaurantId;
            this.itemName = itemName;
            this.description = description;
            this.price = price;
            this.category = category;
            this.foodType = foodType;
            this.isAvailable = true;
        }

        public MenuItem() {
        }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }


}

