package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

@SuppressWarnings("CallToPrintStackTrace")
public class DBConnector {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection connection;
    private static final String URL = "jdbc:mysql://144.86.44.217:3306/ShopAppDatabase";
    //private static final String URL = "jdbc:mysql://192.168.3.119:3306/ShopAppDatabase";
    //private static final String URL = "jdbc:mysql://localhost:3306/ShopAppDatabase";
    private static final String USER = "root";

    private static final String PASSWORD = "ShopAppDB";

    DBConnector() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static final class InstanceHolder {
        private static final DBConnector instance = new DBConnector();
    }

    public static DBConnector getInstance() {
        return InstanceHolder.instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
