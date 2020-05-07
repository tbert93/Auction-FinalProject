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

    @FXML
    private TextArea itemList;
    @FXML
    private Button bidButton;
    @FXML
    private ChoiceBox<String> dropDown;
    @FXML
    private Slider slider;
    @FXML
    private Label sliderLabel;

    //initializes upon startup
    @FXML
    void initialize() throws IOException {
        //download database from server
        getFile(DATABASE);
        //download justItems from server
        getFile(JUST_ITEMS);
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

            Double value = slider.getValue();
            String item = dropDown.getValue();
            String description = findDescription(item);

            //create an Item object and send to the server
            Item i = new Item(item,description,value,USERNAME);
            toServer.writeObject(i);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
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
            itemList.setText(s + number + "\n" + name + "\n" + description + "\n" + price + "\n");
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

    public String findDescription(String item) throws FileNotFoundException {
        File file = new File(DATABASE);
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()){
            String s = sc.nextLine();
            if(s.equals(item)){
                s = sc.nextLine();
                return s.substring(13);

            }

        }
        return null;
    }

}
