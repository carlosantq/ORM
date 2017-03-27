/**
  * Date:      May 3, 2016
  * @author    Carlos Antonio de Oliveira Neto
*/
import java.io.*;
import java.sql.*;
import java.util.*;

public class Orm{
   
   /** name of the sql dabatase (not the name of the sql file!!) **/
   String databaseName;
   
   /** Parametrized Constructor **/
   public Orm(String _databaseName){
      databaseName = _databaseName;
      createFile(databaseName);
   }
   
   /**
      Method that creates a java file for each table on the database
      @param String databaseName
   **/
   public void createFile(String databaseName){
      /** Creates a MySQLDatabase object **/
      MySQLDatabase mysqld = new MySQLDatabase("jdbc:mysql://127.0.0.1:3307/"+databaseName, "root", "sip12e12");
      /** Using the getData method, gets the name of the tables on the database **/
      ArrayList<ArrayList<String>> tables = mysqld.getData("show tables");
      
      /** Convert an ArrayList<ArrayList<String>> to a String array **/
      String[][] table = new String[tables.size()][];
      for (int i = 0; i < tables.size(); i++){
         table[i] = tables.get(i).toArray(new String[tables.get(i).size()]);
      }
   
      try{
         for (int i=0; i<table.length; i++){
         
            /** Creates a .java file for each table **/
            String file = table[i][0].toString()+".java";
            /** First letter of class name is uppercase **/
            file = file.substring(0, 1).toUpperCase() + file.substring(1, file.length());
         
            /** Creats a directory using the database name **/
            File dir = new File(databaseName);
            dir.mkdirs();
            File newfile = new File(dir, file);
            FileWriter fw = new FileWriter (newfile, false);
            PrintWriter pw = new PrintWriter(fw);
            
            /** Takes the .java out of the name of the file **/
            String filename = file.substring(0, file.length()-5);
            
            /** Methods used to create the files **/
            createHeader(newfile);
            startFile(newfile, filename);
            attributes(newfile, filename, mysqld, table[i][0]);
            endFile(newfile);
            
            pw.flush();
            pw.close();
         }
         
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
      }catch (IOException ioe){
         System.out.println("Error writing file");
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
      }
   }
   
   /**
      Creates a header on each file including the name of the creator, how it was created, 
      date, time, and imports the java librares .lang and .util
      @param File file
      @return True or False
   **/
   public boolean createHeader (File file){
      try{
         FileWriter fw = new FileWriter (file, true);
         PrintWriter pw = new PrintWriter(fw);
         
         java.util.Date date= new java.util.Date();
         Timestamp t = new Timestamp(date.getTime());
               
         pw.write("//\tCreated by: Carlos Antonio\n//\tFile: " + file + "\n//\tCreated using: Orm.java\n//\tDate and time of creation: " + t.toString() + "\n\n");
         pw.write("import java.lang.*;\nimport java.util.*;\n\n");
         
         pw.flush();
         pw.close();
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
         return false;
      }catch (IOException ioe){
         System.out.println("Error writing file");
         return false;
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
         return false;
      }
      
      return true;
   }
   
   /**
      Creates the beggining of the class
      @param File file
      @param String filename
      @return True or False
   **/
   public boolean startFile (File file, String filename){
      try{
         FileWriter fw = new FileWriter (file, true);
         PrintWriter pw = new PrintWriter(fw);
         
         pw.write("public class " + filename.substring(0, 1).toUpperCase() + filename.substring(1, filename.length()) + "{\n");
         
         pw.flush();
         pw.close();
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
         return false;
      }catch (IOException ioe){
         System.out.println("Error writing file");
         return false;
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
         return false;
      }
      
      return true;
   }
   
   /**
      Creates the attributes of each file according to the attributes on its respective tables
      @param File file
      @param String filename
      @param MySQLDatabase mysqld
      @param String table
      @return True or False
   **/
   public boolean attributes(File file, String filename, MySQLDatabase mysqld, String table){
      
      mysqld.connect();
      try{
         ResultSet rs = null;
         Statement stmnt = mysqld.conn.createStatement();
         rs = stmnt.executeQuery("select * from "+table);
         
         ResultSetMetaData rsmd = rs.getMetaData();
         int numFields = rsmd.getColumnCount();
         
         /** These arrays will be useful to other methods **/
         String[] types = new String[numFields];
         String[] attr = new String [numFields];
         
         for (int i=1; i<=numFields; i++){
            try{
               
               String attribute = rsmd.getColumnLabel(i);
               
               FileWriter fw = new FileWriter (file, true);
               PrintWriter pw = new PrintWriter(fw);
               
               /** This works for numeric, varchar and date data types **/
               if (rsmd.getColumnTypeName(i) != "VARCHAR" && rsmd.getColumnTypeName(i) != "DATE"){
                  types[i-1] = rsmd.getColumnTypeName(i).toLowerCase();
                  attr[i-1] = attribute;
                  pw.write("\n\t" + rsmd.getColumnTypeName(i).toLowerCase() + " " + attribute + ";");
               }else if (rsmd.getColumnTypeName(i) == "VARCHAR"){
                  types[i-1] = "String";
                  attr[i-1] = attribute;
                  pw.write("\n\tString " + attribute + ";");
               }else if (rsmd.getColumnTypeName(i) == "DATE"){
                  types[i-1] = "Date";
                  attr[i-1] = attribute;
                  pw.write("\n\tDate " + attribute + ";");
               }
               
               pw.flush();
               pw.close();
            
            }catch(FileNotFoundException fnfe){
               System.out.println("File not found");
               return false;
            }catch (IOException ioe){
               System.out.println("Error writing file");
               return false;
            }catch (ArrayIndexOutOfBoundsException aioobe){
               System.out.println("Array out of bounds");
               return false;
            }
         } 
         
         /** Calls other methods that needs attributes' information **/
         constructor(file, filename, attr, types);
         accessor(file, attr, types);
         mutator(file, attr, types);
         toString(file, attr, types);
          
      }catch(SQLException sqle){
         DLException dle = new DLException(sqle, "Unable to conclude this operation");
         return false;
      }

      return true;
   }
   
