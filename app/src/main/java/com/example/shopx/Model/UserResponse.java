package com.example.shopx.Model;

public class UserResponse {
    private boolean InCart;
    private boolean InWish;
    private String category;

    public UserResponse(){}

    public boolean isInCart() {
        return InCart;
    }

    public void setInCart(boolean inCart) {
        this.InCart = inCart;
    }

    public boolean isInWish() {
        return InWish;
    }

    public void setInWish(boolean inWish) {
        this.InWish = inWish;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
