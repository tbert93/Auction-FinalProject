package assignment7;

import java.io.Serializable;
import java.util.ArrayList;

public class BidData implements Serializable {
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Item> itemsToBeProcessed = new ArrayList<>();
    private static final long serialVersionUID = 1L;
    BidData(){

    }

    void addItems(Item i){
        items.add(i);
    }

    void addItemsToBeProcessed(Item i){
        itemsToBeProcessed.add(i);
    }

    ArrayList<Item> getItems(){
        return items;
    }

    ArrayList<Item> getItemsToBeProcessed(){
        return itemsToBeProcessed;
    }
}
