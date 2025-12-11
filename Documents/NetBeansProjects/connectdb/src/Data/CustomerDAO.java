/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javaswingg.newpackage.Entity.Customer;
public class CustomerDAO {
    // Lấy toàn bộ danh sách khách hàng
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT customerId, customerName, phoneNumber, identityCard FROM Customer";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Customer(rs.getString("customerId"),rs.getString("customerName"),rs.getString("phoneNumber"),rs.getString("identityCard")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm khách hàng mới
    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO Customer (customerId, customerName, phoneNumber, identityCard) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerId());
            ps.setString(2, c.getCustomerName());
            ps.setString(3, c.getPhoneNumber());
            ps.setString(4, c.getIdentityCard());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // Cập nhật thông tin khách hàng
    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE Customer SET customerName = ?, phoneNumber = ?, identityCard = ? WHERE customerId = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getPhoneNumber());
            ps.setString(3, c.getIdentityCard());
            ps.setString(4, c.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
   // Xóa khách hàng theo ID
    public boolean deleteCustomer(String id) {
        String sql = "DELETE FROM Customer WHERE customerId = ?";
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
