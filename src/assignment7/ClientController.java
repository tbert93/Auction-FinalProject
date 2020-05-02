package assignment7;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.util.Scanner;

public class ClientController {

    public final static String DATABASE = "src/assignment7/database.txt";
    public final static int PORT = 4242;
    public final static String HOST = "localhost";
    public final static int FILE_SIZE = 6022386;

    public TextArea itemList;

    //initializes upon startup
    @FXML
    void initialize() throws IOException {
        //download database from server
        getDatabase();
        //output database to GUI
        updateAuctionList();





    }

    public static void getDatabase()throws IOException{
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
            fileOutputStream = new FileOutputStream(DATABASE);
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
            System.out.println("File " + DATABASE
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

}
