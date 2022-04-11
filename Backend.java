   /**
    * ISTE 330 Group 1 project backend
    * This file is responsible for communicating with 
    * the database via MySQL and handling minor communciation 
    * with the FE
    */
   import java.sql.*;
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
         //hash the password 
         //password = SHA1(password);
         int ret = 0;
         try {
            //Still need to implement password hashing 2
            PreparedStatement stmt = conn.prepareStatement("Insert into User (user_type_ID, first_name, last_name, password, email, department_ID, major, office_number, office_hours) VALUES (?,?,?,?,?,?,?,?,?) ");
            stmt.setInt(1, user_type_ID);
            stmt.setString(2, first_name);
            stmt.setString(3, last_name);
            stmt.setString(4, password);
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


}