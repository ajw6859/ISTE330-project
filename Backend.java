     /**
    * ISTE 330 Group 1 project backend
    * This file is responsible for communicating with 
    * the database via MySQL and handling minor communciation 
    * with the FE
    */
   import java.sql.*;
   import java.security.MessageDigest;
   import java.security.NoSuchAlgorithmException;
   public class Backend {

      private Connection conn;
      private String url_string = "jdbc:mysql://localhost/";
      private final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

      /**
       * Constructor --> shouldn't do anything here i don't think
       */
      public Backend(){}
       
      /**
        * Makes the connect to the DB
        */
      public boolean connect(String username, String password, String database){
         url_string += database;
         try{
            Class.forName(DEFAULT_DRIVER);
            conn = DriverManager.getConnection(url_string, username, password);
         } catch (ClassNotFoundException cnfe){
            System.out.println("ERROR CONNECTING\n" + cnfe);
         } catch (SQLException s){
            System.out.println("ERROR CONNECTING\n" + s);
         }
         return (conn != null);
      }
      
      public boolean close(){
         try {
            conn.close();
            conn = null;
         } catch (SQLException s){
            System.out.println("ERROR CONNECTING\n" + s);
         }
         return (conn == null);
      }

      public boolean login(){
         return true;
      }

      public int insertUser(int user_type_ID, String first_name, String last_name, String password, String email, String phone, int department_ID, String major, String office_number, String office_hours){
         //need to do a check to make sure emails arent duplicates
         int ret = 0;
         try {
            //Still need to implement password hashing 2
            PreparedStatement stmt = conn.prepareStatement("Insert into User (user_type_ID, first_name, last_name, password, email, cell_phone, department_ID, major, office_number, office_hours) VALUES (?,?,?,?,?,?,?,?,?,?) ");
            stmt.setInt(1, user_type_ID);
            stmt.setString(2, first_name);
            stmt.setString(3, last_name);
            stmt.setString(4, hashPassword(password));
            stmt.setString(5, email);
            stmt.setString(6, phone);
            stmt.setInt(7, department_ID);
            stmt.setString(8, major);
            stmt.setString(9, office_number);
            stmt.setString(10, office_hours);
            ret = stmt.executeUpdate(); 
         } catch(SQLException s){
            System.out.println("ERROR CONNECTING\n" + s);
         }
         return ret;
      }
      
      /**
       * Helper function for hashing passwords
       */
      public String hashPassword(String passwordToHash){
         String generatedPassword = null;
         try 
          {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
      
            // Add password bytes to digest
            md.update(passwordToHash.getBytes());
      
            // Get the hash's bytes
            byte[] bytes = md.digest();
      
            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
              sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
      
            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
          } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
          }
          return generatedPassword;
      }
      
      /**
       * Used to validate login credentials
       */
      public boolean validateLogin(String email, String password){
         String passToTest = hashPassword(password);
         String result = null;
         try{
            //perform lookup for email
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM User WHERE email=?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
               result = rs.getString(1);
            }
    
         } catch(SQLException s){
            System.out.println("ERROR CONNECTING\n" + s);
         }
         if(result != null){
            byte[] aByteArray = passToTest.getBytes();
            byte[] bByteArray = result.getBytes();
            return MessageDigest.isEqual(aByteArray, bByteArray);
         } else {
            return false;
         }
      
      }

      /**
       * Gets the user type for a user that we already know exists 
       */
      public int getUserTypeID(String email  ){
         int result = 0;
         try{
            //perform lookup for email
            PreparedStatement stmt = conn.prepareStatement("SELECT user_type_ID FROM User WHERE email=?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
               result = rs.getInt(1);
            }
    
         } catch(SQLException s){
            System.out.println("ERROR CONNECTING\n" + s);
         }
         return result;
      }

   public int deleteUser(String email) {
      int numberOfRowsDeleted = 0;
      try {
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM user where email = ?");
         stmt.setString(1, email);
         numberOfRowsDeleted = stmt.executeUpdate();
      }catch (SQLException sqle) {
         System.out.println("ERROR IN METHOD deletePassenger()");
         System.out.println("ERROR MESSAGE -> "+sqle);
      }
      return numberOfRowsDeleted;
   }
   public int deleteAbstract(int abstract_ID) {
      int numberOfRowsDeleted = 0;
      try {
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM abstract where abstract_ID = ?");
         stmt.setInt(1, abstract_ID);
         numberOfRowsDeleted = stmt.executeUpdate();
      }catch (SQLException sqle) {
         System.out.println("ERROR IN METHOD deletePassenger()");
         System.out.println("ERROR MESSAGE -> "+sqle);
      }
      return numberOfRowsDeleted;
   }
   /*
   public int updateUser(int user_ID, int user_type_ID, String first_name, String last_name, String password, String email, int department_ID, String major, String of) {
      int numberOfRowsUpdated = 0;
      JOptionPane.showMessageDialog(null, "Updating a Passenger", "In DataLayer",
      JOptionPane.PLAIN_MESSAGE);
      try {
         PreparedStatement stmt = conn.prepareStatement("UPDATE passenger SET street = ? WHERE passengerID = ?");
         stmt.setInt(2, passengerID);
         stmt.setString(1, streetAddress);
         numberOfRowsUpdated = stmt.executeUpdate();
         
         System.out.println("Records Updated -> "+numberOfRowsUpdated+"<-");
      }
      
      catch (SQLException sqle) {
         System.out.println("ERROR IN METHOD updatePassenger()");
         System.out.println("ERROR MESSAGE -> "+sqle);
      }
      return numberOfRowsUpdated;
   }
   */
   
      public int updateUser(String major, String email) {
      int records = 0;
      
      try {
      PreparedStatement stmt = conn.prepareStatement("UPDATE User set major = ? WHERE email = ?"); 
      stmt.setString(1, major);
      stmt.setString(2, email); 
      records = stmt.executeUpdate();
      } // end of try
      
       catch(SQLException sqle) {
         System.out.println("Error --->" + sqle); 
      } // end of sql exception 
      
      System.out.println("# of records affected -->" + records);
      return records;
      } // end of updateUser     


   public int insertAbstract(String title, String abs, String keywords){
      int ret = 0;
      try{
         PreparedStatement stmt = conn.prepareStatement("INSERT into Abstract (title, abstract, keywords) VALUES (?, ?, ?)");
         stmt.setString (1, title);
         stmt.setString(2, abs);
         stmt.setString(3, keywords);
         ret = stmt.executeUpdate(); 
      }  catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return ret;
   }

   public int getAbstractID(String abs){
      int result = 0;
         try{
            //perform lookup for email
            PreparedStatement stmt = conn.prepareStatement("SELECT abstract_ID FROM Abstract WHERE abstract=?");
            stmt.setString(1, abs);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
               result = rs.getInt(1);
            }
         } catch(SQLException s){
            System.out.println("ERROR CONNECTING\n" + s);
         }
         return result;
   }

   public int insertUserToAbstract(int id, String first_name, String last_name){
      int ret = 0;
      try{
         //first lookup the user based on name to get their id 
         int res = lookupUserByName(first_name, last_name);
         if(res == 0){
            return res;
         }
         PreparedStatement stmt = conn.prepareStatement("INSERT into User_To_Abstract (user_ID, abstract_ID) VALUES (?, ?)");
         stmt.setInt(1, res);
         stmt.setInt(2, id);
         ret = stmt.executeUpdate(); 
      }  catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return ret;
   }
   
   public int lookupUserByName(String first_name, String last_name){
      int result = 0;
      int pid = 1;
         try{
            //perform lookup by first and last name and where the user is a professor 
            PreparedStatement stmt = conn.prepareStatement("SELECT user_ID FROM User WHERE first_name=? AND last_name=? AND user_type_ID=?");
            stmt.setString(1, first_name);
            stmt.setString(2, last_name);
            stmt.setInt(3, pid);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
               result = rs.getInt(1);
            } else {
               result = 0;
            }
         } catch(SQLException s){
            System.out.println("ERROR CONNECTING\n" + s);
            return 0;
         }
         return result;
   
   }
   

   public int getAbstractsByEmail(String email){
      String [] res = {};
      try{
         //String [[]]
         //email to get user_ID
         //User_ID to get abstract_ID's 
         //abstract_id's tp get abstract entries 
         PreparedStatement stmt = conn.prepareStatement("SELECT abstract_ID, title, abstract FROM abstract JOIN User_To_Abstract USING(abstract_ID) JOIN User USING(user_ID) WHERE user.email=?");
         stmt.setString(1, email);
         ResultSet rs = stmt.executeQuery();
         //if(rs.next()){
            
         //}

      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return 0;
   }
      
   
   
}