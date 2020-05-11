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
    public final static int PORT = 4242;
    public final static String HOST = "localhost";
    public final static int FILE_SIZE = 6022386;
    public final static String USERNAME = "Client 1";
    public final static String PASSWORD = "1234";
    public BidData bd;
    //public static Socket socket;

    @FXML
    private TextArea itemList;
    @FXML
    private TextArea messageText;
    @FXML
    private Button bidButton;
    @FXML
    private Button  refreshButton;
    @FXML
    private ChoiceBox<String> dropDown;
    @FXML
    private Slider slider;
    @FXML
    private Label sliderLabel;

    //initializes upon startup
    @FXML
    void initialize() throws IOException {
        //socket = new Socket(HOST,PORT);
        //download database from server
        //getFile(DATABASE);
        getBidData();
        createDataBase();
        //download justItems from server
        //getFile(JUST_ITEMS);
        createJustItems();
        //output database to GUI
        updateAuctionList();
        //update dropdown menu with items
        updateDropDown();
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

            Double value = slider.getValue();
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

            createDataBase();
            updateAuctionList();
            updateMessage();




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
        int sliderValue = (int) slider.getValue();
        sliderLabel.setText(Integer.toString(sliderValue));
    }

    public static void getFile(String file)throws IOException{
        int bytesRead;
        int current = 0;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            System.out.println("Connecting...");

            // receive file
            byte [] bytes  = new byte [FILE_SIZE];
            InputStream is = socket.getInputStream();
            fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bytesRead = is.read(bytes,0,bytes.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(bytes, current, (bytes.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bufferedOutputStream.write(bytes, 0 , current);
            bufferedOutputStream.flush();
            System.out.println("File " + file
                    + " downloaded (" + current + " bytes read)");
        }
        finally {
            if (fileOutputStream != null) fileOutputStream.close();
            if (bufferedOutputStream != null) bufferedOutputStream.close();
            if (socket != null) socket.close();
        }
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
            itemList.setText(s + number + "\n" + name + "\n" + description + "\n" + price + "\n" + bid + "\n");
        }


    }

    public void updateMessage(){
        String s = messageText.getText();
        messageText.setText(s + bd.getMessage() + "\n");
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

    public Double findBuyNowPrice(String item) throws FileNotFoundException {

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

            for(int j = 0; j < bd.getItems().size(); j++){
                System.out.println(bd.getItems().get(j).getItemName());
                System.out.println(bd.getItems().get(j).getItemDescription());
                System.out.println(bd.getItems().get(j).getItemPrice());
                System.out.println(bd.getItems().get(j).getUsername());
            }



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

        }
        bufferedWriter.close();
    }

}
