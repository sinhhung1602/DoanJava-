package javaswingg.newpackage.Giaodien.Function;

import javax.swing.*;
import java.awt.*;
import javaswingg.newpackage.Entity.BookingRequest;
import javaswingg.newpackage.Entity.MeetingRoom;
import Data.BookingDAO;
import Data.RoomDAO;
import javaswingg.newpackage.Giaodien.UI.UI;
import java.util.List;
public class Report extends JPanel {

    private JFrame parentFrame;
    private RoomDAO roomDAO = new RoomDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private JPanel roomStatsCard;
    private JPanel bookingStatsCard;
    private JPanel revenueStatsCard;
    private JPanel statsPanel;
    public Report(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializeUI();
    }
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UI.BACKGROUND_COLOR);
        add(UI.createTitleLabel("Thống kê và Báo cáo"), BorderLayout.NORTH);
        add(createStatsScrollPane(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }
    private JScrollPane createStatsScrollPane() {
        statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setBackground(UI.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0;

        gbc.gridy = 0;
        roomStatsCard = createRoomStatsCard();
        statsPanel.add(roomStatsCard, gbc);

        gbc.gridy = 1;
        bookingStatsCard = createBookingStatsCard();
        statsPanel.add(bookingStatsCard, gbc);

        gbc.gridy = 2;
        revenueStatsCard = createRevenueStatsCard();
        statsPanel.add(revenueStatsCard, gbc);

        JScrollPane scrollPane = new JScrollPane(statsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }
    public void refreshReport() {
        statsPanel.removeAll();        
        // Tạo lại các card mới với dữ liệu mới nhất
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;

        gbc.gridy = 0;
        roomStatsCard = createRoomStatsCard();
        statsPanel.add(roomStatsCard, gbc);

        gbc.gridy = 1;
        bookingStatsCard = createBookingStatsCard();
        statsPanel.add(bookingStatsCard, gbc);

        gbc.gridy = 2;
        revenueStatsCard = createRevenueStatsCard();
        statsPanel.add(revenueStatsCard, gbc);
        
        // Cập nhật giao diện
        statsPanel.revalidate();
        statsPanel.repaint();        
        System.out.println("📊 Report refreshed!");
    }
    private JPanel createRoomStatsCard() {
        List<MeetingRoom> rooms = roomDAO.getAllRooms();
        int totalRooms = rooms.size();
        int availableRooms = 0;
        int maintenanceRooms = 0;
        for (MeetingRoom r : rooms) {
            switch (r.getStatus()) {
                case "Sẵn sàng" -> availableRooms++;
                case "Bảo trì" -> maintenanceRooms++;
            }
        }
        JPanel card = UI.createReportCard("Thống kê Phòng Họp", UI.SUCCESS_COLOR);
        JTextArea textArea = new JTextArea(
                String.format(
                        """
                        Tổng số phòng: %d
                        Phòng sẵn sàng: %d
                        Phòng bảo trì: %d
                        Phòng đang sử dụng: %d
                        """,
                        totalRooms, availableRooms, maintenanceRooms,
                        totalRooms - availableRooms - maintenanceRooms
                )
        );
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.add(textArea, BorderLayout.CENTER);
        return card;
    }
    private JPanel createBookingStatsCard() {

        List<BookingRequest> bookings = bookingDAO.getAllBookings();
        int total = bookings.size();
        int pending = 0;
        int approved = 0;   
        for (BookingRequest b : bookings) {
            String status = b.getStatus();
            if ("Chờ duyệt".equalsIgnoreCase(status)) {
                pending++;
            } else if ("Đã duyệt".equalsIgnoreCase(status)) {
                approved++;
            }
        }
        JPanel card = UI.createReportCard("Thống kê Yêu cầu Thuê", UI.INFO_COLOR);

        JTextArea textArea = new JTextArea(
                String.format(
                        """
                        Tổng yêu cầu: %d
                        Chờ duyệt: %d
                        Đã duyệt: %d
                        """,
                        total, pending, approved
                )
        );
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.add(textArea, BorderLayout.CENTER);
        return card;
    }
    private JPanel createRevenueStatsCard() {

        List<BookingRequest> bookings = bookingDAO.getAllBookings();
        List<MeetingRoom> rooms = roomDAO.getAllRooms();
        double totalRevenue = 0;
        int totalHours = 0;
        for (BookingRequest b : bookings) {
            if (b.getStatus().equals("Đã duyệt")) {

                MeetingRoom room = rooms.stream()
                        .filter(r -> r.getRoomId().equals(b.getRoomId()))
                        .findFirst()
                        .orElse(null);

                if (room != null) {
                    int hours = (int) b.getDurationHours();
                    totalHours += hours;
                    totalRevenue += hours * room.getPricePerHour();
                }
            }
        }
        JPanel card = UI.createReportCard("Thống kê Doanh Thu", UI.WARNING_COLOR);
        JTextArea textArea = new JTextArea(
                String.format(
                        """
                        Tổng giờ thuê: %d giờ
                        Tổng doanh thu: %,.0f VNĐ
                        Trung bình/giờ: %,.0f VNĐ
                        """,
                        totalHours,
                        totalRevenue,
                        totalHours > 0 ? totalRevenue / totalHours : 0
                )
        );
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        card.add(textArea, BorderLayout.CENTER);

        return card;
    }
    private JPanel createBottomPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(UI.BACKGROUND_COLOR);

        JButton exportButton = UI.createStyledButton("📊 Xuất báo cáo", UI.PINK_COLOR);
        exportButton.addActionListener(e -> showExportReportDialog());

        bottom.add(exportButton);
        return bottom;
    }

    private void showExportReportDialog() {
        String report = ReportGenerator.generateReport(roomDAO, bookingDAO);

        JDialog dialog = new JDialog(parentFrame, "Nội dung báo cáo", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(parentFrame);

        JTextArea textArea = new JTextArea(report);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        dialog.add(new JScrollPane(textArea));
        dialog.setVisible(true);
    }
    public static class ReportGenerator {
        public static String generateReport(RoomDAO roomDAO, BookingDAO bookingDAO) {
            List<MeetingRoom> rooms = roomDAO.getAllRooms();
            List<BookingRequest> bookings = bookingDAO.getAllBookings();
            StringBuilder sb = new StringBuilder();
            sb.append("======= BÁO CÁO HỆ THỐNG =======\n\n");

            int totalRooms = rooms.size();
            int available = 0, maintenance = 0;
            for (MeetingRoom r : rooms) {
                if (r.getStatus().equals("Sẵn sàng")) available++;
                if (r.getStatus().equals("Bảo trì")) maintenance++;
            }

            sb.append("---- PHÒNG HỌP ----\n");
            sb.append(String.format(
                    """
                    Tổng: %d
                    Sẵn sàng: %d
                    Bảo trì: %d
                    Đang sử dụng: %d
                    
                    """,
                    totalRooms, available, maintenance,
                    totalRooms - available - maintenance
            ));

            int pending = 0, approved = 0;

            for (BookingRequest b : bookings) {
                String status = b.getStatus();
                if ("Chờ duyệt".equalsIgnoreCase(status)) {
                    pending++;
                } else if ("Đã duyệt".equalsIgnoreCase(status)) {
                    approved++;
                }
            }
            sb.append("---- YÊU CẦU ----\n");
            sb.append(String.format(
                    """
                    Tổng: %d
                    Chờ duyệt: %d
                    Đã duyệt: %d
                    
                    """,
                    bookings.size(), pending, approved
            ));
            double revenue = 0;
            int hours = 0;

            for (BookingRequest b : bookings) {
                if ("Đã duyệt".equalsIgnoreCase(b.getStatus())) {

                    MeetingRoom room = rooms.stream()
                            .filter(r -> r.getRoomId().equals(b.getRoomId()))
                            .findFirst().orElse(null);

                    if (room != null) {
                        int h = (int) b.getDurationHours();
                        hours += h;
                        revenue += h * room.getPricePerHour();
                    }
                }
            }
            sb.append("---- DOANH THU ----\n");
            sb.append(String.format(
                    """
                    Tổng giờ thuê: %d
                    Doanh thu: %,.0f VNĐ
                    Trung bình/giờ: %,.0f VNĐ
                    """,
                    hours,
                    revenue,
                    hours > 0 ? revenue / hours : 0
            ));
            return sb.toString();
        }
    }
}