/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgc.DB;

import java.sql.*;
import java.io.*;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;

public class DBConnect {

    public static String CONNECTION_URL = "";
    public static String CONNECTION_URL_MA = "";
    public static final String USERNAME = "cG9zdGdyZXM=";
    public static final String PASSWORD = "U3VwZXJBZG1pbjAwNw==";

    public String db_postgres_jdbc = "jdbc:postgresql://";
    public String db_postgres_server = "";
    public String db_postgres_username = "";
    public String db_postgres_password = "";

    //Connect Mysql For Weight Scale System
    public static final String MYSQL_CONNECTION_URL = "jdbc:mysql://bw-rv016.dyndns.org:3306/";
    public static final String MYSQL_DBNAME = "Z2VuaXVzX2RhdGE=";
    public static final String MYSQL_USERNAME = "Y2dj";
    public static final String MYSQL_PASSWORD = "c3lzdGVtYWRtaW4=";

    //Connect Mysql For Weight Scale System    
    public String db_mysql_server = "";
    public String db_mysql_weight_scale_database = "";
    public String db_mysql_username = "";
    public String db_mysql_password = "";

    public DBConnect() {
    }

    public void closeConnection(Connection conn) throws Exception {
        if (conn != null) {
            //System.out.println("#: PostgreSQL connection {"+conn+"} has been droped.");
            conn.close();
        }
    }

    public Connection openTestConnection() throws Exception {
        Connection conn;
        Class.forName("org.postgresql.Driver");
        String db_postgres_jdbc_test = "jdbc:postgresql://192.168.1.40:5432/CGCDB_PRODUCT_2558";
        //String db_postgres_jdbc_test = "jdbc:postgresql://localhost:5432/CGCDB_PRODUCT_2558";
        //System.out.println("db_postgres_jdbc_test = " + db_postgres_jdbc_test);

        byte[] user_decode = Base64.decodeBase64(USERNAME);
        byte[] pass_decode = Base64.decodeBase64(PASSWORD);
        conn = DriverManager.getConnection(db_postgres_jdbc_test, new String(user_decode), new String(pass_decode));
        if (conn == null) {
            throw new SQLException("Cannot initial database connection, because it's NULL.");
        }
        //System.out.println("#: PostgreSQL connection opened := "+conn);
        return conn;
    }

    public Connection openNewConnection() throws Exception {
        Connection conn;

        Class.forName("org.postgresql.Driver");

        ReadPostgreSQLConfig();

        String CONNECTION_DB = db_postgres_jdbc + db_postgres_server + CONNECTION_URL;
        byte[] user_decode = Base64.decodeBase64(db_postgres_username);
        byte[] pass_decode = Base64.decodeBase64(db_postgres_password);

        conn = DriverManager.getConnection(CONNECTION_DB, new String(user_decode), new String(pass_decode));
        if (conn == null) {
            throw new SQLException("Cannot initial database connection, because it's NULL.");
        }

        return conn;
    }

    public Connection openNewConnectionMA() throws Exception {
        Connection conn;

        Class.forName("org.postgresql.Driver");

        ReadPostgreSQLConfig();

        String CONNECTION_DB = db_postgres_jdbc + db_postgres_server + CONNECTION_URL_MA;

        System.out.println("CONNECTION_DB = " + CONNECTION_DB);
        byte[] user_decode = Base64.decodeBase64(db_postgres_username);
        byte[] pass_decode = Base64.decodeBase64(db_postgres_password);

        conn = DriverManager.getConnection(CONNECTION_DB, new String(user_decode), new String(pass_decode));
        if (conn == null) {
            throw new SQLException("Cannot initial database connection, because it's NULL.");
        }

        return conn;
    }