   /**
      Creates a default constructor
      @param File file
      @param String filename
      @param String[] attr
      @param String[] types
      @return True or False
   **/
   public boolean constructor (File file, String filename, String[] attr, String[] types){
      
      try{
         FileWriter fw = new FileWriter (file, true);
         PrintWriter pw = new PrintWriter(fw);
         
         /** Creates the constructor **/
         pw.write("\n\n\tpublic " + filename + "(){\n");
         
         for (int i=0; i<attr.length; i++){
            
            /** Put 0 or null on the attributes' values according to their types (numeric or Date/String) **/
            pw.write("\t\t" + attr[i] + " = ");
            if (types[i] != "String" && types[i] != "Date"){
               pw.write("0;\n");
            }else{
               pw.write("null;\n");
            }
         
         }
         
         pw.write("\t}\n");
         
         pw.flush();
         pw.close();
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
         return false;
      }catch (IOException ioe){
         System.out.println("Error writing file");
         return false;
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
         return false;
      }
      
      return true;
   }
   
   /**
      Creates an accessor for each attribute
      @param File file
      @param String[] attr
      @param String[] types
      @return True or False
   **/
   public boolean accessor (File file, String[] attr, String[] types){
      try{
         FileWriter fw = new FileWriter (file, true);
         PrintWriter pw = new PrintWriter(fw);
         
         for (int i=0; i<attr.length; i++){
            /** Method signature **/
            pw.write("\n\tpublic " + types[i] + " get" + attr[i].substring(0, 1).toUpperCase() + attr[i].substring(1, attr[i].length()) + "(){\n");
            /** Return **/
            pw.write("\t\treturn " + attr[i] + ";\n\t}\n");
         }
         
         pw.flush();
         pw.close();
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
         return false;
      }catch (IOException ioe){
         System.out.println("Error writing file");
         return false;
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
         return false;
      }
      
      return true;
   }
   
   /**
      Creates a mutator for each attribute
      @param File file
      @param String[] attr
      @param String[] types
      @return True or False
   **/
   public boolean mutator (File file, String[] attr, String[] types){
     try{
         FileWriter fw = new FileWriter (file, true);
         PrintWriter pw = new PrintWriter(fw);
         
         for (int i=0; i<attr.length; i++){
            /** Method signature **/
            pw.write("\n\tpublic void set" + attr[i].substring(0, 1).toUpperCase() + attr[i].substring(1, attr[i].length()) + " (" + types[i] + " " + attr[i] + "){\n");
            /** Assigning values **/
            pw.write("\t\t" + "this." + attr[i] + " = " + attr[i] + ";\n\t}\n");    
         }
         
         pw.flush();
         pw.close();
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
         return false;
      }catch (IOException ioe){
         System.out.println("Error writing file");
         return false;
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
         return false;
      }
      
      return true; 
   }
   
   /**
      Creates a toString method for the class
      @param File file
      @param String[] attr
      @param String[] types
      @return True or False
   **/
   public boolean toString (File file, String[] attr, String[] types){
      String result = "";
      String newString = "";
      
      try{
         FileWriter fw = new FileWriter (file, true);
         PrintWriter pw = new PrintWriter(fw);
         
         /** Method signature **/
         pw.write("\n\tpublic String toString(){\n\t\treturn ");
         
         /** Return **/
         for (int i=0; i<attr.length; i++){
            if (i != attr.length-1){
               newString = "\"" + attr[i] + " = " + "\"" + " + " + attr[i] + " + ";
            }else{
               newString = "\"" + attr[i] + " = " + "\"" + " + " + attr[i];
            }
            result += newString; 
         }
         
         pw.write(result + ";\n\t}");
         
         
         pw.flush();
         pw.close();
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
         return false;
      }catch (IOException ioe){
         System.out.println("Error writing file");
         return false;
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
         return false;
      }
      
      return true;
   }

   /**
      Ends the class
      @File file
      @return True or False
   **/   
   public boolean endFile (File file){
      try{
         FileWriter fw = new FileWriter (file, true);
         PrintWriter pw = new PrintWriter(fw);
         
         pw.write("\n}");
         
         pw.flush();
         pw.close();
      }catch(FileNotFoundException fnfe){
         System.out.println("File not found");
         return false;
      }catch (IOException ioe){
         System.out.println("Error writing file");
         return false;
      }catch (ArrayIndexOutOfBoundsException aioobe){
         System.out.println("Array out of bounds");
         return false;
      }
      
      return true;
   }
   
   public static void main (String [] args){
   
      /* get the name of the database from the command line and create an Orm object */
      Orm orm = new Orm(args[0]);
      
   }
}