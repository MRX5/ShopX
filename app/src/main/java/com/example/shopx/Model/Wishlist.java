package com.example.shopx.Model;

public class Wishlist {
    private String productId;
    private String name;
    private String image_url;
    private String price;
    private String category;

    public Wishlist(){}
    public Wishlist(String productId, String name, String productImage, String price, String category) {
        this.productId = productId;
        this.name = name;
        this.image_url = productImage;
        this.price = price;
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
