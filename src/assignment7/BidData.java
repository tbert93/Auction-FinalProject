package assignment7;

import java.io.Serializable;
import java.util.ArrayList;

public class BidData implements Serializable {
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Item> itemsToBeProcessed = new ArrayList<>();
    private String message;
    private static final long serialVersionUID = 1L;
    BidData(){

    }

    void addItems(Item i){
        items.add(i);
    }

    void addItemsToBeProcessed(Item i){
        itemsToBeProcessed.add(i);
    }

    void setMessage(String m){
        message = m;
    }

    ArrayList<Item> getItems(){
        return items;
    }

    ArrayList<Item> getItemsToBeProcessed(){
        return itemsToBeProcessed;
    }

    String getMessage(){
        return message;
    }
}
