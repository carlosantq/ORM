/**
  * Date:      April 16, 2016
  * @author    Carlos Antonio de Oliveira Neto
*/
import java.io.*;
import java.sql.*;

public class DLException{
   
   /** DLException class attributes **/
   Exception e;
   String[] s;
   
   /** Constructor with single exception parameter **/
   public DLException (Exception _e){
      e = _e;
      log();
   }
   
   /** Constructor with exception and strings parameters **/
   public DLException (Exception _e, String... _s){
      e = _e;
      s = _s;
      log();
   }
   
   /**
      Creates a log file.
   **/
   public void log(){
      try{
         /** Create/Choose the file **/
         FileWriter fw = new FileWriter ("../log/logfile.txt", true);
         PrintWriter pw = new PrintWriter (fw);
         
         /** Timestamp that will be displayed **/
         java.util.Date date= new java.util.Date();
         Timestamp t = new Timestamp(date.getTime());
         
         /** Loop to write timestamp, exceptions and strings to the file **/
         for (int i=0; i<s.length; i++){
            pw.write("\n" + t.toString() + "\n");
            e.printStackTrace(pw);
            e.toString();
            pw.write(s[i] + "\n");
         }
         
         pw.flush();
         pw.close();
                     
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
      }catch (IOException ioe){
         System.out.println("Error writing file");
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
      }
   }
   
}