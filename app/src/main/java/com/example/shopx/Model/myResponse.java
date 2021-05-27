package com.example.shopx.Model;

public class myResponse {
    private String productId;
    private String category;


    public myResponse(String productId, String category) {
        this.productId = productId;
        this.category = category;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() { return productId; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
