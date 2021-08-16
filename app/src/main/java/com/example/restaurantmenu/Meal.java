package com.example.restaurantmenu;

public class Meal {
    private int id;
    private int userID;
    private int image;
    private String name;
    private String ingredients;
    private String price;
    private String type;
    private int cart;
    private int cartIcon;

    public Meal(int id, int userID, int image, String name, String ingredients, String price, String type, int cart, int cartIcon) {
        this.id = id;
        this.userID = userID;
        this.image = image;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.type = type;
        this.cart = cart;
        this.cartIcon = cartIcon;
    }

    public Meal(int userID, int image, String name, String ingredients, String price, String type, int cart, int cartIcon) {
        this.userID = userID;
        this.image = image;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.type = type;
        this.cart = cart;
        this.cartIcon = cartIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCart() {
        return cart;
    }

    public void setCart(int cart) {
        this.cart = cart;
    }

    public int getCartIcon() {
        return cartIcon;
    }

    public void setCartIcon(int cartIcon) {
        this.cartIcon = cartIcon;
    }
}
