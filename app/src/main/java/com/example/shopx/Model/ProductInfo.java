package com.example.shopx.Model;

public class ProductInfo {
    private String id;
    private String name;
    private String image_url;
    private String price;
    private String category;
    private boolean InCart;
    private boolean InWish;

    public boolean isInCart() {
        return InCart;
    }

    public void setInCart(boolean inCart) {
        InCart = inCart;
    }

    public boolean isInWish() {
        return InWish;
    }

    public void setInWish(boolean inWish) {
        InWish = inWish;
    }

    public ProductInfo(){}

    public ProductInfo(String name, String image_url, String price) {
        this.name = name;
        this.image_url = image_url;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
