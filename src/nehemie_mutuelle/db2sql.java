/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

/**
 *
 * @author arnowod
 */
/**
 * Copyright Isocra Ltd 2004
 * You can use, modify and freely distribute this file as long as you credit Isocra Ltd.
 * There is no explicit or implied guarantee of functionality associated with this file, use it at your own risk.
 */


import java.io.FileInputStream;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.eclipse.persistence.internal.xr.Util;

/**
 * This class connects to a database and dumps all the tables and contents out to stdout in the form of
 * a set of SQL executable statements
 */
public class db2sql {

   
    /** Extract the schema
     * 
     * Taken from http://www.isocra.com/articles/db2sql.java, with additions for indexes and vendor
     * 
     * Copyright Isocra Ltd 2004
     * 
     * @param dbConn A database connection
     * @return A string representation of the schema
     * 
     * 
     */
    
     private static String join(String separator, List<String> input) {

        if (input == null || input.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.size(); i++) {

            sb.append(input.get(i));

            // if not the last item
            if (i != input.size() - 1) {
                sb.append(separator);
            }

        }

        return sb.toString();

    }
     
     public static String  sqlToString (String query, Connection conn, String columnname) throws SQLException{
                Statement st;
                StringBuilder result = new StringBuilder();
                
                result.append("data "+columnname);
                result.append('\n');
   

      
         st = conn.createStatement();
         ResultSet rs = st.executeQuery(query);

         int cols = rs.getMetaData().getColumnCount();

         for(int i = 1; i <= cols; i ++){
            result.append(rs.getMetaData().getColumnLabel(i));
            if(i < cols) result.append(',');
            else result.append('\n');
         }

         while (rs.next()) {

            for(int i = 1; i <= cols; i ++){
                result.append(rs.getString(i));
                if(i < cols) result.append(',');
            }
            result.append('\n'); 
            
         }
         result.append('\n'); 
         return result.toString();
     
     
     }
             


