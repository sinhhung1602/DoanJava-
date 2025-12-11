package connectdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class connecrsql {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=MeetingRoomManagement;encrypt=false;";
        String user = "sa";
        String password = "160206";

        try {
            // 1. Nạp driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 2. Kết nối
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Kết nối thành công SQL Server!");

            // 3. Câu SQL
            String sql = "INSERT INTO Admin VALUES ('sinhhung', '16022006', N'Do Sinh Hung')";

            // 4. Thực thi INSERT
            Statement stmt = conn.createStatement();
            int check = stmt.executeUpdate(sql);

            System.out.println("Số dòng thay đổi: " + check);
            if (check > 0) {
                System.out.println("Thêm dữ liệu thành công");
            } else {
                System.out.println("Thêm dữ liệu thất bại");
            }

            // 5. Đóng kết nối
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
