package Data;
import javaswingg.newpackage.Entity.Admin;
import java.sql.*;
public class DAOadmin {   
    public boolean checkLogin(String username, String password) {
        String sql = "SELECT username FROM Admin WHERE username = ? AND password = ?";        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);         
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi kiểm tra đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public Admin login(String username, String password) {
        String sql = "SELECT username, password, fullname FROM Admin WHERE username = ? AND password = ?";        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {           
            stmt.setString(1, username);
            stmt.setString(2, password); 
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getString("username"),rs.getString("password"),rs.getString("fullname"));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }   
    // Kiểm tra username tồn tại
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Admin WHERE username = ?";        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {           
            stmt.setString(1, username);           
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi kiểm tra username: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public boolean register(Admin admin) {
        if (isUsernameExists(admin.getUsername())) {
            System.err.println("❌ Username đã tồn tại!");
            return false;
        }        
        String sql = "INSERT INTO Admin (username, password, fullname) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {           
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getFullName());            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Đăng ký thành công: " + admin.getUsername());
                return true;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đăng ký: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public boolean registerUser(String username, String password, String fullName) {
        if (isUsernameExists(username)) {
            System.err.println("❌ Username đã tồn tại: " + username);
            return false;
        }       
        String sql = "INSERT INTO Admin (username, password, fullname) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {           
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Đăng ký thành công: " + username);
                return true;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đăng ký: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}