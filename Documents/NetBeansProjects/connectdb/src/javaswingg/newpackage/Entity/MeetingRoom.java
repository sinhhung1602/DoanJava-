package javaswingg.newpackage.Entity;
import java.time.LocalDateTime;
import java.util.List;
public class MeetingRoom {
    private String roomId;
    private String roomName;
    private int capacity;
    private String location;
    private String status;
    private double pricePerHour;
    
    public MeetingRoom(String roomId, String roomName, int capacity, String location, String status, double pricePerHour) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.location = location;
        this.status = status;
        this.pricePerHour = pricePerHour;
    }

    public String getId() { 
        return roomId; 
    }
    public String getRoomId() { 
        return roomId; 
    }
    public String getName() { 
        return roomName; 
    }
    public String getRoomName() { 
        return roomName; 
    }
    public int getCapacity() { 
        return capacity; 
    }
    public String getLocation() { 
        return location; 
    }
    public String getStatus() { 
        return status; 
    }
    public double getPricePerHour() { 
        return pricePerHour; 
    }
    
    // Setters
    public void setRoomId(String roomId) { 
        this.roomId = roomId; 
    }
    public void setName(String name) { 
        this.roomName = name; 
    }
    public void setRoomName(String roomName) { 
        this.roomName = roomName; 
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity; 
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setPricePerHour(double pricePerHour) { 
        this.pricePerHour = pricePerHour; 
    }

    public void addRoom() {        
    }
    
    public void updateRoom() {        
    }
    
    public void deleteRoom() {      
    }
    
    public MeetingRoom searchRoom(String id) {       
        return null;
    }
    
    public boolean checkAvailability(LocalDateTime startTime, LocalDateTime endTime, List<BookingRequest> bookings) {
        if (!this.status.equals("Sẵn sàng")) {
            return false;
        }       
        if (bookings != null) {
            for (BookingRequest booking : bookings) {
                if (booking.getRoomId().equals(this.roomId) && booking.getStatus().equals("Đã duyệt")) {
                    if (!(endTime.isBefore(booking.getStartTime()) || startTime.isAfter(booking.getEndTime()))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
}