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

}