    public Connection openNewConnectionMA_old() throws Exception {
        Connection conn;
        Class.forName("org.postgresql.Driver");
        byte[] user_decode = Base64.decodeBase64(USERNAME);
        byte[] pass_decode = Base64.decodeBase64(PASSWORD);

        conn = DriverManager.getConnection(CONNECTION_URL_MA, new String(user_decode), new String(pass_decode));

        //System.out.println("CONNECTION_URL_MA = " + CONNECTION_URL_MA);
        //System.out.println("USERNAME = " + USERNAME);
        //System.out.println("PASSWORD = " + PASSWORD);
        if (conn == null) {
            throw new SQLException("Cannot initial database connection, because it's NULL.");
        }
        //System.out.println("#: PostgreSQL connection opened := "+conn);
        return conn;
    }

    public Connection openNewConnectionMySQL_Old() throws Exception {
        Connection conn;
        Class.forName("com.mysql.jdbc.Driver");
        byte[] dbname_decode = Base64.decodeBase64(MYSQL_DBNAME);
        byte[] user_decode = Base64.decodeBase64(MYSQL_USERNAME);
        byte[] pass_decode = Base64.decodeBase64(MYSQL_PASSWORD);

        String DB_Server = MYSQL_CONNECTION_URL + new String(dbname_decode);

        System.out.println(DB_Server);

        conn = DriverManager.getConnection(DB_Server, new String(user_decode), new String(pass_decode));

        if (conn == null) {
            throw new SQLException("Cannot initial database connection, because it's NULL.");
        }

        return conn;
    }

    public Connection openNewConnectionMySQL_Test() throws Exception {
        Connection conn;
        Class.forName("com.mysql.jdbc.Driver");
        byte[] dbname_decode = Base64.decodeBase64(MYSQL_DBNAME);
        byte[] user_decode = Base64.decodeBase64(MYSQL_USERNAME);
        byte[] pass_decode = Base64.decodeBase64(MYSQL_PASSWORD);

        String DB_Server = MYSQL_CONNECTION_URL + new String(dbname_decode);

        System.out.println(DB_Server);

        conn = DriverManager.getConnection(DB_Server, new String(user_decode), new String(pass_decode));

        if (conn == null) {
            throw new SQLException("Cannot initial database connection, because it's NULL.");
        }

        return conn;
    }

    public Connection openNewConnectionMySQL() throws Exception {
        Connection conn;
        Class.forName("com.mysql.jdbc.Driver");

        ReadMySQLConfig();

        byte[] dbname_decode = Base64.decodeBase64(db_mysql_weight_scale_database);
        String DB_Server = db_mysql_server + new String(dbname_decode);
        byte[] user_decode = Base64.decodeBase64(db_mysql_username);
        byte[] pass_decode = Base64.decodeBase64(db_mysql_password);

        conn = DriverManager.getConnection(DB_Server, new String(user_decode), new String(pass_decode));
        if (conn == null) {
            throw new SQLException("Cannot initial database connection, because it's NULL.");
        }
        //System.out.print("DATABASE Connect to : "+MYSQL_CONNECTION_URL);
        //System.out.println("#: mysql connection opened := "+conn);
        return conn;
    }

    public void ReadPostgreSQLConfig() throws Exception {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("config.properties");

            //System.out.println("INPUT = " + input) ;
            // load a properties file
            prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));

            //Get Properties Values
            db_postgres_server = prop.getProperty("db_postgres_server");
            db_postgres_username = prop.getProperty("db_postgres_username");
            db_postgres_password = prop.getProperty("db_postgres_password");

        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    public void ReadMySQLConfig() throws Exception {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("config.properties");

            //System.out.println("INPUT = " + input) ;
            // load a properties file
            prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));

            //Get Properties Values
            db_mysql_server = prop.getProperty("db_mysql_server");
            db_mysql_weight_scale_database = prop.getProperty("db_mysql_weight_scale_database");
            db_mysql_username = prop.getProperty("db_mysql_username");
            db_mysql_password = prop.getProperty("db_mysql_password");

        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

}
