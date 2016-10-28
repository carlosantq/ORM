/**
  * Date:      April 9, 2016
  * @author    Carlos Antonio de Oliveira Neto
*/
import java.sql.*;
import java.util.*;

public class MySQLDatabase{

   /** MySQLDatabase class attributes **/
   String uri;
   String user;
   String password;
   Connection conn = null;

   /**
      Constructor of the class that sets the database, username and password.
      @param _uri Database
      @param _user Username
      @param _password Password
   **/
   public MySQLDatabase(String _uri, String _user, String _password){
      uri = _uri;
      user = _user;
      password = _password;
   }
   
   /**
      Method used to try to connect to the given database.
   **/
   public boolean connect(){
      try{
         conn = DriverManager.getConnection(uri, user, password);
      }catch (SQLException sqle){
         DLException dle = new DLException(sqle, "Unable to conclude this operation");
         return false;
      }
      return true;
   }
   
   /**
      Method used to try to close the database connection.
   **/
   public boolean closeConnection(){
      try{
         conn.close();
      }catch(SQLException sqle){
         DLException dle = new DLException(sqle, "Unable to conclude this operation");
         return false;
      }
      
      return true;
   }
   
   /**
      This method is used by all getData to do all their work that they have in common.
   **/
   public ArrayList<ArrayList<String>> commonGetData (String sqlString, boolean booleanValue, PreparedStatement stmnt){
      /* Creates a 2-d ArrayList that will be used to return the results. */
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      
      /* Try to connect to the database */
      if (connect() == true){
         System.out.println("MySQL Database connected!");
      }else{
         System.out.println("Cannot connect to the given MySQL database");
      }
      
      try{
         /* Try to execute successfully the select statement. */
         ResultSet rs = null;
         if (stmnt == null){
            Statement stmnt2 = conn.createStatement();
            rs = stmnt2.executeQuery(sqlString);
         }else{
            rs = stmnt.executeQuery();
         } 
        
         /* Use Metadata do determine the number of fields requested */
         ResultSetMetaData rsmd = rs.getMetaData();
         int numFields = rsmd.getColumnCount();
         
         /* While there are rows... */
         while(rs.next()){
            /* Creates a temporary 1-d ArrayList of Strings to catch the attribute values */
            ArrayList<String> partialResult = new ArrayList<String>();
            
            /* If the booleanValue is true, the first row to be added contains the column names. */
            if (booleanValue == true){
               for (int j=1; j<=numFields; j++){
                  partialResult.add(rsmd.getColumnLabel(j));
               }
               partialResult.add("\n");
            }
            
            /* Add the attribute values to the 1-d ArrayList */
            for (int i=1; i<=numFields; i++){
               partialResult.add(rs.getString(i));
            }
            
            /* Add the 1-d ArrayList of Strings to the 2-d ArrayList */
            result.add(partialResult);
         }
      }catch(SQLException sqle){
         DLException dle = new DLException(sqle, "Unable to conclude this operation");
         return null;
      }
      
      /* Try to close the database */
      if (closeConnection() == true){
         System.out.println("MySQL Database closed!");
      }else{
         System.out.println("Cannot close the given MySQL database");
      }
      
      /* Returns the final 2-d ArrayList */
      return result;
   }
   
   /** 
      getData accepts a SQL string. Then this method returns a 2-d
      ArrayList. This will be used for doing "SELECT" sql statements.
      @param sqlStrign The sql select statement
   **/
   public ArrayList<ArrayList<String>> getData(String sqlString){
   
      /* Temporary statement */
      PreparedStatement stmnt = null;
   
      /* Creates a 2-d ArrayList that will be used to return the results from the commonGetData. */
       ArrayList<ArrayList<String>> result = commonGetData(sqlString, false, stmnt);     
      
      /* Returns the final 2-d ArrayList */
      return result;
     }
     
