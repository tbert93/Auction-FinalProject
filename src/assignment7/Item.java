package assignment7;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {
    private String itemName;
    private String itemDescription;
    private Double itemPrice = 1.00;
    private Boolean sold;
    private String username;
    private static final long serialVersionUID = 1234L;
    Item(String name, String description, Double price){//constructed only from server side
        setItemName(name);
        setItemDescription(description);
        setItemPrice(price);
        sold = false;

    }

    Item(String name, String description, Double price, String client){ //constructed only from client side
        setItemName(name);
        setItemDescription(description);
        setItemPrice(price);
        Sold();
        setUsername(client);
    }

    private void setItemName(String name){
        itemName = name;
    }

    private void setItemDescription(String description){
        itemDescription = description;
    }

    void setItemPrice(Double price){
        itemPrice = price;
    }

    void setUsername(String client){
        username = client;
    }

    String getItemName(){
        return itemName;
    }

    String getItemDescription(){
        return itemDescription;
    }

    Double getItemPrice(){
        return itemPrice;
    }

    String getUsername(){
        return username;
    }

    boolean isSold(){
        return sold;
    }

    public String toString(){
        return null;

    }

    void Sold(){
        sold = true;
    }






}
