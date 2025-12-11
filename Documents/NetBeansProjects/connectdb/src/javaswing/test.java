/*
 * Enhanced Meeting Room Booking System
 * Updated to align with class diagram specifications
 */
package javaswing;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.time.*;
import java.time.format.*;

public class test extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

// ============= ENTITY CLASSES =============

class Customer {
    private String customerId;
    private String customerName;
    private String phoneNumber;
    private String email;
    
    public Customer(String customerId, String customerName, String phoneNumber, String email) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    
    // Setters
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    
    // Methods from diagram
    public void addCustomer() {
        DataManager.getInstance().addCustomer(this);
    }
    
    public void updateCustomer() {
        // Update logic handled by DataManager
    }
    
    public void deleteCustomer() {
        DataManager.getInstance().deleteCustomer(this.customerId);
    }
    
    public Customer searchCustomer(String id) {
        return DataManager.getInstance().getCustomerById(id);
    }
}

class BookingRequest {
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
    
    public BookingRequest(String bookingId, String roomId, String customerName, String department,
                         LocalDateTime startTime, LocalDateTime endTime, String purpose, String status) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.customerName = customerName;
        this.department = department;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
        this.requestDate = LocalDateTime.now();
        this.totalAmount = 0;
    }
    
    // Getters
    public String getId() { return bookingId; }
    public String getBookingId() { return bookingId; }
    public String getRoomId() { return roomId; }
    public String getCustomerId() { return customerId; }
    public String getRequesterName() { return customerName; }
    public String getDepartment() { return department; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getPurpose() { return purpose; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public double getTotalAmount() { return totalAmount; }
    
    // Setters
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public long getDurationHours() {
        return java.time.Duration.between(startTime, endTime).toHours();
    }
    
    // Methods from diagram
    public void createBooking() {
        DataManager.getInstance().addBooking(this);
    }
    
    public void updateBooking() {
        // Update logic
    }
    
    public void cancelBooking() {
        this.status = "Cancelled";
    }
    
    public BookingRequest searchBooking(String id) {
        return DataManager.getInstance().getBookingById(id);
    }
    
    public double calculateTotalAmount() {
        MeetingRoom room = DataManager.getInstance().getRoomById(this.roomId);
        if (room != null) {
            this.totalAmount = getDurationHours() * room.getPricePerHour();
        }
        return this.totalAmount;
    }
}

class MeetingRoom {
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
    
    // Getters
    public String getId() { return roomId; }
    public String getRoomId() { return roomId; }
    public String getName() { return roomName; }
    public String getRoomName() { return roomName; }
    public int getCapacity() { return capacity; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public double getPricePerHour() { return pricePerHour; }
    
    // Setters
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public void setName(String name) { this.roomName = name; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setLocation(String location) { this.location = location; }
    public void setStatus(String status) { this.status = status; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }
    
    // Methods from diagram
    public void addRoom() {
        DataManager.getInstance().addRoom(this);
    }
    
    public void updateRoom() {
        // Update logic
    }
    
    public void deleteRoom() {
        DataManager.getInstance().deleteRoom(this.roomId);
    }
    
    public MeetingRoom searchRoom(String id) {
        return DataManager.getInstance().getRoomById(id);
    }
    
    public boolean checkAvailability(LocalDateTime startTime, LocalDateTime endTime) {
        if (!this.status.equals("Sẵn sàng")) {
            return false;
        }
        
        for (BookingRequest booking : DataManager.getInstance().getBookings()) {
            if (booking.getRoomId().equals(this.roomId) && 
                booking.getStatus().equals("Đã duyệt")) {
                if (!(endTime.isBefore(booking.getStartTime()) || startTime.isAfter(booking.getEndTime()))) {
                    return false;
                }
            }
        }
        return true;
    }
}

class Facility {
    private String facilityId;
    private String facilityName;
    private String category;
    private int quantity;
    private String condition;
    
    public Facility(String facilityId, String facilityName, String category, int quantity, String condition) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.category = category;
        this.quantity = quantity;
        this.condition = condition;
    }
    
    // Getters
    public String getId() { return facilityId; }
    public String getFacilityId() { return facilityId; }
    public String getName() { return facilityName; }
    public String getFacilityName() { return facilityName; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public String getCondition() { return condition; }
    
    // Setters
    public void setFacilityId(String facilityId) { this.facilityId = facilityId; }
    public void setName(String name) { this.facilityName = name; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    public void setCategory(String category) { this.category = category; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCondition(String condition) { this.condition = condition; }
    
    // Methods from diagram
    public void addFacility() {
        DataManager.getInstance().addFacility(this);
    }
    
    public void updateFacility() {
        // Update logic
    }
    
    public void deleteFacility() {
        DataManager.getInstance().deleteFacility(this.facilityId);
    }
    
    public Facility searchFacility(String id) {
        return DataManager.getInstance().getFacilityById(id);
    }
}

class Employee {
    private String employeeId;
    private String employeeName;
    private String position;
    private String department;
    private String phoneNumber;
    
    public Employee(String employeeId, String employeeName, String position, String department, String phoneNumber) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.department = department;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }
    public String getDepartment() { return department; }
    public String getPhoneNumber() { return phoneNumber; }
    
    // Setters
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public void setPosition(String position) { this.position = position; }
    public void setDepartment(String department) { this.department = department; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    // Methods from diagram
    public void addEmployee() {
        DataManager.getInstance().addEmployee(this);
    }
    
    public void updateEmployee() {
        // Update logic
    }
    
    public void deleteEmployee() {
        DataManager.getInstance().deleteEmployee(this.employeeId);
    }
    
    public Employee searchEmployee(String id) {
        return DataManager.getInstance().getEmployeeById(id);
    }
}

class Admin {
    private String username;
    private String password;
    private String fullName;
    
    public Admin(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }
    
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
}

// ============= DATA MANAGER (Singleton) =============

class DataManager {
    private static DataManager instance;
    private List<Admin> admins;
    private List<MeetingRoom> rooms;
    private List<BookingRequest> bookings;
    private List<Facility> facilities;
    private List<Customer> customers;
    private List<Employee> employees;
    private Admin currentAdmin;
    
    private DataManager() {
        admins = new ArrayList<>();
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        facilities = new ArrayList<>();
        customers = new ArrayList<>();
        employees = new ArrayList<>();
        initializeData();
    }   
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    private void initializeData() {
        admins.add(new Admin("admin", "admin123", "Quản trị viên"));
        
        rooms.add(new MeetingRoom("R001", "Phòng họp A1", 10, "Tầng 1", "Sẵn sàng", 100000));
        rooms.add(new MeetingRoom("R002", "Phòng họp A2", 20, "Tầng 1", "Sẵn sàng", 150000));
        rooms.add(new MeetingRoom("R003", "Phòng họp B1", 15, "Tầng 2", "Sẵn sàng", 120000));
        rooms.add(new MeetingRoom("R004", "Phòng họp C1", 30, "Tầng 3", "Bảo trì", 200000));
        
        facilities.add(new Facility("F001", "Máy chiếu", "Thiết bị điện tử", 10, "Tốt"));
        facilities.add(new Facility("F002", "Micro không dây", "Thiết bị âm thanh", 15, "Tốt"));
        facilities.add(new Facility("F003", "Bảng Flipchart", "Văn phòng phẩm", 8, "Khá"));
        facilities.add(new Facility("F004", "Màn hình LED", "Thiết bị điện tử", 5, "Tốt"));
        facilities.add(new Facility("F005", "Bàn ghế", "Nội thất", 50, "Tốt"));
        
        customers.add(new Customer("C001", "Nguyễn Văn A", "0901234567", "nguyenvana@email.com"));
        customers.add(new Customer("C002", "Trần Thị B", "0912345678", "tranthib@email.com"));
        
        employees.add(new Employee("E001", "Phạm Văn C", "Quản lý", "Hành chính", "0923456789"));
        employees.add(new Employee("E002", "Lê Thị D", "Nhân viên", "Kỹ thuật", "0934567890"));
    }
    
    public Admin authenticate(String username, String password) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                currentAdmin = admin;
                return admin;
            }
        }
        return null;
    }
    
    public boolean registerAdmin(String username, String password, String fullName) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username)) {
                return false;
            }
        }
        admins.add(new Admin(username, password, fullName));
        return true;
    }
    
    // Admin getters
    public Admin getCurrentAdmin() { return currentAdmin; }
    
    // Room methods
    public List<MeetingRoom> getRooms() { return rooms; }
    public void addRoom(MeetingRoom room) { rooms.add(room); }
    public void deleteRoom(String id) { rooms.removeIf(r -> r.getId().equals(id)); }
    public MeetingRoom getRoomById(String id) {
        for (MeetingRoom room : rooms) {
            if (room.getId().equals(id)) return room;
        }
        return null;
    }
    
    // Booking methods
    public List<BookingRequest> getBookings() { return bookings; }
    public void addBooking(BookingRequest booking) { bookings.add(booking); }
    public BookingRequest getBookingById(String id) {
        for (BookingRequest booking : bookings) {
            if (booking.getId().equals(id)) return booking;
        }
        return null;
    }
    
    // Facility methods
    public List<Facility> getFacilities() { return facilities; }
    public void addFacility(Facility facility) { facilities.add(facility); }
    public void deleteFacility(String id) { facilities.removeIf(f -> f.getId().equals(id)); }
    public Facility getFacilityById(String id) {
        for (Facility facility : facilities) {
            if (facility.getId().equals(id)) return facility;
        }
        return null;
    }
    
    // Customer methods
    public List<Customer> getCustomers() { return customers; }
    public void addCustomer(Customer customer) { customers.add(customer); }
    public void deleteCustomer(String id) { customers.removeIf(c -> c.getCustomerId().equals(id)); }
    public Customer getCustomerById(String id) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(id)) return customer;
        }
        return null;
    }
    
    // Employee methods
    public List<Employee> getEmployees() { return employees; }
    public void addEmployee(Employee employee) { employees.add(employee); }
    public void deleteEmployee(String id) { employees.removeIf(e -> e.getEmployeeId().equals(id)); }
    public Employee getEmployeeById(String id) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId().equals(id)) return employee;
        }
        return null;
    }
    
    // Statistics methods
    public int getPendingBookingsCount() {
        int count = 0;
        for (BookingRequest booking : bookings) {
            if (booking.getStatus().equals("Chờ duyệt")) count++;
        }
        return count;
    }
    
    public int getApprovedBookingsCount() {
        int count = 0;
        for (BookingRequest booking : bookings) {
            if (booking.getStatus().equals("Đã duyệt")) count++;
        }
        return count;
    }
}

