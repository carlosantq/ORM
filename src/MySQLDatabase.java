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
        
}

