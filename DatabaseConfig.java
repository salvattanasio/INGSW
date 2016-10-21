/*
 * This class contains basics information, structures and function to connecting a database
 */
package ingsftw;

import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.pool.*;

public class DatabaseConfig {
     public  Connection conn;
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
    
        /**CONNESSIONE AL db**/
    //
    /*
    public static String user = "ingSW";
    public static String password = "admin";
    //
    */
    /*CREDENZIALI CAMILLA*/
    ///*
    public static String user = "ingegneria";
    public static String password = "password";
    //*/

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
    public static Connection defaultConnection = null;

    /**
    * Return the default connection.
    *
    * @return Connessione di default (quella gi&agrave; attiva, o una nuova
    * ottenuta in base ai parametri di connessione attualmente impostati
    * @throws SQLException In caso di problemi di connessione
    */
   public static Connection nuovaConnessione() throws SQLException {
      ods = new OracleDataSource();
      ods.setDriverType("thin");
      ods.setServerName(host);
      ods.setPortNumber(porta);
      ods.setUser(user);
      ods.setPassword(password);
      ods.setDatabaseName(servizio);
      return ods.getConnection();
   }
}