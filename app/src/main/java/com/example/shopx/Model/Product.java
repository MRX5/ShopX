package com.example.shopx.Model;

public class Product {
    private String id;
    private String name;
    private String price;
    private String imageUrl;
    private String description;
    private String category;
    private boolean inWishlist;
    private boolean inCart;

    public Product() { }

    public Product(String id, String name, String price, String category, boolean inWishlist, boolean inCart) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.inWishlist = inWishlist;
        this.inCart = inCart;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInWishlist() {
        return inWishlist;
    }

    public void setInWishlist(boolean inWishlist) {
        this.inWishlist = inWishlist;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
