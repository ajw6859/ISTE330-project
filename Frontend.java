/**
 * ISTE 330 Group 1 project frontend 
 * This file is responsible for communicating with the user 
 * and sending any necesary input to the backend 
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;

public class Frontend {

    public static Font myFontForOutput = new Font("Courier", Font.PLAIN, 32);
    private String databaseName, userName, password, email; //can probably make these local
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
        connect(userName, password, databaseName);  //connect to the db

        //add a professor and student user for testing purposes check if email already exists so no duplicates
        if(be.getUserTypeID("jim.habermas@rit.edu") == 0){
          be.insertUser(1, "Jim", "Habermas", "test", "jim.habermas@rit.edu", "0000000000", 1, null, "GOL 2650", "M, W: 3-3:50"); //professor user
        }
        if(be.getUserTypeID("ajw6859@rit.edu") == 0){
          be.insertUser(2, "Allison", "Wright", "test", "ajw6859@rit.edu", "9782398944", 2, "CSEC", null, null); //student user
        }
        

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
      //String email = "a";
      String password = "a";
      int id = 0;
      int exit = 1; //for emergency stops? idk if we need this or not 
      while(exit != 0){ //while the crendtials don't match
        System.out.print("Enter your email: ");
        email = GetInput.readLine();
         System.out.print("Enter your password: ");
         password = GetInput.readLine();
         if(be.validateLogin(email, password)){
            System.out.println("Login Successful.");
            id = be.getUserTypeID(email);//get the user type to return;
            exit = 0;
            break;
         }
        System.out.print("Do you wish to continue to login? (0 = no | 1 = yes): ");
        exit = GetInput.readLineInt();
      }
      return id;
    }

    /**
     * Used to display options for students
     */
    public void mainMenuStudent(){
      int opt = 0;
      
      while(opt != 5){
        System.out.println("Student Main Menu <3\nOptions:\n1)Search by keyword/phrase\n2)View matches\n3)Connect\n4)Exit");
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
     * Allows a student to search
     */
    public static void searchByKeyword(){
        System.out.println("Enter up to three keywords or phrases, seperated by comma");
       
        Scanner scanner = new Scanner(System.in);  // Create Scanner object
        String inputString = scanner.nextLine();
       
        //split string by comma, remove spaces
        ArrayList<String> inputStringSplit = new ArrayList<>(Arrays.asList(inputString.split(",[ ]*")));
       
        //print out keywords in list
        for (String a : inputStringSplit){
            System.out.println(a);
        }
       
        //pass to backend
        // the list is called inputStringSplit
    }


    /**
     * Used to display options for professor
     */
    public void mainMenuProfessor(){
      //mark as connected --> I think we may need to add another column into the db to mannage new vs old connections
      int opt = 0;
      
      while(opt != 5){
        System.out.println("Professor Main Menu <3\nOptions:\n1)Add an abstract\n2)Edit an abstract\n3)Delete an abstract\n4)View macthes\n5)Exit");
        System.out.print("Selection: ");
        opt = GetInput.readLineInt();
        switch(opt){
        case 1: 
          System.out.println("You selected option 1: Add an Abstract.");
          insertAbstract();
          break;
        case 2: 
          System.out.println("You selected option 2. Edit an Abstract");
          updateAbstract();
          break;
        case 3: 
          System.out.println("You selected option 3. Delete an Abstract");
          removeAbstract();
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
    public void insertAbstract(){
      try {
        //get file path
        String filepath = "";
        System.out.print("Enter the direct path to the file: ");
        filepath = GetInput.readLine();

        //make file reader object 
        File file = new File(filepath);
        Scanner sc = new Scanner(file);
        String data = ""; //tmp data
        String title = "";
        String abs = ""; //for the full abstract
        String keywords = ""; //abstract without unnecessary words 
        String [] author_names = new String[3]; // used to store author names for the given abstract
        List<String> abs_iter = new ArrayList<String>(); //used to store abstracts so they can be iterated over for associations
        String [] to_block = {"a", "to", "the", "there", "their", "they're", "i"};

        boolean first = true; //used to mark first line for title
        boolean second = true; //used to mark second line for authors 
        boolean inserted = false; //used to denote that 
        int index = 0;
        
        while(sc.hasNextLine()){
          //read in each line at a time 
          data = sc.nextLine();

          //if first line split by "-", "by", and then commas
          if(first){
            title = data;
            first = false;
            inserted = false; 

          } else if(second){
            //split authors by comma 
            author_names = data.split(","); //tmp storage for author names
            
            //trim off extra white space 
            for(int i=0; i < author_names.length; i++){
              author_names[i] = author_names[i].replaceAll("\n", "").trim();
              index ++;
            }
            second = false;

          } else {
            //split the given line via whitespace 
            for (String word : data.split("\\s+")){
              if(word.equals("****")){
                first = true; second = true; 
                //insert the abstract 
                be.insertAbstract(title, abs, keywords);
                abs_iter.add(abs);

                int abs_id = be.getAbstractID(abs);
                for(int i=0; i < author_names.length; i++){
                  String [] name = author_names[i].split("\\s+"); //splits name into first and last 
                  
                  String first_name = name[0];
                  String last_name = name[name.length-1];
                  if(first_name.charAt(0) == ' '){
                    first_name = name[1];
                  }  
                  
                  //need to check if the user is in the db
                  int ah = be.lookupUserByName(first_name, last_name);
                  if(ah == 0){
                    insertUserHelper(first_name, last_name);
                    int ret = be.insertUserToAbstract(abs_id, first_name, last_name);
                    System.out.println(ret + " record(s) inserted");
                  } else {
                    int ret = be.insertUserToAbstract(abs_id, first_name, last_name);
                    System.out.println(ret + " record(s) inserted");
                  }
                }

                abs ="";
              } else {
                abs += word; 
                abs += " ";

                //check for blocked words ****needs work but i don't want to rn
                boolean added = false;
                for(int i=0; i < to_block.length; i++){
                  if(!word.toLowerCase().contains(to_block[i]) && !added){ //if the word isnt to be blocked
                    keywords += word;  
                    keywords += " ";
                    added = true;
                  }
                }          
              }
            }
          } 
        } // end while  
          //for each abstract in the file go through and add the association
          for(String item: abs_iter){
            

          } 
      } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
      
    }


    /**
     * Used when inserting a user from an abstract
     */
    public void insertUserHelper(String first_name, String last_name){
      System.out.println("User to be added: "+ last_name + ", " + first_name);
      int user_type_ID = 1;
      System.out.print("Enter email: ");
      String email = GetInput.readLine();
      System.out.print("Enter phone: ");
      String phone = GetInput.readLine();
      System.out.print("Enter deparmtent ID: "); //need to make it print out options
      int department_ID = GetInput.readLineInt();
      String major = null; 
      String office_number = null; 
      String office_hours = null;

      if(user_type_ID == 1){ //if professor
        System.out.print("Enter office number: ");
        office_number = GetInput.readLine();
        System.out.print("Enter office hours: ");
        office_hours = GetInput.readLine();
      } else if (user_type_ID == 2){ //if student
        System.out.print("Enter major: ");
        major = GetInput.readLine();
      }

      System.out.print("Enter password: ");
      String password = GetInput.readLine();
    

      int ret = be.insertUser(user_type_ID, first_name, last_name, password, email, phone,department_ID, major, office_number, office_hours);
      System.out.println(ret + "row(s) affected.");
    }

   
    /**
     * Allows a professor to edit an existing abstract
     */
    public void updateAbstract(){
      //select all abstracts for the current user
      //String[] tmp = be.getAbstractsByEmail(email);
      
      System.out.println("a");
    }

    /**
     * Allows a professor to delete an abstract
     */
    public boolean removeAbstract(){
      //delete abstract entry from abstract table
      System.out.println("What abstract entry would you like to delete?");
      int abstractnum = GetInput.readLineInt();
      int ret = be.deleteAbstract(abstractnum);
      System.out.println(ret + " row(s) affected.");
      return true;
    }

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
      System.out.print("Enter your phone: ");
      String phone = GetInput.readLine();
      System.out.print("Enter department ID: "); //need to make it print out options
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
    

      int ret = be.insertUser(user_type_ID, first_name, last_name, password, email, phone,department_ID, major, office_number, office_hours);
      System.out.println(ret + "row(s) affected.");
    }
    
    public void removeUser(){
      //Gather info from the user
      System.out.print("Enter user type\nOptions:\n1 -> professor\n2 -> Student\n3 -> public\nYour selection: ");
      int user_type_ID = GetInput.readLineInt();

      System.out.print("Enter email of the user you want to delete: ");
      String email = GetInput.readLine();
 
      if(user_type_ID == 1){ //if professor
        int ret = be.deleteUser(email);
        System.out.println(ret + "row(s) affected.");
      } else if (user_type_ID == 2){ //if student
        System.out.println("Student not allowed to delete user");
      }   
    } // end of remove user
    
    
    public void updateUser() {
    System.out.println("What is the major you want to change to?");
    String major = GetInput.readLine();
   
    System.out.println("What is the email of the user you are changing the major of?");
    String email = GetInput.readLine();
   
    be.updateUser(major, email); 

    
    } // end of update user
    
    
    
    
           
      

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