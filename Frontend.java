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
        int type_ID = login(); //login operates as a loop

        //main menu calls
        if(type_ID == 1){ //is professor 
          mainMenuProfessor();
        } else if(type_ID == 2){ //is student
          mainMenuStudent();
        }

        close(); // close the connection 
        java.util.Date today = new java.util.Date();
        System.out.println("\nProgram terminated @ " + today + "\n");
        System.exit(0);
        
    }
    
    /**
     * Used to log users in
     */
    public int login(){
      String email = "a";
      String password = "a";
      int id = 0;
      int exit = 1; //for emergency stops? idk if we need this or not 
      while(exit != 0){ //while the crendtials don't match
        System.out.print("Do you wish to continue to login? (0 = no | 1 = yes): ");
        exit = GetInput.readLineInt();
        System.out.print("Enter your email: ");
        email = GetInput.readLine();
         System.out.print("Enter your password: ");
         password = GetInput.readLine();
         if(be.validateLogin(email, password)){
            System.out.println("Login Successful.");
            id = be.getUserTypeID(email);//get the use type to return;
            exit = 0;
         }
      }
      return id;
    }

    /**
     * Used to display options for students
     */
    public void mainMenuStudent(){
      int opt = 0;
      
      while(opt != 5){
        System.out.println("Student Main Menu <3\nOptions:\n1) Search by keyword/phrase\n2)View matches\n3)Connect\n4)Exit");
        System.out.print("Selection: ");
        opt = GetInput.readLineInt();
        switch(opt){
        case 1: 
          System.out.println("You selected option 1.");
          break;
        case 2: 
          System.out.println("You selected option 2.");
          break;
        case 3: 
          System.out.println("You selected option 3.");
          break;
        case 4:
          System.out.println("You selected option 4. Have a nice day!");
          break;
        default:
          System.out.println("Default triggered");
        }
      }

    }

    /**
     * Used to display options for professor
     */
    public void mainMenuProfessor(){
      //mark as connected --> I think we may need to add another column into the db to mannage new vs old connections
      int opt = 0;
      
      while(opt != 5){
        System.out.println("Professor Main Menu <3\nOptions:\n1) Add an abstract\n2)Edit an abstract\n3)Delete an abstract\n4)View macthes\n5)Exit");
        System.out.print("Selection: ");
        opt = GetInput.readLineInt();
        switch(opt){
        case 1: 
          System.out.println("You selected option 1: Add an Abstract.");
          boolean ret = insertAbstract();
          break;
        case 2: 
          System.out.println("You selected option 2.");
          break;
        case 3: 
          System.out.println("You selected option 3.");
          break;
        case 4:
          System.out.println("You selected option 4.");
          break;
        case 5:
          System.out.println("You selected option 5. Have a nice day!");
          break;
        default:
          System.out.println("Default triggered");
        }
      }

    }

    /**
     * Allows a professor to upload an abstract
     */
    public boolean insertAbstract(){return true;}

    /**
     * Allows a professor to edit an existing abstract
     */
    public boolean updateAbstract(){return true;}

    /**
     * Allows a professor to delete an abstract
     */
    public boolean deleteAbstract(){return true;}

    /**
     * Allows a professor to view prospective matches
     */
    public void viewMatches(){}

    /**
     * Helper function to format data 
     */
    public void formatData(){}

    /**
     * used to add users to get all info needed to add a user to the db 
     */
    public void addUser(){
      //Gather info from the user
      System.out.print("Enter user type\nOptions:\n1 -> professor\n2 -> Student\n3 -> public\nYour selection: ");
      int user_type_ID = GetInput.readLineInt();
      System.out.print("Enter your first name: ");
      String first_name = GetInput.readLine();
      System.out.print("Enter your last name: ");
      String last_name = GetInput.readLine();
      System.out.print("Enter your email: ");
      String email = GetInput.readLine();
      System.out.print("Enter deparmtent ID: "); //need to make it print out options
      int department_ID = GetInput.readLineInt();
      String major = null; 
      String office_number = null; 
      String office_hours = null;

      if(user_type_ID == 1){ //if professor
        System.out.print("Enter your office number: ");
        office_number = GetInput.readLine();
        System.out.print("Enter your office hours: ");
        office_hours = GetInput.readLine();
      } else if (user_type_ID == 2){ //if student
        System.out.print("Enter your major: ");
        major = GetInput.readLine();
      }

      System.out.print("Enter your password: ");
      String password = GetInput.readLine();
    

      int ret = be.insertUser(user_type_ID, first_name, last_name, password, email, department_ID, major, office_number, office_hours);
      System.out.println(ret + "row(s) affected.");
    }
    
    
    /**
     * Used to make the BE call to establish a connection to the DB
     */
    public void connect(String userName, String password, String databaseName){
      boolean connected = be.connect(userName, password, databaseName);
      if(connected) {System.out.println("Connection successful.");} else {System.out.println("Error connecting");}
    }
    
    /**
     * BE call to close the connection 
     */
    public void close(){
      boolean closed = be.close();
      if(closed) {System.out.println("Connection successfully closed.");} else {System.out.println("Error closing connection");}
    }


    /**
     * Main method
     */
    public static void main (String[] args){
      Frontend fe = new Frontend(); //create gui to connect to the DB

    }

}