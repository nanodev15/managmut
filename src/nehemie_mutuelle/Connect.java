/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elommarcarnold
 */
public class Connect {
    static PreparedStatement pre =null;
    static Connection conn=null;
    public static Connection ConnectDb() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
          // Connection conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/mutuelle2", "root", "ejbcalogin");
          Connection conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/mutuelle", "root", "ejbcalogin");

             return conn;
        } catch (Exception e) {
             e.printStackTrace();
            return null;
        }
    }
    
    public static Connection ConnectDb2() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
  //          Connection conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/mutuelle2?user=root&password=ejbcalogin&sessionVariables=sql_mode='STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,PIPES_AS_CONCAT'");
            Connection conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/mutuelle?user=root&password=ejbcalogin&sessionVariables=sql_mode='STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,PIPES_AS_CONCAT'");

             return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void close(Connection conn, ResultSet rs, PreparedStatement pre) throws SQLException {
        conn.close();
        rs.close();
        pre.close();
    }
    
   public void MySQLDumpee(String host, String username, String password, String db) throws SQLException, IOException {
        try{
          MySQLDump per;
          per = new  MySQLDump(host, username, password, db);
          per.doMain();
        }
        catch (SQLException se){
            throw se;
        }
    }
    
    
    class MySQLDump {

    //Command Line Arguments

    private boolean help;
  
    private String hostname;
  
    private String username;
    private String password;
    // receives other command line parameters than options
    private List arguments = new ArrayList();

    private String version = "0.1";
    private String database = null;
    private Connection conn = null;
    private DatabaseMetaData databaseMetaData;
    private String databaseProductVersion = null;
    private String mysqlVersion = null;

    /**
    * Default contructor for MySQLDump.
    */
    public MySQLDump() {

    }
    
    

    /**
    * Create a new instance of MySQLDump using default database.
    *
    * @param  host      MySQL Server Hostname
    * @param  username  MySQL Username
    * @param  password  MySQL Password
    */
    public MySQLDump(String host, String username, String password) throws SQLException {
        try{
            connect(host, username, password, "mysql");
          
        }
        catch (SQLException se){
            throw se;
        }

    }

    /**
    * Create a new instance of MySQLDump using supplied database.
    *
    * @param  host      MySQL Server Hostname
    * @param  username  MySQL Username
    * @param  password  MySQL Password
    * @param  db        Default database
    */
    public MySQLDump(String host, String username, String password, String db) throws SQLException{
        try{
            connect(host, username, password, db);
          
        }
        catch (SQLException se){
            throw se;
        }
    }

    /**
    * Connect to MySQL server
    *
    * @param  host      MySQL Server Hostname
    * @param  username  MySQL Username
    * @param  password  MySQL Password
    * @param  db        Default database
    */
    public void connect(String host, String usernam, String passwor, String db) throws SQLException{
        try
        {
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection ("jdbc:mysql://" + host + "/" + db, usernam, passwor);
            databaseMetaData = conn.getMetaData();
            databaseProductVersion = databaseMetaData.getDatabaseProductVersion();
            hostname = host;
            database = db;
            password =passwor;
            username = usernam;
            System.out.println ("Database connection established");
        }
        catch (SQLException se){
            throw se;
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
        }
    }

    public File dumpAllDatabases(){
        return null;
    }

    public String dumpCreateDatabase(String database) {
        String createDatabase = null;
        try{
            Statement s = conn.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            s.executeQuery ("SHOW CREATE DATABASE " + database);
            ResultSet rs = s.getResultSet ();
            while (rs.next ())
            {
                createDatabase = rs.getString("Create Database") + ";";
            }
        } catch (SQLException e) {

        }
        return createDatabase;
    } 

    public File dumpDatabase(String database){
        return null;
    }

    public File dumpAllTables(String database){
        return null;
    }

    public String dumpCreateTable(BufferedWriter out, String table) throws IOException {
        String createTable = null;
        try{
            out.write("\n\n--\n-- Table structure for table `" + table + "`\n--\n\n");
            Statement s = conn.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            s.executeQuery ("SHOW CREATE TABLE " + table);
            ResultSet rs = s.getResultSet ();
            while (rs.next ())
            {
                createTable = rs.getString("Create Table") + ";";
            }
        } catch (SQLException e) {

        } catch(IOException e){
            System.err.println (e.getMessage());
        }
        return createTable;
    }

    public void dumpTable(BufferedWriter out, String table){
         try{
            Statement s = conn.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            out.write("\n\n--\n-- Dumping data for table `" + table + "`\n--\n\n");
            s.executeQuery ("SELECT /*!40001 SQL_NO_CACHE */ * FROM " + table);
            ResultSet rs = s.getResultSet ();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            String prefix = new String("INSERT INTO " + table + " (");
            for (int i = 1; i <= columnCount; i++) {
                if (i == columnCount){
                    prefix += rsMetaData.getColumnName(i) + ") VALUES(";
                }else{
                    prefix += rsMetaData.getColumnName(i) + ",";
                }
            }
            String postfix = new String();
            int count = 0;
            while (rs.next ())
            {

                postfix = "";
                for (int i = 1; i <= columnCount; i++) {
                    if (i == columnCount){
                        System.err.println(rs.getMetaData().getColumnClassName(i));
                        postfix += "'" + rs.getString(i) + "');\n";
                    }else{

                        System.err.println(rs.getMetaData().getColumnTypeName(i));
                        if (rs.getMetaData().getColumnTypeName(i).equalsIgnoreCase("LONGBLOB")){
                            try{
                                postfix += "'" + escapeString(rs.getBytes(i)).toString() + "',";
                            }catch (Exception e){
                                postfix += "NULL,";
                            }
                        }else{
                            try{
                                postfix += "'" + rs.getString(i).replaceAll("\n","\\\\n").replaceAll("'","\\\\'") + "',";
                            }catch (Exception e){
                                postfix += "NULL,";
                            }
                    }   }
                }
                out.write(prefix + postfix + "\n");
                ++count;
            }
            rs.close ();
            s.close();
        }catch(IOException e){
            System.err.println (e.getMessage());
        }catch(SQLException e){
            System.err.println (e.getMessage());
        }
    }

    public File dumpAllViews(String database) {
        return null;
    }

    public String dumpCreateView(String view) {
        return null;
    }

    public File dumpView(String view) {
        return null;
    }

    /**
    * Escape string ready for insert via mysql client
    *
    * @param  bIn       String to be escaped passed in as byte array
    * @return bOut      MySQL compatible insert ready ByteArrayOutputStream
    */
    private ByteArrayOutputStream escapeString(byte[] bIn){
        int numBytes = bIn.length;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes+ 2);
        for (int i = 0; i < numBytes; ++i) {
            byte b = bIn[i];

            switch (b) {
            case 0: /* Must be escaped for 'mysql' */
                    bOut.write('\\');
                    bOut.write('0');
                    break;

            case '\n': /* Must be escaped for logs */
                    bOut.write('\\');
                    bOut.write('n');
                    break;

            case '\r':
                    bOut.write('\\');
                    bOut.write('r');
                    break;

            case '\\':
                    bOut.write('\\');
                    bOut.write('\\');

                    break;

            case '\'':
                    bOut.write('\\');
                    bOut.write('\'');

                    break;

            case '"': /* Better safe than sorry */
                    bOut.write('\\');
                    bOut.write('"');
                    break;

            case '\032': /* This gives problems on Win32 */
                    bOut.write('\\');
                    bOut.write('Z');
                    break;

            default:
                    bOut.write(b);
            }
        }
        return bOut;
    }

    private String getHeader(){
        //return Dump Header
        return "-- BinaryStor MySQL Dump " + version + "\n--\n-- Host: " + hostname + "    " + "Database: " + database + "\n-- ------------------------------------------------------\n-- Server Version: " + databaseProductVersion + "\n--";
    }

    /**
    * Main entry point for MySQLDump when run from command line
    *
    * @param  args  Command line arguments
    */
 

    /**
    * Parse command line arguments and run MySQLDump
    *
    * @param  args  Command line arguments
    */
    public void doMain() throws IOException {

        //Do we have a hostname? if not use localhost as default
        if (hostname == null){
            hostname = "localhost";
        }
        //First argument here should be database
   //     database = (String) arguments.remove(0);
        database = "mutuelle2";
        try{
            //Create temporary file to hold SQL output.
            File temp = File.createTempFile(database, ".sql");
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            System.out.println("pass"+password);
            System.out.println("database"+database);
            this.connect(hostname, username, password, database);
            out.write(getHeader());
            
            out.write(dumpCreateTable(out,"blobdump"));
            this.dumpTable(out,"blobdump");
            out.close();
            this.cleanup();
        }
        catch (SQLException se){
            System.err.println (se.getMessage());
        }
    }

    public int cleanup(){
        try
        {
            conn.close ();
            System.out.println ("Database connection terminated");
        }
        catch (Exception e) { /* ignore close errors */ }
        return 1;
    }

}
    
}
