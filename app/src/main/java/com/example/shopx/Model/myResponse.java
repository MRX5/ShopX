package com.example.shopx.Model;

public class myResponse {
    private String productId;
    private String category;
    private String name;

    public myResponse(String productId,String category)
    {
        this.productId=productId;
        this.category=category;
    }
    public myResponse(String productId, String category,String name) {
        this.productId = productId;
        this.category = category;
        this.name=name;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() { return productId; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
