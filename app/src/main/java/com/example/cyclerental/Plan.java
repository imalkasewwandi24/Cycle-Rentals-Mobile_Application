package com.example.cyclerental;

public class Plan {
    private int imageId;
    private String name;
    private String price;
    private String time;

    public Plan(int imageId, String name, String price, String time) {
        this.imageId = imageId;
        this.name = name;
        this.price = price;
        this.time = time;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }
}
