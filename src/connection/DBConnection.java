package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/user_db";
    private static final String USER = "root"; // or your DB user
    private static final String PASSWORD = ""; // your MySQL password

    // public static Connection getConnection() throws SQLException {
    //     return DriverManager.getConnection(URL, USER, PASSWORD);
    // }


    public static Connection connectDB() {
       try
       {
         Connection connect =DriverManager.getConnection(URL, USER, PASSWORD);
         return connect;
       
       }catch(Exception e){e.printStackTrace();}
       return null;
    }
    public static Connection connectyourdb(String url) {
      try
      {
        Connection connect =DriverManager.getConnection(url, USER, PASSWORD);
        return connect;
      
      }catch(Exception e){e.printStackTrace();}
      return null;
   }
}