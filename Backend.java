     /**
    * ISTE 330 Group 1 project backend
    * This file is responsible for communicating with 
    * the database via MySQL and handling minor communciation 
    * with the FE
    */
   import java.sql.*;
   import java.security.MessageDigest;
   import java.security.NoSuchAlgorithmException;
   import java.util.*;
   public class Backend {

      private Connection conn;
      private String url_string = "jdbc:mysql://localhost/";
      private final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
      private int current_user_ID;

      /**
       * Constructor --> shouldn't do anything here i don't think
       */
      public Backend(){}
       
      /**
        * Makes the connect to the DB
        */
      public boolean connect(String username, String password, String database){
         url_string += database;
         url_string = url_string + "?serverTimezone=UTC";

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

      /**
       * used to set the userid for the user who is currently logged in
       */
      public void setSessionUserID(String email){
         current_user_ID = getUserIDByEmail(email);
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
   
      public int updateUser(String major, String first_name, String last_name, String password, String phone, String office_number, String office_hours,  String email) {
      int records = 0;
      
      try {
         PreparedStatement stmt = conn.prepareStatement("UPDATE User set major = ?, first_name = ?, last_name = ?, password = ?, phone = ?, office_number = ?, office_hours = ?  WHERE email = ?"); 
         stmt.setString(1, major);
         stmt.setString(2, first_name);
         stmt.setString(3, last_name);
         stmt.setString(4, password);
         stmt.setString(5, phone);
         stmt.setString(6, office_number);
         stmt.setString(7, office_hours);
         stmt.setString(8, email); 
         records = stmt.executeUpdate();
      } // end of try
      
         catch(SQLException sqle) {
         System.out.println("Error --->" + sqle); 
      } // end of sql exception 
      
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

   /**
    * Returns the user_ID for the email provided 
    */
   public int getUserIDByEmail(String email){
      int res = 0;
      try{
         PreparedStatement stmt = conn.prepareStatement("SELECT user_ID FROM user WHERE user.email=?");
         stmt.setString(1, email);
         ResultSet rs = stmt.executeQuery();
         if(rs.next()){
            res = rs.getInt(1);
         }

      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return res;
   }

   /**
    * Gets a users abstracts based on the uid provided 
    */
   public List<String> getUserAbstracts(int uid){
      List<String> res = new ArrayList<String>();   //for results
      int rowCount, size = 0;
      try{
         //need to get number of rows to know how many abstracts the user has 
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Abstract JOIN user_to_abstract USING(abstract_ID) WHERE user_ID ="+uid );
         rs.next();
         rowCount = rs.getInt(1);
         
         //gets all abstracts 
         PreparedStatement stmtt = conn.prepareStatement("SELECT abstract_ID, title, abstract FROM abstract JOIN user_to_abstract USING(abstract_ID) WHERE user_ID=?");
         stmtt.setInt(1, uid);
         ResultSet rss = stmtt.executeQuery();
         
         int index = 1;
         boolean is_int = true;
         //if values are returned 
         while(rss.next()){
            for(int i=0; i < rowCount*3; i++){       
  
               if(is_int){ //every third is an int
                  res.add(String.valueOf(rss.getInt(index)));
                  is_int = false;
               } else {
                  res.add(rss.getString(index));
               }
               
               if(index % 3 == 0){
                  rss.next();
                  is_int = true;
                  index = 1;
               } else {
                  index ++;
               }          
            }
         }

      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return res;

   }


   public int updateAbstract(int abs_ID, String title, String abs, String keywords){
      int ret = 0;
      try{
         PreparedStatement stmt = conn.prepareStatement("UPDATE Abstract SET title=?, abstract=?, keywords=? WHERE abstract_ID = ?");
         stmt.setString(1, title);
         stmt.setString(2, abs);
         stmt.setString(3, keywords);
         stmt.setInt(4, abs_ID);
         ret = stmt.executeUpdate(); 

      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }

      return ret;
   }
   


   /**
    * Called when a student searches for specific keywords
    */
   public void searchByKeyword(List<String> keywords){
      //add keywords to the lookup table 
      addKeywords(keywords);

      //retrieve professor abstracts that contain that keyword
      scanAbstracts(keywords);
      //NOTE: the above method call is like a chain reaction that sets everything off

   }


   /**
    * Used to add keywords to the DB based on what was searched 
    */
   public void addKeywords(List<String> keywords){
      List<String> to_block = new ArrayList<String>(); // words to be blocked 
      to_block = populateToBlock();
      //remove bad keywords that shouldn't be added to the db 
      for(int i=0; i <keywords.size(); i++){
         for(int j=0; j < to_block.size(); j++){
            if(keywords.get(i).toLowerCase().equals(to_block.get(j).toLowerCase())){
               keywords.remove(i);
            }
         }
         //check that the keyword isn't already in the db 
         int exists = lookup_keyword(keywords.get(i));
         if(exists == 0){
            int ret = 0;
            //insert each of the keywords into the db
            try {
               PreparedStatement stmt = conn.prepareStatement("INSERT into Lookup_Keyword (keyword_type) VALUES (?)");
               stmt.setString(1, keywords.get(i));
               ret = stmt.executeUpdate();
               System.out.println("Keyword inserted.");
            } catch(SQLException s){
               System.out.println("ERROR CONNECTING\n" + s);
            }
         }
      }
   }

   /**
    * gets the keyword id based on the provided keyword
    * can be used to prevent duplicate keywords being inserted
    */
   public int lookup_keyword(String keyword){
      int res = 0;
      try{
         PreparedStatement stmt = conn.prepareStatement("SELECT keyword_ID FROM Lookup_Keyword WHERE keyword_type = ?");
         stmt.setString(1, keyword);
         ResultSet rs = stmt.executeQuery();
         if(rs.next()){
            res = rs.getInt(1);
         }
      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return res;
   }

   /**
    * Used to scan exisiting abstracts for the list of keywords provided 
    */
   public void scanAbstracts(List<String> keywords){
      int rowCount = 0;
      try{
         //get the total number of abstracts in the database
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Abstract");
         rs.next();
         rowCount = rs.getInt(1);
      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      //for each keyword in the list 
      for(int i=0; i < keywords.size(); i++){
         for(int j=rowCount; j > 0; j--){
            String abs = getAbstract(j); //retrieve one abstract at a time 
            containsWord(abs, keywords.get(i)); //check if the abstract and keyword match
         }
      }
   }

   /**
    * Returns an abstact based on its id
    */
   public String getAbstract(int id){
      String res = "";
      try{
         PreparedStatement stmt = conn.prepareStatement("SELECT abstract FROM Abstract WHERE abstract_ID= ?");
         stmt.setInt(1, id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next()){
            res = rs.getString(1);
         }
      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return res;
   
   }

   /**
    * Used to check if an absrtact contains a specific keyword
    */
   public void containsWord( String abs, String keyword){
      //split the abstract by space and remove puncuation 
      String [] split = abs.split("\\s+"); //split the abstract by space 
      for(int i=0; i<split.length; i++){
         //make lowercase and remove any puctuation
         String word = split[i].toLowerCase().replaceAll("\\p{Punct}", "");
         
         //if the word in the abs is the same as the word 
         if(keyword.equals(word)){    
            //create a connection between the professor for the given abstract and the student user who is logged in       
            insertConnection(abs, keyword); 
         }
      }
   }

   public void insertConnection(String abs, String keyword){
      int abs_ID = getAbstractID(abs);
      int [] fac_IDs =  getFacultyIDsForAbstract(abs_ID); //get all faculty id's based on the abstract id
      int keyword_ID = lookup_keyword(keyword);
      if(keyword_ID != 0){ //non zero means it exists
         //check that the connection doesn't already exist FOR EACH AUTHOR AH
         for(int i=0; i < fac_IDs.length; i++){
            int fac_ID;
            if(fac_IDs[i] != 0){
               fac_ID = fac_IDs[i];
            } else {return;}
            
            int exists = 1; // used to check if there is already an exisiting association 
            try{
               PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Connection WHERE faculty_ID=? AND student_ID=? and keyword_ID=?"); 
               stmt.setInt(1, fac_ID);
               stmt.setInt(2, current_user_ID);
               stmt.setInt(3, keyword_ID);
               ResultSet rs = stmt.executeQuery();
               if(!rs.next()){ //if there isn't something returned ?
                  exists = 0;
               }

            }catch(SQLException s){
               System.out.println("ERROR CONNECTING\n" + s);
            }
            
            if(exists == 0){ //insert new conenction 
               int ret = 0;
               try{
                  PreparedStatement stmt = conn.prepareStatement("INSERT into Connection (faculty_ID, student_ID, keyword_ID) VALUES (?, ?, ?)");
                  stmt.setInt(1, fac_ID);
                  stmt.setInt(2, current_user_ID);
                  stmt.setInt(3, keyword_ID);
                  ret = stmt.executeUpdate(); 
               }  catch(SQLException s){
                  System.out.println("ERROR CONNECTING\n" + s);
               }
               System.out.println(ret + "record(s) inserted."); // sanity check 
            } 

         }
      }
   
   }

   /**
    * used to retrieve faculty id's based on the abstract_id
    */
   public int[] getFacultyIDsForAbstract(int abs_ID){
      int[] ids = new int[3]; //only up to 3 authors 
      int index = 0; //used to know where to set the next element in the array
      try{
         PreparedStatement stmt = conn.prepareStatement("SELECT user_ID from User_To_Abstract WHERE abstract_ID = ?"); 
         stmt.setInt(1, abs_ID);
         ResultSet rs = stmt.executeQuery();
         while(rs.next()){
            ids[index] = rs.getInt(1);
            index++;           
         }

      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }      
      return ids;

   }


   /**
    * Used to get connections for a student 
    */
   public List<String> getConnections(int uid){
      List<String> res = new ArrayList<String>();
      List<Integer> fac_ids = new ArrayList<Integer>();
      try{
         //need to first retrieve all faculty id's based on the student uid provided 
         fac_ids = getFacultyIds(uid);
         //iterate over all the faculty ids
         for(int i=0; i < fac_ids.size(); i++){
            PreparedStatement stmt = conn.prepareStatement("SELECT first_name, last_name, email, cell_phone, office_hours, office_number FROM user JOIN connection ON (user_ID = faculty_ID) WHERE user_ID = ?"); 
            stmt.setInt(1, fac_ids.get(i));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
               res.add(rs.getString(1) + " " +rs.getString(2)); //concat first and last name 
               res.add(rs.getString(3)); //email
               res.add(rs.getString(4)); //cell_phone
               res.add(rs.getString(5)); //office hours
               res.add(rs.getString(6)); //office number
               //rs.next();
            }
         }

      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return res;
   }

   /**
    * Used to get faculty id's for a specific student id from the connection table
    */
   public List<Integer> getFacultyIds(int uid){
      List<Integer> res = new ArrayList<Integer>();
      try{
         PreparedStatement stmt = conn.prepareStatement("SELECT faculty_ID FROM connection WHERE student_ID = ?"); 
         stmt.setInt(1, uid);
         ResultSet rs = stmt.executeQuery();
         
         while(rs.next()){
            res.add(Integer.valueOf(rs.getInt(1)));
         }

      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }
      return res;
   }

   /**
    * used to filter out words that should be blocked so that they 
    * are not added as keywords
    * may need to be added on to idk
    */
   public List<String> populateToBlock(){
      List<String> ret = new ArrayList<String>();
      ret.add("the"); ret.add("is"); ret.add("on"); ret.add("a"); ret.add("the"); ret.add("to");
      ret.add("we");ret.add("be"); ret.add("or"); ret.add("This"); ret.add("There");
      return ret;
   }


   /**
    * Used to retrieve conenctions from the db 
    */
   public void viewMatches(int type_ID){
      //based on the type_ID of the user, search for the appropriate ID in the connection table 
      List<Integer> res = new ArrayList<Integer>();
      if(type_ID == 1){ //if professor 
         System.out.println("MATCHES\n______________________________________________________");   //header  
         try{
            //select student first_name, last_name, email, major, keywords in common 
            //user to connection to keywords
            //first get the user data
            PreparedStatement stmt = conn.prepareStatement("SELECT user_ID, first_name, last_name, email, cell_phone FROM User JOIN Connection ON(User.user_ID = Connection.student_ID) WHERE faculty_ID = ?"); 
            stmt.setInt(1, current_user_ID);
            ResultSet rs = stmt.executeQuery();
            
            //for each row in the db that is returned we will need to scan for the necesarry keyword_types that 
            //correspond with the appropriate keyword type
            while(rs.next()){
               System.out.println(rs.getString(3) + ", " + rs.getString(2) + " | " + rs.getString(4) + " | " + rs.getString(5));
               getConnectionKeywords(current_user_ID, rs.getInt(1));
            }

         }catch(SQLException s){
            System.out.println("ERROR CONNECTING\n" + s);
         }

      } else if(type_ID == 2){ // if student 
         System.out.println("Student: MATCHES\n______________________________________________________");
         try {
         // select professor first_name, last_name, email, major, keywords in common 
         PreparedStatement stmt = conn.prepareStatement("SELECT first_name, last_name, email, cell_phone, department_ID, office_number, office_hours FROM User JOIN Connection ON(User.user_ID = Connection.faculty_ID) WHERE student_ID = ?");
         stmt.setInt(1, current_user_ID);
         ResultSet rs = stmt.executeQuery();
         
         while(rs.next()) {
         System.out.println(rs.getString(2) + ", " + rs.getString(1) + " | " + rs.getString(3) + " | " + rs.getString(4) + " | " + rs.getString(5) + " | " + rs.getString(6) + " | " + rs.getString(7)); 
         getConnectionKeywords(current_user_ID, rs.getInt(2));  
         
         } // end of while  
        
         
         } // end of try 
         
         catch (SQLException s) {
            System.out.println("ERROR CONNECTING\n" + s); 
         } // end of catch
         
      } else {
         System.out.println("No type ID found.");
      }
      // return res;
   }

   public void getConnectionKeywords(int faculty_ID, int student_ID){
      try{
         //select keyword type 
         PreparedStatement stmt = conn.prepareStatement("SELECT keyword_type FROM Lookup_Keyword JOIN Connection USING(keyword_ID) WHERE faculty_ID = ? AND student_ID = ?"); 
         stmt.setInt(1, faculty_ID);
         stmt.setInt(2, student_ID);
         ResultSet rs = stmt.executeQuery();
         
         //for each row returned there will be a keyword associated 
         while(rs.next()){
            System.out.print("\tKeywords: "+ rs.getString(1) + " ");
         }

      }catch(SQLException s){
         System.out.println("ERROR CONNECTING\n" + s);
      }

   }

   }
      
   
   