// ============= UI CLASSES =============

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        setTitle("Hệ thống Booking Phòng Họp");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
      
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(0xF5F5F5));
        
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(new Color(0x1976D2));
        headerPanel.setPreferredSize(new Dimension(0,100));
        JLabel titleLabel = new JLabel("HỆ THỐNG BOOKING PHÒNG HỌP");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel loginLabel = new JLabel("Đăng nhập");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginLabel.setForeground(new Color(0x333333));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(loginLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCCCCCC)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel passLabel = new JLabel("Mật khẩu:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCCCCCC)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(passwordField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginButton = createStyledButton("Đăng nhập", new Color(0x1976D2));
        JButton registerButton = createStyledButton("Đăng ký", new Color(0x43A047));
        
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> showRegisterDialog());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);
        
JPanel centerWrapper = new JPanel(new GridBagLayout());
    centerWrapper.setBackground(Color.WHITE);
    centerWrapper.add(formPanel);
        
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Admin admin = DataManager.getInstance().authenticate(username, password);
        if (admin != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Đăng ký tài khoản", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Đăng ký tài khoản mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        JTextField regUsername = new JTextField(20);
        JPasswordField regPassword = new JPasswordField(20);
        JTextField regFullName = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        panel.add(regUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        panel.add(regPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        panel.add(regFullName, gbc);
        
        JButton registerBtn = createStyledButton("Đăng ký", new Color(0x43A047));
        registerBtn.addActionListener(e -> {
            String username = regUsername.getText();
            String password = new String(regPassword.getPassword());
            String fullName = regFullName.getText();
            
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            if (DataManager.getInstance().registerAdmin(username, password, fullName)) {
                JOptionPane.showMessageDialog(dialog, "Đăng ký thành công!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Tên đăng nhập đã tồn tại!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(registerBtn, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}

class MainFrame extends JFrame {
    private JPanel contentPanel;
    
    public MainFrame() {
        setTitle("Hệ thống Booking Phòng Họp");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0x1976D2));
        menuBar.setBorderPainted(false);
        
        JMenu homeMenu = createStyledMenu("🏠 Trang chủ");
        JMenuItem homeItem = new JMenuItem("Về trang chủ");
        styleMenuItem(homeItem);
        homeItem.addActionListener(e -> showDashboard());
        homeMenu.add(homeItem);
        
        JMenu roomMenu = createStyledMenu("🏢 Quản lý phòng họp");
        JMenuItem viewRooms = new JMenuItem("Xem danh sách phòng");
        JMenuItem addRoom = new JMenuItem("Thêm phòng họp mới");
        styleMenuItem(viewRooms);
        styleMenuItem(addRoom);
        viewRooms.addActionListener(e -> showRoomManagement());
        addRoom.addActionListener(e -> showAddRoomDialog());
        roomMenu.add(viewRooms);
        roomMenu.add(addRoom);
        
        JMenu bookingMenu = createStyledMenu("📅 Quản lý yêu cầu thuê");
        JMenuItem newBooking = new JMenuItem("Tạo yêu cầu mới");
        JMenuItem viewBookings = new JMenuItem("Xem yêu cầu thuê");
        styleMenuItem(newBooking);
        styleMenuItem(viewBookings);
        newBooking.addActionListener(e -> showNewBookingDialog());
        viewBookings.addActionListener(e -> showBookingManagement());
        bookingMenu.add(newBooking);
        bookingMenu.add(viewBookings);
        
        JMenu facilityMenu = createStyledMenu("🔧 Quản lý cơ sở vật chất");
        JMenuItem viewFacilities = new JMenuItem("Xem cơ sở vật chất");
        JMenuItem addFacility = new JMenuItem("Thêm cơ sở vật chất");
        styleMenuItem(viewFacilities);
        styleMenuItem(addFacility);
        viewFacilities.addActionListener(e -> showFacilityManagement());
        addFacility.addActionListener(e -> showAddFacilityDialog());
        facilityMenu.add(viewFacilities);
        facilityMenu.add(addFacility);
        
        JMenu reportMenu = createStyledMenu("📊 Thống kê báo cáo");
        JMenuItem viewReport = new JMenuItem("Xem báo cáo");
        styleMenuItem(viewReport);
        viewReport.addActionListener(e -> showReportPanel());
        reportMenu.add(viewReport);
        
      JMenu systemMenu = createStyledMenu("️ Hệ thống");
        JMenuItem logout = new JMenuItem("Đăng xuất");
        JMenuItem exit = new JMenuItem("Thoát");
        styleMenuItem(logout);
        styleMenuItem(exit);
        logout.addActionListener(e -> logout());
        exit.addActionListener(e -> System.exit(0));
        systemMenu.add(logout);
        systemMenu.add(exit);
        
        menuBar.add(homeMenu);
        menuBar.add(roomMenu);
        menuBar.add(bookingMenu);
        menuBar.add(facilityMenu);
        menuBar.add(reportMenu);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(0xF5F5F5));
        showDashboard();
        add(contentPanel);
    }
    
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return menu;
    }
    
    private void styleMenuItem(JMenuItem item) {
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
    }
    
    private void showDashboard() {
        contentPanel.removeAll();
        
        JPanel dash = new JPanel(new BorderLayout(0, 20));
        dash.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        dash.setBackground(new Color(0xF5F5F5));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0xF5F5F5));
        
        JLabel welcome = new JLabel("Chào mừng, " + DataManager.getInstance().getCurrentAdmin().getFullName() + "!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcome.setForeground(new Color(0x1976D2));
        headerPanel.add(welcome, BorderLayout.WEST);
        
        JLabel dateLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(0x666666));
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        dash.add(headerPanel, BorderLayout.NORTH);
        
        JPanel grid = new JPanel(new GridLayout(2, 2, 25, 25));
        grid.setBackground(new Color(0xF5F5F5));
        
        grid.add(createModernCard("Tổng phòng họp", 
            String.valueOf(DataManager.getInstance().getRooms().size()), 
            new Color(0x4CAF50), "🏢"));
        grid.add(createModernCard("Yêu cầu chờ duyệt", 
            String.valueOf(DataManager.getInstance().getPendingBookingsCount()), 
            new Color(0xFF9800), "⏳"));
        grid.add(createModernCard("Cơ sở vật chất", 
            String.valueOf(DataManager.getInstance().getFacilities().size()), 
            new Color(0x2196F3), "🔧"));
        grid.add(createModernCard("Yêu cầu đã duyệt", 
            String.valueOf(DataManager.getInstance().getApprovedBookingsCount()), 
            new Color(0x9C27B0), "✅"));
        
        dash.add(grid, BorderLayout.CENTER);
        contentPanel.add(dash);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createModernCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE0E0E0), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0x666666));
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void showRoomManagement() {
        contentPanel.removeAll();
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0xF5F5F5));
        
        JLabel titleLabel = new JLabel("Quản lý phòng họp");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x1976D2));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Mã phòng", "Tên phòng", "Sức chứa", "Vị trí", "Trạng thái", "Giá/giờ (VNĐ)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (MeetingRoom room : DataManager.getInstance().getRooms()) {
            model.addRow(new Object[]{
                room.getId(),
                room.getName(),
                room.getCapacity(),
                room.getLocation(),
                room.getStatus(),
                String.format("%,.0f", room.getPricePerHour())
            });
        }
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0x1976D2));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(0xF5F5F5));
        
        JButton addButton = createStyledButton("Thêm phòng mới", new Color(0x4CAF50));
        JButton editButton = createStyledButton("Chỉnh sửa", new Color(0xFF9800));
        JButton deleteButton = createStyledButton("Xóa", new Color(0xF44336));
        
        addButton.addActionListener(e -> showAddRoomDialog());
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String roomId = (String) model.getValueAt(selectedRow, 0);
                showEditRoomDialog(DataManager.getInstance().getRoomById(roomId));
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần chỉnh sửa!");
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa phòng này?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String roomId = (String) model.getValueAt(selectedRow, 0);
                    DataManager.getInstance().getRooms().removeIf(r -> r.getId().equals(roomId));
                    model.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần xóa!");
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAddRoomDialog() {
        JDialog dialog = new JDialog(this, "Thêm phòng họp mới", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        JTextField locationField = new JTextField(20);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Sẵn sàng", "Bảo trì", "Đang sử dụng"});
        JTextField priceField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mã phòng:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tên phòng:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Sức chứa:"), gbc);
        gbc.gridx = 1;
        panel.add(capacitySpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Vị trí:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Giá/giờ (VNĐ):"), gbc);
        gbc.gridx = 1;
        panel.add(priceField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createStyledButton("Lưu", new Color(0x4CAF50));
        JButton cancelButton = createStyledButton("Hủy", new Color(0x757575));
        
        saveButton.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                int capacity = (Integer) capacitySpinner.getValue();
                String location = locationField.getText().trim();
                String status = (String) statusCombo.getSelectedItem();
                double price = Double.parseDouble(priceField.getText().trim());
                
                if (id.isEmpty() || name.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                    return;
                }
                
                MeetingRoom room = new MeetingRoom(id, name, capacity, location, status, price);
                DataManager.getInstance().addRoom(room);
                JOptionPane.showMessageDialog(dialog, "Thêm phòng thành công!");
                dialog.dispose();
                showRoomManagement();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá phòng phải là số!");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showEditRoomDialog(MeetingRoom room) {
        if (room == null) return;
        
        JDialog dialog = new JDialog(this, "Chỉnh sửa phòng họp", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(room.getName(), 20);
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(room.getCapacity(), 1, 100, 1));
        JTextField locationField = new JTextField(room.getLocation(), 20);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Sẵn sàng", "Bảo trì", "Đang sử dụng"});
        statusCombo.setSelectedItem(room.getStatus());
        JTextField priceField = new JTextField(String.valueOf(room.getPricePerHour()), 20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tên phòng:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Sức chứa:"), gbc);
        gbc.gridx = 1;
        panel.add(capacitySpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Vị trí:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Giá/giờ (VNĐ):"), gbc);
        gbc.gridx = 1;
        panel.add(priceField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createStyledButton("Cập nhật", new Color(0xFF9800));
        JButton cancelButton = createStyledButton("Hủy", new Color(0x757575));
        
        saveButton.addActionListener(e -> {
            try {
                room.setName(nameField.getText().trim());
                room.setCapacity((Integer) capacitySpinner.getValue());
                room.setLocation(locationField.getText().trim());
                room.setStatus((String) statusCombo.getSelectedItem());
                room.setPricePerHour(Double.parseDouble(priceField.getText().trim()));
                
                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                dialog.dispose();
                showRoomManagement();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Giá phòng phải là số!");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showBookingManagement() {
        contentPanel.removeAll();
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0xF5F5F5));
        
        JLabel titleLabel = new JLabel("Quản lý yêu cầu thuê phòng");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x1976D2));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Mã YC", "Phòng", "Người yêu cầu", "Phòng ban", "Thời gian bắt đầu", 
                          "Thời gian kết thúc", "Mục đích", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (BookingRequest booking : DataManager.getInstance().getBookings()) {
            MeetingRoom room = DataManager.getInstance().getRoomById(booking.getRoomId());
            model.addRow(new Object[]{
                booking.getId(),
                room != null ? room.getName() : booking.getRoomId(),
                booking.getRequesterName(),
                booking.getDepartment(),
                booking.getStartTime().format(formatter),
                booking.getEndTime().format(formatter),
                booking.getPurpose(),
                booking.getStatus()
            });
        }
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(0x1976D2));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(0xF5F5F5));
        
        JButton addButton = createStyledButton("Tạo yêu cầu mới", new Color(0x4CAF50));
        JButton approveButton = createStyledButton("Duyệt", new Color(0x2196F3));
        JButton rejectButton = createStyledButton("Từ chối", new Color(0xF44336));
        
        addButton.addActionListener(e -> showNewBookingDialog());
        
        approveButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String bookingId = (String) model.getValueAt(selectedRow, 0);
                for (BookingRequest booking : DataManager.getInstance().getBookings()) {
                    if (booking.getId().equals(bookingId)) {
                        booking.setStatus("Đã duyệt");
                        model.setValueAt("Đã duyệt", selectedRow, 7);
                        JOptionPane.showMessageDialog(this, "Đã duyệt yêu cầu!");
                        showDashboard();
                        showBookingManagement();
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn yêu cầu cần duyệt!");
            }
        });
        
        rejectButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String bookingId = (String) model.getValueAt(selectedRow, 0);
                for (BookingRequest booking : DataManager.getInstance().getBookings()) {
                    if (booking.getId().equals(bookingId)) {
                        booking.setStatus("Từ chối");
                        model.setValueAt("Từ chối", selectedRow, 7);
                        JOptionPane.showMessageDialog(this, "Đã từ chối yêu cầu!");
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn yêu cầu cần từ chối!");
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showNewBookingDialog() {
        JDialog dialog = new JDialog(this, "Tạo yêu cầu thuê phòng mới", true);
        dialog.setSize(550, 550);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField idField = new JTextField("BK" + (DataManager.getInstance().getBookings().size() + 1), 20);
        
        JComboBox<String> roomCombo = new JComboBox<>();
        for (MeetingRoom room : DataManager.getInstance().getRooms()) {
            if (room.getStatus().equals("Sẵn sàng")) {
                roomCombo.addItem(room.getId() + " - " + room.getName());
            }
        }
        
        JTextField nameField = new JTextField(20);
        JTextField deptField = new JTextField(20);
        
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy HH:mm");
        startDateSpinner.setEditor(startEditor);
        
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "dd/MM/yyyy HH:mm");
        endDateSpinner.setEditor(endEditor);
        
        JTextArea purposeArea = new JTextArea(3, 20);
        purposeArea.setLineWrap(true);
        JScrollPane purposeScroll = new JScrollPane(purposeArea);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mã yêu cầu:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Phòng họp:"), gbc);
        gbc.gridx = 1;
        panel.add(roomCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Người yêu cầu:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Phòng ban:"), gbc);
        gbc.gridx = 1;
        panel.add(deptField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Thời gian bắt đầu:"), gbc);
        gbc.gridx = 1;
        panel.add(startDateSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Thời gian kết thúc:"), gbc);
        gbc.gridx = 1;
        panel.add(endDateSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Mục đích:"), gbc);
        gbc.gridx = 1;
        panel.add(purposeScroll, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createStyledButton("Tạo yêu cầu", new Color(0x4CAF50));
        JButton cancelButton = createStyledButton("Hủy", new Color(0x757575));
        
        saveButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String roomSelection = (String) roomCombo.getSelectedItem();
            if (roomSelection == null) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn phòng họp!");
                return;
            }
            String roomId = roomSelection.split(" - ")[0];
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();
            String purpose = purposeArea.getText().trim();
            
            if (name.isEmpty() || dept.isEmpty() || purpose.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            Date startDate = (Date) startDateSpinner.getValue();
            Date endDate = (Date) endDateSpinner.getValue();
            
            LocalDateTime start = LocalDateTime.ofInstant(startDate.toInstant(), java.time.ZoneId.systemDefault());
            LocalDateTime end = LocalDateTime.ofInstant(endDate.toInstant(), java.time.ZoneId.systemDefault());
            
            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(dialog, "Thời gian kết thúc phải sau thời gian bắt đầu!");
                return;
            }
            
            BookingRequest booking = new BookingRequest(id, roomId, name, dept, start, end, purpose, "Chờ duyệt");
            DataManager.getInstance().addBooking(booking);
            JOptionPane.showMessageDialog(dialog, "Tạo yêu cầu thành công!");
            dialog.dispose();
            showBookingManagement();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showFacilityManagement() {
        contentPanel.removeAll();
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0xF5F5F5));
        
        JLabel titleLabel = new JLabel("Quản lý cơ sở vật chất");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x1976D2));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Mã CSVC", "Tên", "Danh mục", "Số lượng", "Tình trạng"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Facility facility : DataManager.getInstance().getFacilities()) {
            model.addRow(new Object[]{
                facility.getId(),
                facility.getName(),
                facility.getCategory(),
                facility.getQuantity(),
                facility.getCondition()
            });
        }
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0x1976D2));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xE0E0E0)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(0xF5F5F5));
        
        JButton addButton = createStyledButton("Thêm CSVC", new Color(0x4CAF50));
        JButton editButton = createStyledButton("Chỉnh sửa", new Color(0xFF9800));
        JButton deleteButton = createStyledButton("Xóa", new Color(0xF44336));
        
        addButton.addActionListener(e -> showAddFacilityDialog());
        
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String facilityId = (String) model.getValueAt(selectedRow, 0);
                for (Facility facility : DataManager.getInstance().getFacilities()) {
                    if (facility.getId().equals(facilityId)) {
                        showEditFacilityDialog(facility);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn CSVC cần chỉnh sửa!");
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa CSVC này?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String facilityId = (String) model.getValueAt(selectedRow, 0);
                    DataManager.getInstance().getFacilities().removeIf(f -> f.getId().equals(facilityId));
                    model.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn CSVC cần xóa!");
            }
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAddFacilityDialog() {
        JDialog dialog = new JDialog(this, "Thêm cơ sở vật chất mới", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Thiết bị điện tử", "Thiết bị âm thanh", "Văn phòng phẩm", "Nội thất", "Khác"
        });
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        JComboBox<String> conditionCombo = new JComboBox<>(new String[]{"Tốt", "Khá", "Trung bình", "Cần bảo trì"});
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mã CSVC:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Danh mục:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        panel.add(quantitySpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Tình trạng:"), gbc);
        gbc.gridx = 1;
        panel.add(conditionCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createStyledButton("Lưu", new Color(0x4CAF50));
        JButton cancelButton = createStyledButton("Hủy", new Color(0x757575));
        
        saveButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            int quantity = (Integer) quantitySpinner.getValue();
            String condition = (String) conditionCombo.getSelectedItem();
            
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            Facility facility = new Facility(id, name, category, quantity, condition);
            DataManager.getInstance().addFacility(facility);
            JOptionPane.showMessageDialog(dialog, "Thêm CSVC thành công!");
            dialog.dispose();
            showFacilityManagement();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showEditFacilityDialog(Facility facility) {
        if (facility == null) return;
        
        JDialog dialog = new JDialog(this, "Chỉnh sửa cơ sở vật chất", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(facility.getName(), 20);
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Thiết bị điện tử", "Thiết bị âm thanh", "Văn phòng phẩm", "Nội thất", "Khác"
        });
        categoryCombo.setSelectedItem(facility.getCategory());
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(facility.getQuantity(), 1, 1000, 1));
        JComboBox<String> conditionCombo = new JComboBox<>(new String[]{"Tốt", "Khá", "Trung bình", "Cần bảo trì"});
        conditionCombo.setSelectedItem(facility.getCondition());
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Danh mục:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        panel.add(quantitySpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tình trạng:"), gbc);
        gbc.gridx = 1;
        panel.add(conditionCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createStyledButton("Cập nhật", new Color(0xFF9800));
        JButton cancelButton = createStyledButton("Hủy", new Color(0x757575));
        
        saveButton.addActionListener(e -> {
            facility.setName(nameField.getText().trim());
            facility.setCategory((String) categoryCombo.getSelectedItem());
            facility.setQuantity((Integer) quantitySpinner.getValue());
            facility.setCondition((String) conditionCombo.getSelectedItem());
            
            JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
            dialog.dispose();
            showFacilityManagement();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showReportPanel() {
        contentPanel.removeAll();
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0xF5F5F5));
        
        JLabel titleLabel = new JLabel("Thống kê và Báo cáo");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x1976D2));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        statsPanel.setBackground(new Color(0xF5F5F5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Thống kê phòng họp
        JPanel roomStats = createReportCard("Thống kê Phòng Họp", new Color(0x4CAF50));
        JTextArea roomText = new JTextArea();
        roomText.setEditable(false);
        roomText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roomText.setBackground(Color.WHITE);
        roomText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        int totalRooms = DataManager.getInstance().getRooms().size();
        int availableRooms = 0;
        int maintenanceRooms = 0;
        
        for (MeetingRoom room : DataManager.getInstance().getRooms()) {
            if (room.getStatus().equals("Sẵn sàng")) availableRooms++;
            if (room.getStatus().equals("Bảo trì")) maintenanceRooms++;
        }
        
        roomText.setText(String.format(
            "Tổng số phòng: %d\n" +
            "Phòng sẵn sàng: %d\n" +
            "Phòng bảo trì: %d\n" +
            "Phòng đang sử dụng: %d",
            totalRooms, availableRooms, maintenanceRooms, 
            totalRooms - availableRooms - maintenanceRooms
        ));
        
        roomStats.add(roomText, BorderLayout.CENTER);
        statsPanel.add(roomStats);
        
        // Thống kê yêu cầu thuê
        JPanel bookingStats = createReportCard("Thống kê Yêu cầu Thuê", new Color(0x2196F3));
        JTextArea bookingText = new JTextArea();
        bookingText.setEditable(false);
        bookingText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookingText.setBackground(Color.WHITE);
        bookingText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        int totalBookings = DataManager.getInstance().getBookings().size();
        int pendingBookings = DataManager.getInstance().getPendingBookingsCount();
        int approvedBookings = DataManager.getInstance().getApprovedBookingsCount();
        int rejectedBookings = 0;
        
        for (BookingRequest booking : DataManager.getInstance().getBookings()) {
            if (booking.getStatus().equals("Từ chối")) rejectedBookings++;
        }
        
        bookingText.setText(String.format(
            "Tổng yêu cầu: %d\n" +
            "Chờ duyệt: %d\n" +
            "Đã duyệt: %d\n" +
            "Từ chối: %d",
            totalBookings, pendingBookings, approvedBookings, rejectedBookings
        ));
        
        bookingStats.add(bookingText, BorderLayout.CENTER);
        statsPanel.add(bookingStats);
        
        // Thống kê doanh thu
        JPanel revenueStats = createReportCard("Thống kê Doanh Thu", new Color(0xFF9800));
        JTextArea revenueText = new JTextArea();
        revenueText.setEditable(false);
        revenueText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        revenueText.setBackground(Color.WHITE);
        revenueText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        double totalRevenue = 0;
        int totalHours = 0;
        
        for (BookingRequest booking : DataManager.getInstance().getBookings()) {
            if (booking.getStatus().equals("Đã duyệt")) {
                MeetingRoom room = DataManager.getInstance().getRoomById(booking.getRoomId());
                if (room != null) {
                    long hours = booking.getDurationHours();
                    totalHours += hours;
                    totalRevenue += hours * room.getPricePerHour();
                }
            }
        }
        
        revenueText.setText(String.format(
            "Tổng doanh thu: %,.0f VNĐ\n" +
            "Tổng giờ thuê: %d giờ\n" +
            "Trung bình/giờ: %,.0f VNĐ",
            totalRevenue, totalHours, 
            totalHours > 0 ? totalRevenue / totalHours : 0
        ));
        
        revenueStats.add(revenueText, BorderLayout.CENTER);
        statsPanel.add(revenueStats);
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        // Nút xuất báo cáo
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(0xF5F5F5));
        
        JButton exportButton = createStyledButton("Xuất báo cáo PDF", new Color(0xE91E63));
        exportButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Tính năng xuất báo cáo PDF sẽ được phát triển trong phiên bản tiếp theo!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonPanel.add(exportButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createReportCard(String title, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xE0E0E0), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(color);
        card.add(titleLabel, BorderLayout.NORTH);
        
        return card;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn đăng xuất?", 
            "Xác nhận đăng xuất", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}