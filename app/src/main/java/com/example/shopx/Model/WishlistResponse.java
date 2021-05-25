package com.example.shopx.Model;

public class WishlistResponse {
    private String productId;
    private String category;

    public String getProductId() {
        return productId;
    }

    public WishlistResponse(String productId, String category) {
        this.productId = productId;
        this.category = category;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
