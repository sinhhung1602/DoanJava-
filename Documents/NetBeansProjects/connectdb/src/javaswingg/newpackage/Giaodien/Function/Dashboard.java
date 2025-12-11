package javaswingg.newpackage.Giaodien.Function;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javaswingg.newpackage.Giaodien.UI.UI;
import Data.RoomDAO;
import Data.BookingDAO;
import Data.FacilityDAO;
import Data.DAOadmin;

public class Dashboard extends JPanel {
    private RoomDAO roomDAO = new RoomDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private FacilityDAO facilityDAO = new FacilityDAO();
    private DAOadmin adminDAO = new DAOadmin();
    
    // Lưu tất cả các label cần cập nhật
    private JLabel totalRoomsLabel;
    private JLabel pendingLabel;
    private JLabel facilityLabel;
    private JLabel approveLabel;
    
    public Dashboard(JFrame parentFrame) {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(UI.BACKGROUND_COLOR);
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createStatsScrollPane(), BorderLayout.CENTER);
    }
    
    /* ---------------------------- HEADER ---------------------------- */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UI.BACKGROUND_COLOR);
         
        JLabel welcome = new JLabel("Chào mừng đến với hệ thống Booking phòng họp!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcome.setForeground(UI.PRIMARY_COLOR);
        headerPanel.add(welcome, BorderLayout.WEST);
        
        JLabel dateLabel = new JLabel(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateLabel.setForeground(UI.TEXT_SECONDARY);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JScrollPane createStatsScrollPane() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(UI.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        totalRoomsLabel = new JLabel(String.valueOf(roomDAO.getAllRooms().size()));
        grid.add(UI.createModernCard(
                "Tổng phòng họp",
                totalRoomsLabel,
                UI.SUCCESS_COLOR,
                "🏢"
        ), gbc);
        
        gbc.gridx = 1;
        pendingLabel = new JLabel(String.valueOf(bookingDAO.getPendingCount()));
        grid.add(UI.createModernCard(
                "Yêu cầu chờ duyệt",
                pendingLabel,
                UI.WARNING_COLOR,
                "⏳"
        ), gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        facilityLabel = new JLabel(String.valueOf(facilityDAO.getAllFacilities().size()));
        grid.add(UI.createModernCard(
                "Cơ sở vật chất",
                facilityLabel,
                UI.INFO_COLOR,
                "🔧"
        ), gbc);
        
        gbc.gridx = 1;
        approveLabel = new JLabel(String.valueOf(bookingDAO.getApprovedCount()));
        grid.add(UI.createModernCard(
                "Yêu cầu đã duyệt",
                approveLabel,
                UI.PURPLE_COLOR,
                "✅"
        ), gbc);
        
        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    } 
    public void refreshStats() {
        if (totalRoomsLabel != null) {
            totalRoomsLabel.setText(String.valueOf(roomDAO.getAllRooms().size()));
        }
        if (pendingLabel != null) {
            pendingLabel.setText(String.valueOf(bookingDAO.getPendingCount()));
        }
        if (facilityLabel != null) {
            facilityLabel.setText(String.valueOf(facilityDAO.getAllFacilities().size()));
        }
        if (approveLabel != null) {
            approveLabel.setText(String.valueOf(bookingDAO.getApprovedCount()));
        }
        revalidate();
        repaint();       
        System.out.println("🔄 Dashboard refreshed!");
    }
}