   /** 
      This new getData accepts a SQL string and a boolean value. Then this method returns a 2-d
      ArrayList. If the boolean value is true, the first row will contain column names, 
      if is false, will return the same thing as the first getData method.
      @param sqlStrign The sql select statement
      @param booleanValue True or False
   **/
   public ArrayList<ArrayList<String>> getData(String sqlString, boolean booleanValue){
   
      /* Temporary statement */
      PreparedStatement stmnt = null;
   
      /* Creates a 2-d ArrayList that will be used to return the results from the commonGetData. */
      ArrayList<ArrayList<String>> result = commonGetData(sqlString, booleanValue, stmnt);
      
      /* Returns the final 2-d ArrayList */
      return result;
     }
     
   /**
      This third getData method accepts a SQL string and an ArrayList of string values, then it calls
      the "prepare" method, executes the statement, and returns a 2-d ArrayList.
   **/
   public ArrayList<ArrayList<String>> getData (String sqlString, ArrayList<String> stringValues){
      /* Creates a Statement object using the prepare method */
      PreparedStatement stmnt = prepare(sqlString, stringValues);
      
      /* Creates a 2-d ArrayList that will be used to return the results from the commonGetData. */
      ArrayList<ArrayList<String>> result = commonGetData(sqlString, true, stmnt);
      
      /* Returns the final 2-d ArrayList */
      return result;
   }
   
   /** 
      setData accepts an SQL string and returns a boolean if the operation was performed or not. This will be
      used for doing "UPDATE", "DELETE", and "INSERT" operations.
      @param sqlString The sql update, delete or insert statement
      @return true or false
   **/  
   public boolean setData(String sqlString){
   
      /* Try to connect to the database */
      if (connect() == true){
         System.out.println("MySQL Database connected!");
      }else{
         System.out.println("Cannot connect to the given MySQL database");
      }
      
      try{
         /* Try to execute the statement */
         Statement stmnt = conn.createStatement();
         boolean rc = stmnt.execute(sqlString);
      }catch(SQLException sqle){
         /* If fails, returns false */
         DLException dle = new DLException(sqle, "Unable to conclude this operation");
         return false;
      }
      
      /* Try to close the database */
      if (closeConnection() == true){
         System.out.println("MySQL Database closed!");
      }else{
         System.out.println("Cannot close the given MySQL database");
      }
      
      /* If the operation was performed, returns true */
      return true;
   }
   
   /** 
      This new setData accepts an SQL string and an ArrayList of string values and returns a boolean if the 
      operation was performed or not. This will be used for doing "UPDATE", "DELETE", and "INSERT" operations.
      @param sqlString The sql update, delete or insert statement
      @param stringValues To be used by the prepare method
      @return true or false
   **/  
   public boolean setData(String sqlString, ArrayList<String> stringValues){
   
      /* Try to connect to the database */
      if (connect() == true){
         System.out.println("MySQL Database connected!");
      }else{
         System.out.println("Cannot connect to the given MySQL database");
      }
      
      try{
         /* Try to execute the statement */
         PreparedStatement stmnt = prepare (sqlString, stringValues);
         boolean rc = stmnt.execute();
      }catch(SQLException sqle){
         /* If fails, returns false */
         DLException dle = new DLException(sqle, "Unable to conclude this operation");
         return false;
      }
      
      /* Try to close the database */
      if (closeConnection() == true){
         System.out.println("MySQL Database closed!");
      }else{
         System.out.println("Cannot close the given MySQL database");
      }
      
      /* If the operation was performed, returns true */
      return true;
   }
   
