/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.*;
public class DatabaseHelper {

    public static Connection getConnection() throws SQLException {
        String server = "Admin-PC\\SQLEXPRESS";
        String user = "sa";
        String password = "160206";
        String db = "MeetingRoomManagement";
        int port = 1433;
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser(user);
        ds.setPassword(password);
        ds.setDatabaseName(db);
        ds.setServerName(server);
        ds.setPortNumber(port);
        ds.setTrustServerCertificate(true);
        return ds.getConnection();
    }
    // Dùng để test kết nối
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Kết nối thành công!");
            System.out.println("Database: " + conn.getCatalog());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
