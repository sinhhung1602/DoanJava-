package javaswingg.newpackage.Giaodien;

import java.awt.*;
import javax.swing.*;
import javaswingg.newpackage.Giaodien.Function.*;
import javaswingg.newpackage.Giaodien.UI.UI;
public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private Dashboard dashboardPanel;
    private RoomManagement roomManagementPanel;
    private BookingManagement bookingManagementPanel;
    private FacilityManagement facilityManagementPanel;
    private CustomerManagement customerManagementPanel;
    private Report reportPanel;
    
    public MainFrame() {
        initializeFrame();
        createMenuBar();
        initializePanels();
        showDashboard();
    }    
    private void initializeFrame() {
        setTitle("Hệ thống Booking Phòng Họp");
        setSize(1400, 800);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(0xF5F5F5));
        add(contentPanel);
    }  
    private void initializePanels() {
        dashboardPanel = new Dashboard(this);
        roomManagementPanel = new RoomManagement(this,dashboardPanel);
        bookingManagementPanel = new BookingManagement(this,dashboardPanel);
        facilityManagementPanel = new FacilityManagement(this,dashboardPanel);
        customerManagementPanel = new CustomerManagement(this);
        reportPanel = new Report(this);
    }    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0x1976D2));
        menuBar.setBorderPainted(false);
        
        // Home Menu
        JMenu homeMenu = UI.createStyledMenu("🏠 Trang chủ");
        JMenuItem homeItem = new JMenuItem("Về trang chủ");
        UI.styleMenuItem(homeItem);
        homeItem.addActionListener(e -> showDashboard());
        homeMenu.add(homeItem);
        
        // Room Menu
        JMenu roomMenu = UI.createStyledMenu("🏢 Quản lý phòng họp");
        JMenuItem viewRooms = new JMenuItem("Xem danh sách phòng");
        JMenuItem addRoom = new JMenuItem("Thêm phòng họp mới");
        UI.styleMenuItem(viewRooms);
        UI.styleMenuItem(addRoom);
        viewRooms.addActionListener(e -> showRoomManagement());
        addRoom.addActionListener(e -> roomManagementPanel.showAddDialog(null));
        roomMenu.add(viewRooms);
        roomMenu.add(addRoom);
        
        // Booking Menu
        JMenu bookingMenu = UI.createStyledMenu("📋 Quản lý yêu cầu thuê");
        JMenuItem newBooking = new JMenuItem("Tạo yêu cầu mới");
        JMenuItem viewBookings = new JMenuItem("Xem yêu cầu thuê");
        UI.styleMenuItem(newBooking);
        UI.styleMenuItem(viewBookings);
        newBooking.addActionListener(e -> bookingManagementPanel.showNewBookingDialog());
        viewBookings.addActionListener(e -> showBookingManagement());
        bookingMenu.add(newBooking);
        bookingMenu.add(viewBookings);
        
        // Facility Menu
        JMenu facilityMenu = UI.createStyledMenu("🔧 Quản lý cơ sở vật chất");
        JMenuItem viewFacilities = new JMenuItem("Xem cơ sở vật chất");
        JMenuItem addFacility = new JMenuItem("Thêm cơ sở vật chất");
        UI.styleMenuItem(viewFacilities);
        UI.styleMenuItem(addFacility);
        viewFacilities.addActionListener(e -> showFacilityManagement());
        addFacility.addActionListener(e -> facilityManagementPanel.showAddDialog());
        facilityMenu.add(viewFacilities);
        facilityMenu.add(addFacility);
        
        // Customer Menu
        JMenu customerMenu = UI.createStyledMenu("👥 Quản lý khách hàng");
        JMenuItem viewCustomers = new JMenuItem("Danh sách khách hàng");
        UI.styleMenuItem(viewCustomers);
        viewCustomers.addActionListener(e -> showCustomerManagement());
        customerMenu.add(viewCustomers);
        
        // Report Menu
        JMenu reportMenu = UI.createStyledMenu("📊 Thống kê báo cáo");
        JMenuItem viewReport = new JMenuItem("Xem báo cáo");
        UI.styleMenuItem(viewReport);
        viewReport.addActionListener(e -> showReport());
        reportMenu.add(viewReport);
      
        // System Menu
        JMenu systemMenu = UI.createStyledMenu("⚙️ Hệ thống");
        JMenuItem logout = new JMenuItem("Đăng xuất");
        JMenuItem exit = new JMenuItem("Thoát");
        UI.styleMenuItem(logout);
        UI.styleMenuItem(exit);
        logout.addActionListener(e -> logout());
        exit.addActionListener(e -> System.exit(0));
        systemMenu.add(logout);
        systemMenu.add(exit);
        
        menuBar.add(homeMenu);
        menuBar.add(roomMenu);
        menuBar.add(bookingMenu);
        menuBar.add(facilityMenu);
        menuBar.add(customerMenu);
        menuBar.add(reportMenu);
        menuBar.add(systemMenu);
        
        setJMenuBar(menuBar);
    }
    
    public void showDashboard() {       
        switchPanel(dashboardPanel);
    }
    
    public void showRoomManagement() {        
        switchPanel(roomManagementPanel);
    }
    
    public void showBookingManagement() {
        bookingManagementPanel.refreshTable();
        switchPanel(bookingManagementPanel);
    }
    
    public void showFacilityManagement() {
        switchPanel(facilityManagementPanel);
    }
    
    public void showCustomerManagement() {
        switchPanel(customerManagementPanel);
    }
    
    public void showReport() {
        reportPanel.refreshReport();
        switchPanel(reportPanel);
    }
    
    private void switchPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn đăng xuất?", 
            "Xác nhận đăng xuất", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new javaswingg.newpackage.Giaodien.LoginFrame().setVisible(true);
        }
    }
    public void refreshDashboard() {
        if (dashboardPanel != null) {
            dashboardPanel.refreshStats();
        }
    }
}