   /**
      descTable is a method used to allow the execution of any SELECT query and print the result with 
      the column names and column types.
      @param selectQuery The sql select statement 
   **/
   public void descTable(String selectQuery){
      
      /* Try to connect to the database */
      if (connect() == true){
         System.out.println("MySQL Database connected!");
      }else{
         System.out.println("Cannot connect to the given MySQL database");
      }
   
      try{
         Statement stmnt = conn.createStatement();
         ResultSet rs = stmnt.executeQuery(selectQuery);
         
         //Creates a ResultMetaData object and use it to get metadata from the ResultSet
         ResultSetMetaData rsmd = rs.getMetaData();
         //Get the number of columns of the select statement
         int numFields = rsmd.getColumnCount();
         //Variable used to save the biggest width along the columns of the select statement
         int biggestWidth = 0;
         
         //Both vectors will store the column names and its types
         String names[] = new String[numFields];
         String types[] = new String[numFields];
         for (int i=1; i<=numFields; i++){
         
            if (rsmd.getColumnDisplaySize(i) > biggestWidth){
               biggestWidth = rsmd.getColumnDisplaySize(i);
            }
            
            names[i-1] = rsmd.getColumnName(i);
            types[i-1] = rsmd.getColumnTypeName(i);
         }
         
         //Prints number of fields, column names and column types
         System.out.println("Number of Fields: " + numFields);
         for (int i=1; i<=numFields; i++){
            System.out.println("Column " + i + ": " + names[i-1]);
            System.out.println("Type " + i + ": " + types[i-1]);
         }
         
         //If the biggestWidth is too big, it is changed to 30, a reasonable size
         if (biggestWidth > 30){
            biggestWidth = 30;
         }
         
         //Print the headers with column names
         for (int i=1; i<=numFields; i++){
            if (i==numFields){
                  System.out.print(String.format("%" + biggestWidth + "s", names[i-1] + "\n")); 
               }else if (i==1){
                  System.out.print(names[i-1] + "\t\t");
               }else{
                  System.out.print(String.format("%" + biggestWidth + "s", names[i-1])); 
               } 
         }
         
         //Print the rows
         while (rs.next()){
            for (int i=1; i<=numFields; i++){
               if (i==numFields){
                  System.out.print(String.format("%" + biggestWidth + "s", rs.getString(i) + "\n"));
               }else if (i==1){
                  System.out.print(rs.getString(i) + "\t");
               }else{
                  System.out.print(String.format("%" + biggestWidth + "s", rs.getString(i)));
               }   
            }
         }
         
      }catch (SQLException sqle){
         System.out.println("Exception: " + sqle);
      }
   }
   
   /**
      This method accepts a string and an ArrayList of string values. The string that is passed in 
      represents a prepared statement to be executed. 
      @param sqlString The sql statement
      @param stringValues To be used by the prepare method
   **/
   public boolean executeStmnt (String sqlString, ArrayList<String> stringValues){
      boolean rc = false;
      
      connect();
      
      try{
         PreparedStatement stmnt = prepare(sqlString, stringValues);
         rc = stmnt.execute();
      }catch (SQLException sqle){
         System.out.println("Error: " + sqle);
         return false;
      }
      
      closeConnection();
      
      return true;
   }
   
   /**
      This method accepts a SQL string and an ArrayList of string values, prepares it,
      binds the values, and returns a prepared statement.
      @param sqlString The sql statement
      @param stringValues To be used with the statement
   **/
   public PreparedStatement prepare (String sqlString, ArrayList<String> stringValues){
      connect();
      
      PreparedStatement ps = null;
      //ResultSet rs = null;
      
      try{
         ps = conn.prepareStatement(sqlString);
         
         System.out.println("Prepared statement before bind variables set: \n\t" + ps.toString());
         
         for (int i=0; i<stringValues.size(); i++){
            ps.setString(i+1, stringValues.get(i));
         }
         
         System.out.println("Prepared statement after bind variables set: \n\t" + ps.toString());

         //rs = ps.executeQuery();
         //ps.execute();
      }catch (SQLException sqle){
         System.out.println("Exception: " + sqle);
      }
      
      return ps;
   }
   
   /**
      This method will start a transaction.
      @return true or false
   **/
   public boolean startTrans(){
      connect();
      
      try{
         conn.setAutoCommit(false);
      }catch (SQLException sqle){
         System.out.println("Error1: " + sqle);
         return false;
      }
      
      return true;
   }
   
   /**
      This method will end a transaction.
      @return true or false
   **/
   public boolean endTrans(){
   
      connect();
      
      try{
         conn.setAutoCommit(true);
      }catch (SQLException sqle){
         System.out.println("Error2: " + sqle);
         return false;
      }
      
      return true;
   }
   
   /**
      This method will rollback a transaction.
      @return true or false;
   **/
   public boolean rollbackTrans(){
      try{
         conn.rollback();
      }catch (SQLException sqle){
         System.out.println("Error3: " + sqle);
         return false;  
      }
      
      return true;
   }
        
}