    public static String dumpDB(Connection dbConn) {        
        // Default to not having a quote character
        String columnNameQuote = "";
        DatabaseMetaData dbMetaData = null;
    
        try {
        	dbMetaData = dbConn.getMetaData();
            StringBuilder result = new StringBuilder();
            String catalog = null;
            String schema = null;
            String tables = null;
            
            String hostname = "localhost";
         
        
            result.append("Host: " + hostname + " " + System.getProperty("os.name") + " ");
            result.append(System.getProperty("os.version") + " ");
            result.append(System.getProperty("os.arch") + "\n");
            result.append("Database: " + dbMetaData.getDatabaseProductName() + " (" + dbMetaData.getDatabaseProductVersion() + ")\n");
            result.append("Driver name: " + dbMetaData.getDriverName() + " (" + dbMetaData.getDriverVersion() + ")\n");
            List columnnames = new ArrayList<String>();
            try( ResultSet rs = dbMetaData.getTables(catalog, schema, tables, null) ) {
                if (! rs.next()) 
                    System.err.println("Unable to find any tables matching: catalog="+catalog+" schema="+schema+" tables="+tables);
                else {
                    // Right, we have some tables, so we can go to work.
                    // the details we have are
                    // TABLE_CAT String => table catalog (may be null)
                    // TABLE_SCHEM String => table schema (may be null)
                    // TABLE_NAME String => table name
                    // TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
                    // REMARKS String => explanatory comment on the table
                    // TYPE_CAT String => the types catalog (may be null)
                    // TYPE_SCHEM String => the types schema (may be null)
                    // TYPE_NAME String => type name (may be null)
                    // SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
                    // REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
                    // We will ignore the schema and stuff, because people might want to import it somewhere else
                    // We will also ignore any tables that aren't of type TABLE for now.
                    // We use a do-while because we've already caled rs.next to see if there are any rows
                   
                    do {
                        String tableName = rs.getString("TABLE_NAME");
                        String tableType = rs.getString("TABLE_TYPE");
                        columnnames.removeAll(columnnames);
                        if ("TABLE".equalsIgnoreCase(tableType)) {
                            result.append("\n-- "+tableName);
                            result.append("\nCREATE TABLE "+tableName+" (\n");
                            try ( ResultSet tableMetaData = dbMetaData.getColumns(null, null, tableName, "%") ) {
                                boolean firstLine = true;
                                while (tableMetaData.next()) {
                                    if (firstLine) {
                                        firstLine = false;
                                    } else {
                                        // If we're not the first line, then finish the previous line with a comma
                                        result.append(",\n");
                                    }
                                    String columnName = tableMetaData.getString("COLUMN_NAME");
                                    String columnType = tableMetaData.getString("TYPE_NAME");
                                    // WARNING: this may give daft answers for some types on some databases (eg JDBC-ODBC link)
                                    int columnSize = tableMetaData.getInt("COLUMN_SIZE");
                                    String nullable = tableMetaData.getString("IS_NULLABLE");
                                    String nullString = "NULL";
                                    if ("NO".equalsIgnoreCase(nullable)) {
                                        nullString = "NOT NULL";
                                    }
                                    result.append("    "+columnNameQuote+columnName+columnNameQuote+" "+columnType+" ("+columnSize+")"+" "+nullString);
                                }
                                
                                
                                
                                
                            }

                            // Now we need to put the primary key constraint
                            try {
                                ResultSet primaryKeys = dbMetaData.getPrimaryKeys(catalog, schema, tableName);
                                // What we might get:
                                // TABLE_CAT String => table catalog (may be null)
                                // TABLE_SCHEM String => table schema (may be null)
                                // TABLE_NAME String => table name
                                // COLUMN_NAME String => column name
                                // KEY_SEQ short => sequence number within primary key
                                // PK_NAME String => primary key name (may be null)
                                String primaryKeyName = null;
                                StringBuffer primaryKeyColumns = new StringBuffer();
                                while (primaryKeys.next()) {
                                    String thisKeyName = primaryKeys.getString("PK_NAME");
                                    if ((thisKeyName != null && primaryKeyName == null)
                                        || (thisKeyName == null && primaryKeyName != null)
                                        || (thisKeyName != null && ! thisKeyName.equals(primaryKeyName))
                                        || (primaryKeyName != null && ! primaryKeyName.equals(thisKeyName))) {
                                        // the keynames aren't the same, so output all that we have so far (if anything)
                                        // and start a new primary key entry
                                        if (primaryKeyColumns.length() > 0) {
                                            // There's something to output
                                            result.append(",\n    PRIMARY KEY ");
                                            if (primaryKeyName != null) { result.append(primaryKeyName); }
                                            result.append("("+primaryKeyColumns.toString()+")");
                                        }
                                        // Start again with the new name
                                        primaryKeyColumns = new StringBuffer();
                                        primaryKeyName = thisKeyName;
                                    }
                                    // Now append the column
                                    if (primaryKeyColumns.length() > 0) {
                                        primaryKeyColumns.append(", ");
                                    }
                                    primaryKeyColumns.append(primaryKeys.getString("COLUMN_NAME"));
                                }
                                if (primaryKeyColumns.length() > 0) {
                                    // There's something to output
                                    result.append(",\n    PRIMARY KEY ");
                                    if (primaryKeyName != null) { result.append(primaryKeyName); }
                                    result.append(" ("+primaryKeyColumns.toString()+")");
                                }
                            } catch (SQLException e) {
                                // NB you will get this exception with the JDBC-ODBC link because it says
                                // [Microsoft][ODBC Driver Manager] Driver does not support this function
                                System.err.println("Unable to get primary keys for table "+tableName+" because "+e);
                            }

                            result.append("\n);\n");

                            /**
                             * Get the indexes for this table
                             */
                            try
                            {
                                ResultSet indexes = dbMetaData.getIndexInfo(null, null, tableName, false, false);
                                Map<String, List<String>> index2col = new HashMap<String,List<String>>();
                                Map<String, Boolean> index2unique = new HashMap<String, Boolean>();
                                while (indexes.next())
                                {
                                    Boolean nonUnique = indexes.getBoolean("NON_UNIQUE");
                                    String name = indexes.getString("INDEX_NAME");
                                    String col = indexes.getString("COLUMN_NAME");
                                    if (!index2col.containsKey(name))
                                        index2col.put(name, new ArrayList<String>());
                                    index2col.get(name).add(col);
                                    index2unique.put(name, !nonUnique);
                                }
                                for (String index: index2col.keySet())
                                {
                                    List<String> cols = index2col.get(index);
                                    result.append("CREATE ");
                                    if (index2unique.get(index)) result.append("UNIQUE ");
                                    result.append("INDEX " + index + " ON " + tableName + " (");
                                      List<String> myStringList = new ArrayList<String>(cols.toArray(new String[]{}).length);
                                     for (String s:cols.toArray(new String[]{})) {
                                          myStringList.add( s );
                                      }
                                    
                                    
                                    result.append(join(", ",myStringList));
                                    result.append(")\n");
                                }
                            }
                            catch (SQLException e)
                            {
                                System.err.println("Unable to get indexes for table "+tableName+" because "+e);
                            }
                            
                            
                            
                            
                            
                            String data = sqlToString ("SELECT * FROM "+tableName, dbConn, tableName);
                            result.append(data);
                            
                            
                        }
                    } while (rs.next());
                }

            }
            return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return null;
    }

 
}
