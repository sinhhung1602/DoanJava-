package Data;
import javaswingg.newpackage.Entity.BookingRequest;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
public class BookingDAO {
    public List<BookingRequest> getAllBookings() {
        List<BookingRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM BookingRequest";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDateTime start = rs.getTimestamp("startTime")
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("endTime")
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                list.add(new BookingRequest(
                        rs.getString("bookingId"),rs.getString("roomId"),rs.getString("customerId"),rs.getString("customerName"),rs.getString("department"),
                        start,end,rs.getString("purpose"),rs.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void addBooking(BookingRequest booking) {
        String sql = "INSERT INTO BookingRequest(bookingId,roomId,customerId,customerName,department,startTime,endTime,purpose, status) VALUES (?, ?, ?,?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, booking.getId());
            ps.setString(2, booking.getRoomId());
            ps.setString(3, booking.getCustomerId());//
            ps.setString(4, booking.getRequesterName());
            ps.setString(5, booking.getDepartment());
            ps.setTimestamp(6, Timestamp.valueOf(booking.getStartTime()));
            ps.setTimestamp(7, Timestamp.valueOf(booking.getEndTime()));
            ps.setString(8, booking.getPurpose());
            ps.setString(9, booking.getStatus());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateStatus(String bookingId, String status) {
        String sql = "UPDATE BookingRequest SET status = ? WHERE bookingId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, bookingId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteBooking(String bookingId) {
    String sql = "DELETE FROM BookingRequest WHERE bookingId = ?";   
    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
               ps.setString(1, bookingId);
        int rowsAffected = ps.executeUpdate();        
        System.out.println("🗑️ Đã xóa " + rowsAffected + " booking(s)");       
    } catch (Exception e) {
        System.err.println("❌ Lỗi không thể xóa " + e.getMessage());
        e.printStackTrace();
    }
}
    // Thêm phương thức đếm yêu cầu chờ duyệt
    public int getPendingCount() {
        String sql = "SELECT COUNT(*) FROM BookingRequest WHERE status = N'Chờ duyệt'";       
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {          
            if (rs.next()) {
                return rs.getInt(1);
            }           
        } catch (Exception e) {
            e.printStackTrace();
        }      
        return 0;
    }
    // Thêm phương thức đếm yêu cầu đã duyệt
    public int getApprovedCount() {
        String sql = "SELECT COUNT(*) FROM BookingRequest WHERE status = N'Đã duyệt'";       
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {           
            if (rs.next()) {
                return rs.getInt(1);
            }          
        } catch (Exception e) {
            e.printStackTrace();
        }        
        return 0;
    }
}