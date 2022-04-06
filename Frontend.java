/**
 * ISTE 330 Group 1 project frontend 
 * This file is responsible for communicating with the user 
 * and sending any necesary input to the backend 
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Frontend {

    public static Font myFontForOutput = new Font("Courier", Font.PLAIN, 32);
    private String databaseName, userName, password; //can probably make these local
    private Backend be;

    /**
     * Constructor initializes FE GUI and BE connection
     */
    public Frontend(){
        Scanner scanner = new Scanner(System.in);
        be = new Backend(); //create the new backend instancex 
        JPanel databaseBox = new JPanel(new GridLayout(2,1));
        JLabel lblDatabase = new JLabel("Database name?");
        lblDatabase.setFont(myFontForOutput);
        databaseBox.add(lblDatabase);
        JTextField textfieldDatabaseName = new JTextField("project");
        textfieldDatabaseName.setFont(myFontForOutput);
        textfieldDatabaseName.setForeground(Color.BLUE);
        databaseBox.add(textfieldDatabaseName);
        JOptionPane.showMessageDialog(null,databaseBox,"Database name Input Prompt",JOptionPane.QUESTION_MESSAGE);
        databaseName = textfieldDatabaseName.getText();
          
        //url = url + databaseName;
        //url = url + "?serverTimezone=UTC";

	     JPanel Inputbox = new JPanel(new GridLayout(2,2));
        JLabel lblUser = new JLabel("Username  -> ");
		  JLabel lblPassword = new JLabel("Password  -> ");
        JTextField tfUser = new JTextField("root");
        JTextField tfPassword = new JPasswordField("");

        Inputbox.add(lblUser);
		  Inputbox.add(tfUser);
		  Inputbox.add(lblPassword);
        Inputbox.add(tfPassword);

        lblUser.setFont(myFontForOutput);
        tfUser.setFont(myFontForOutput);
        tfUser.setForeground(Color.BLUE);
        lblPassword.setFont(myFontForOutput);
        tfPassword.setFont(myFontForOutput);
        tfPassword.setForeground(Color.BLUE);
		  JOptionPane.showMessageDialog(null, Inputbox,"SQL Input Prompt", JOptionPane.INFORMATION_MESSAGE); 

         //Get username and pass --> set to root + student when left blank
         userName = tfUser.getText();
         password = tfPassword.getText();
         if(userName.equals("")){
            userName = "root";
         }
         if(password.equals("")){
            password = "student";
         }
         connect(userName, password, databaseName);  
         close();     

        java.util.Date today = new java.util.Date();
        System.out.println("\nProgram terminated @ " + today + "\n");
        System.exit(0);
    }
    
    
    /**
     * Used to make the BE call to establish a connection to the DB
     */
    public void connect(String userName, String password, String databaseName){
      boolean connected = be.connect(userName, password, databaseName);
      if(connected) {System.out.println("Connection successful.");} else {System.out.println("Error connecting");}
    }
    
    public void close(){
      boolean closed = be.close();
      if(closed) {System.out.println("Connection successfully closed.");} else {System.out.println("Error closing connection");}
    }


    /**
     * Main method
     */
    public static void main (String[] args){
        Frontend fe = new Frontend();

    }

}