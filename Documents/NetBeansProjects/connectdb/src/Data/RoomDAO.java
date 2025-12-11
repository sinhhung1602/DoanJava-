package Data;
import javaswingg.newpackage.Entity.MeetingRoom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class RoomDAO {
    // Lấy danh sách phòng
    public List<MeetingRoom> getAllRooms() {
        List<MeetingRoom> rooms = new ArrayList<>();
        String sql = "SELECT * From MeetingRoom";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(new MeetingRoom(rs.getString("RoomId"),
                        rs.getString("RoomName"),
                        rs.getInt("Capacity"),
                        rs.getString("Location"),
                        rs.getString("Status"),
                        rs.getDouble("PricePerHour")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }
    // Lấy phòng theo ID
    public MeetingRoom getRoomById(String id) {
        String sql = "SELECT * FROM MeetingRoom WHERE RoomId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MeetingRoom(rs.getString("RoomId"),rs.getString("RoomName"),rs.getInt("Capacity"),
                        rs.getString("Location"),
                        rs.getString("Status"),
                        rs.getDouble("PricePerHour")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // Thêm phòng
    public boolean addRoom(MeetingRoom r) {
        String sql = "INSERT INTO MeetingRoom VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getId());
            ps.setString(2, r.getName());
            ps.setInt(3, r.getCapacity());
            ps.setString(4, r.getLocation());
            ps.setString(5, r.getStatus());
            ps.setDouble(6, r.getPricePerHour());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // Update phòng
    public boolean updateRoom(MeetingRoom r) {
        String sql = "UPDATE MeetingRoom SET RoomName=?, Capacity=?, Location=?, Status=?, PricePerHour=? WHERE RoomId=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getName());
            ps.setInt(2, r.getCapacity());
            ps.setString(3, r.getLocation());
            ps.setString(4, r.getStatus());
            ps.setDouble(5, r.getPricePerHour());
            ps.setString(6, r.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // Xóa phòng
    public boolean deleteRoom(String id) {
        String sql = "DELETE FROM MeetingRoom WHERE RoomId=?";
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
