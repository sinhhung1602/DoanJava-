package javaswingg.newpackage.Entity;
import java.time.LocalDateTime;
public class BookingRequest {
    private String bookingId;
    private String roomId;
    private String customerId;
    private String customerName;
    private String department;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private String status;
    private LocalDateTime requestDate;
    private double totalAmount;
    
    public BookingRequest(String bookingId, String roomId, String customerId, String customerName, String department,
                         LocalDateTime startTime, LocalDateTime endTime, String purpose, String status) {//
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.customerName = customerName;
        this.customerId = customerId;
        this.department = department;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
        this.requestDate = LocalDateTime.now();
        this.totalAmount = 0;
    }
    public String getId() { 
        return bookingId; 
    }
    public String getBookingId() { 
        return bookingId; }
    public String getRoomId() { 
        return roomId; 
    }
    public String getCustomerId() { 
        return customerId; 
    }
    public String getRequesterName() { 
        return customerName; 
    }
    public String getDepartment() { 
        return department; 
    }
    public LocalDateTime getStartTime() { 
        return startTime; 
    }
    public LocalDateTime getEndTime() { 
        return endTime; 
    }
    public String getPurpose() { 
        return purpose; 
    }
    public String getStatus() { 
        return status; 
    }
    public LocalDateTime getRequestDate() { 
        return requestDate; 
    }
    public double getTotalAmount() { 
        return totalAmount; 
    }

    public void setBookingId(String bookingId) { 
        this.bookingId = bookingId; 
    }
    public void setRoomId(String roomId) { 
        this.roomId = roomId; 
    }
    public void setCustomerId(String customerId) { 
        this.customerId = customerId; 
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime; 
    }
    public void setEndTime(LocalDateTime endTime) { 
        this.endTime = endTime; 
    }
    public void setPurpose(String purpose) { 
        this.purpose = purpose; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
    public void setTotalAmount(double totalAmount) { 
        this.totalAmount = totalAmount; 
    }   
    public long getDurationHours() {
        return java.time.Duration.between(startTime, endTime).toHours();
    }
  
    public void createBooking() {        
    }
    
    public void updateBooking() {
    }
    
    public void cancelBooking() {
        this.status = "Cancelled";
    }
    
    public BookingRequest searchBooking(String id) {
        return null;
    }   
    public double calculateTotalAmount(double pricePerHour) {
        this.totalAmount = getDurationHours() * pricePerHour;
        return this.totalAmount;
    }
}