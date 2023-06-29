package com.example.tenang_capstone.ui.shop;

public class ShopItemList {
    public String name;
    public String description;
    public String type;
    public String cost;
    public String id;
    public String image;

    public ShopItemList(String name, String cost, String description, String type, String id, String image) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.cost = cost;
        this.id = id;
        this.image = image;
    }
}
