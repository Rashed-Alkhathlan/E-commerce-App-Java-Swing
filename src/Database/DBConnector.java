package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

@SuppressWarnings("CallToPrintStackTrace")
public class DBConnector {
    private Connection connection;

    private static final String URL = "jdbc:mysql://192.168.3.119:3306/OnlineStore";
    private static final String USER = "root";
    private static final String PASSWORD = "ShopAppDB";

    DBConnector() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
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
