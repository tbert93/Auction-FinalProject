package assignment7;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientController {

    public final static String DATABASE = "src/assignment7/database.txt";
    public final static String JUST_ITEMS = "src/assignment7/justItems.txt";
    //default Port, change manually here
    //remember to change Server Port
    public final static int PORT = 4242;
    //default Host, change manually here
    public final static String HOST = "localhost";
    //default username
    public static String USERNAME = "Client 1";
    public final static String PASSWORD = "1234";
    //BidData object from Server is stored here
    public BidData bd;

    @FXML
    private TextArea itemList;
    @FXML
    private TextArea messageText;
    @FXML
    private TextArea yourItemsText;
    @FXML
    private Button bidButton;
    @FXML
    private Button  refreshButton;
    @FXML
    private Button clearButton;
    @FXML
    private ChoiceBox<String> dropDown;
    @FXML
    private ChoiceBox<String> usernameDropDown;
    @FXML
    private Slider slider;
    @FXML
    private Label sliderLabel;

    //initializes upon startup
    @FXML
    void initialize() throws IOException {

        //download database from server
        getBidData();
        createDataBase();
        //create text file with just items
        createJustItems();
        //output database to GUI
        updateAuctionList();
        //update dropdown menu with items
        updateDropDown();
        //username can only be chosen at startup
        updateUsernameDropDown();

    }

    @FXML
    void bid(MouseEvent event) {
        try{
            //establish connection with server
            Socket socket = new Socket(HOST,PORT);
            //create an output stream to server
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            //create an input stream from server
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

            //get item properties
            Double value = Math.round(slider.getValue() * 100.0) / 100.0;
            String item = dropDown.getValue();
            String description = findDescription(item);
            Double buyNowPrice = findBuyNowPrice(item);

            //create an Item object and send to the server
            Item i = new Item(item,description,buyNowPrice,value,USERNAME);
            toServer.reset();
            System.out.println("Sending Item object");
            toServer.writeObject(i);
            System.out.println("Item sent");

            //create a BidData object and receive from server
            System.out.println("Receiving Bid Data");
            bd = (BidData)fromServer.readObject();
            System.out.println("Bid Data Received");
            System.out.println(bd.getItems().size());

            //update GUI
            createDataBase();
            updateAuctionList();
            updateMessage(item);




        }

        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }

    }
    @FXML
    void refreshList(MouseEvent event) throws IOException {
        getBidData();
        updateAuctionList();

    }

    @FXML
    void showMoney(MouseEvent event){
        double sliderValue = Math.round(slider.getValue() * 100.0) / 100.0;
        sliderLabel.setText(Double.toString(sliderValue));
    }

    @FXML
    void clearMessages(MouseEvent event){
        messageText.clear();

    }

    @FXML
    void setUsername(){
        USERNAME = usernameDropDown.getValue();
        usernameDropDown.setDisable(true);
    }


    public void updateAuctionList() throws FileNotFoundException {

        File itemsFile = new File(DATABASE);

        Scanner sc = new Scanner(itemsFile);

        itemList.clear();

        while(sc.hasNextLine()){
            String s = itemList.getText();
            String number = sc.nextLine();
            String name = sc.nextLine();
            String description = sc.nextLine();
            String price = sc.nextLine();
            String bid = sc.nextLine();
            String line = sc.nextLine();
            itemList.setText(s + number + "\n" + name + "\n" + description + "\n" + price + "\n" + bid + "\n" + line + "\n");
        }


    }

    public void updateMessage(String item){
        //updates message after bid
        String s = messageText.getText();
        messageText.setText(s + bd.getMessage() + "\n");
        //updates your item list
        if(bd.getMessage().equals("Item Sold to you!")){
            String i = yourItemsText.getText();
            yourItemsText.setText(i + "\n" + item);
        }
    }

    public void updateDropDown() throws FileNotFoundException {
        File file = new File(JUST_ITEMS);
        Scanner sc = new Scanner(file);

        dropDown.getItems().clear();
        while(sc.hasNextLine()){
            String s = sc.nextLine();
            dropDown.getItems().add(s);
        }


    }

    public void updateUsernameDropDown(){
        //Manually add clients here
        usernameDropDown.getItems().add("Client 1");
        usernameDropDown.getItems().add("Client 2");
        usernameDropDown.getItems().add("Client 3");
        usernameDropDown.getItems().add("Client 4");
        usernameDropDown.getItems().add("Client 5");

    }

    public String findDescription(String item) throws FileNotFoundException {
        File file = new File(DATABASE);
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()){
            String s = sc.nextLine();
            if(s.length() > 5) {
                if (s.substring(6).equals(item)) {
                    s = sc.nextLine();
                    return s.substring(13);
                }
            }

        }
        return null;
    }

    public Double findBuyNowPrice(String item)  {

        for(int i = 0; i < bd.getItems().size(); i++){
            if(bd.getItems().get(i).getItemName().equals(item))
                return bd.getItems().get(i).getItemPrice();
        }

        return null;
    }

    public void getBidData(){
        try{
            //establish connection with server
            Socket socket = new Socket(HOST,PORT);
            //create an output stream to server
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            //create an input stream from server
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

            //create an Item object and send to the server
            Item i = null;
            toServer.reset();
            System.out.println("Sending Item object");
            toServer.writeObject(i);
            System.out.println("Item sent");

            //create a BidData object and receive from server
            System.out.println("Receiving Bid Data");
            bd = (BidData)fromServer.readObject();
            System.out.println("Bid Data Received");
            System.out.println(bd.getItems().size());
        }

        catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public void createJustItems() throws IOException {
        FileWriter writer = new FileWriter(JUST_ITEMS,false);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);


        for(int i = 0; i < bd.getItems().size(); i++) {
            String s = bd.getItems().get(i).getItemName();
            bufferedWriter.write(s);
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
    }

    public void createDataBase() throws IOException {
        FileWriter writer = new FileWriter(DATABASE,false);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        for(int i = 0; i < bd.getItems().size(); i++){
            bufferedWriter.write((i+1) + ".");
            bufferedWriter.newLine();
            bufferedWriter.write("Name: " + bd.getItems().get(i).getItemName());
            bufferedWriter.newLine();
            bufferedWriter.write("Description: " + bd.getItems().get(i).getItemDescription());
            bufferedWriter.newLine();
            if(!bd.getItems().get(i).isSold())
                bufferedWriter.write("BuyNow Price: " + bd.getItems().get(i).getItemPrice());
            else
                bufferedWriter.write("SOLD to " + bd.getItems().get(i).getUsername());
            bufferedWriter.newLine();
            if(!bd.getItems().get(i).isSold()) {
                bufferedWriter.write("Current Bid: " + bd.getItems().get(i).getBidPrice());
                if(bd.getItems().get(i).getUsername() != null)
                    bufferedWriter.write(" held by " + bd.getItems().get(i).getUsername());
            }else
                bufferedWriter.write(" ");
            bufferedWriter.newLine();
            bufferedWriter.write("_______________________________________________");
            bufferedWriter.newLine();

        }
        bufferedWriter.close();
    }

}
