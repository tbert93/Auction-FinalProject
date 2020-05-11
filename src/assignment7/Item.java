package assignment7;


import java.io.Serializable;

public class Item implements Serializable {
    private String itemName;
    private String itemDescription;
    private Double buyNowPrice;
    private Double bidPrice;
    private Boolean sold;
    private String username;
    private static final long serialVersionUID = 1234L;

    Item(String name, String description, Double price, Double startingPrice){//constructed only from server side
        setItemName(name);
        setItemDescription(description);
        setItemPrice(price);
        setBidPrice(startingPrice);
        sold = false;
    }

    Item(String name, String description, Double price, Double bid, String client){ //constructed only from client side
        setItemName(name);
        setItemDescription(description);
        setItemPrice(price);
        setBidPrice(bid);
        setUsername(client);
        sold = false;
    }

    private void setItemName(String name){
        itemName = name;
    }

    private void setItemDescription(String description){
        itemDescription = description;
    }

    void setItemPrice(Double price){
        buyNowPrice = price;
    }

    void setBidPrice(Double startingPrice){
        bidPrice = startingPrice;
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
        return buyNowPrice;
    }

    String getUsername(){
        return username;
    }

    Double getBidPrice(){
        return bidPrice;
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
