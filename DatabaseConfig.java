/*
 * This class contains basics information, structures and function to connecting a database
 */
package ingsftw;

import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.pool.*;

public class DatabaseConfig {
    private Connection conn;
     
    /**
    * Server address
    */
    public static String host = "127.0.0.1";
    
    /**
    * Service name.
    */
    public static String servizio = "xe";
    
    /**
    * Connection port.
    */
    public static int porta = 1521;
    
    /**
    * User.
    */  
    public static String user = "ingegneria";
    public static String password = "password";
  

    /**
    * Scheme name (usually it's the same of user).
    */
    public static String schema = "ingegneria";
    
    /**
    * DataSource Object to connect a DB
    */
    private static OracleDataSource ods;
    
    /**
    * Variable used to save an active connection, if it exists.
    */
    private static Connection defaultConnection = null;

    /**
    * Return the default connection.
    *
    * @return Default connection, either one already used or a new one
    * obtained from the currently set parameters.
    * @throws SQLException In case of connection problems.
    */
   private static Connection nuovaConnessione() throws SQLException {
      ods = new OracleDataSource();
      ods.setDriverType("thin");
      ods.setServerName(host);
      ods.setPortNumber(porta);
      ods.setUser(user);
      ods.setPassword(password);
      ods.setDatabaseName(servizio);
      
      return ods.getConnection();
   }
   
    /**
     *
     * @return
     * @throws SQLException
     */
    public static Connection getDBConnection() throws SQLException{
       if(defaultConnection==null)
           return(nuovaConnessione());
       
       return(defaultConnection);
    }
    
    public static void closeConnection(){
        defaultConnection=null;
    }


}