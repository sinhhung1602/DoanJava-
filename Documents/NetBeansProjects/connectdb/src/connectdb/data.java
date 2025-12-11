/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectdb;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author Admin
 */
public class data {
  public static Connection getConnection() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyPhongHop3;encrypt=false;";
            String user = "sa";            // 🔧 Thay bằng tài khoản SQL của bạn
            String password = "160206";    // 🔧 Thay bằng mật khẩu SQL của bạn

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("❌ Lỗi kết nối SQL Server: " + e.getMessage());
            return null;
        }
    }
}
