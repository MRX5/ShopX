package com.example.shopx.Model;

public class Mobile {
    private String id;
    private String name;
    private String price;
    private String imageUrl;
    private String description;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public Mobile(String id,String name, String price) {
        this.id=id;
        this.name = name;
        this.price = price;
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
        int cnt=0;
        StringBuilder builder=new StringBuilder();
        for(int i=price.length()-1;i>=0;i--)
        {
            builder.append(price.charAt(i));
            cnt++;
            if(cnt==3)
            {
                cnt=0;
                builder.append(',');
            }
        }
        return builder.reverse().toString();
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
