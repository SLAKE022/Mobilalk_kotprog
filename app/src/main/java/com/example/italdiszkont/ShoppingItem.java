package com.example.italdiszkont;

public class ShoppingItem {
    private String id;
    private String name;
    private String info;
    private String price;
    private float rated;
    private String alcohol;
    private int imageResource;
    private int cartedCount=0;

    public ShoppingItem() {
    }

    public ShoppingItem(String name, String info, String price, float rated, String alcohol, int imageResource) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.rated = rated;
        this.alcohol = alcohol;
        this.imageResource = imageResource;
        this.cartedCount = 0;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }

    public float getRated() {
        return rated;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public int getImageResource() {
        return imageResource;
    }
    public int getCartedCount() {
        return cartedCount;
    }
    public String _getid(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setCartedCount(){
        this.cartedCount =0;
    }
}
