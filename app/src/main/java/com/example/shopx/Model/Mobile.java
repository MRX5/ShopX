package com.example.shopx.Model;

public class Mobile {
    private String id;
    private String name;
    private String price;
    private String imageUrl;
    private String description;
    private boolean inWishlist;
    private boolean inCart;

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


    public Mobile(String id, String name, String price,boolean inWishlist) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inWishlist=inWishlist;
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
        int cnt = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = price.length() - 1; i >= 0; i--) {
            builder.append(price.charAt(i));
            cnt++;
            if (cnt == 3) {
                cnt = 0;
                builder.append(',');
            }
        }
        return builder.reverse().toString();
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
