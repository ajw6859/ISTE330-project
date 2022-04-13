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

      public int insertUser(int user_type_ID, String first_name, String last_name, String password, String email, int department_ID, String major, String office_number, String office_hours){
         //need to do a check to make sure emails arent duplicates
         int ret = 0;
         try {
            //Still need to implement password hashing 2
            PreparedStatement stmt = conn.prepareStatement("Insert into User (user_type_ID, first_name, last_name, password, email, department_ID, major, office_number, office_hours) VALUES (?,?,?,?,?,?,?,?,?) ");
            stmt.setInt(1, user_type_ID);
            stmt.setString(2, first_name);
            stmt.setString(3, last_name);
            stmt.setString(4, hashPassword(password));
            stmt.setString(5, email);
            stmt.setInt(6, department_ID);
            stmt.setString(7, major);
            stmt.setString(8, office_number);
            stmt.setString(9, office_hours);
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
      public int getUserTypeID(String email){
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

   public int deleteUser(int user_ID) {
      int numberOfRowsDeleted = 0;
      JOptionPane.showMessageDialog(null, "Deleting a User", "In DataLayer",
      JOptionPane.PLAIN_MESSAGE);
      try {
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM user where user_ID = ?");
         stmt.setInt(1, user_ID);
         numberOfRowsDeleted = stmt.executeUpdate();
         
         System.out.println("Records Deleted -> "+numberOfRowsDeleted+"<-");
      }
      
      catch (SQLException sqle) {
         System.out.println("ERROR IN METHOD deletePassenger()");
         System.out.println("ERROR MESSAGE -> "+sqle);
      }
      return numberOfRowsDeleted;
   }
   
   public int updateUser(int user_ID, int user_type_ID, String first_name, String last_name, String password, String email, String) {
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

}