package Data;
import javaswingg.newpackage.Entity.Facility;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class FacilityDAO {
    // Lấy toàn bộ CSVC
    public List<Facility> getAllFacilities() {
        List<Facility> list = new ArrayList<>();
        String sql = "SELECT * FROM Facility";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Facility(rs.getString("facilityId"),
                        rs.getString("facilityName"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("condition")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // Lấy theo ID
    public Facility getFacilityById(String id) {
        String sql = "SELECT * FROM Facility WHERE facilityId = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Facility(
                        rs.getString("facilityId"),
                        rs.getString("facilityName"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("condition")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    // Thêm CSVC
    public boolean addFacility(Facility f) {
        String sql = "INSERT INTO Facility (facilityId, facilityName, category, quantity, condition) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getId());
            ps.setString(2, f.getName());
            ps.setString(3, f.getCategory());
            ps.setInt(4, f.getQuantity());
            ps.setString(5, f.getCondition());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // Cập nhật CSVC
    public boolean updateFacility(Facility f) {
        String sql = "UPDATE Facility SET facilityName=?, category=?, quantity=?, condition=? WHERE facilityId=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getName());
            ps.setString(2, f.getCategory());
            ps.setInt(3, f.getQuantity());
            ps.setString(4, f.getCondition());
            ps.setString(5, f.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa CSVC
    public boolean deleteFacility(String id) {
        String sql = "DELETE FROM Facility WHERE facilityId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
