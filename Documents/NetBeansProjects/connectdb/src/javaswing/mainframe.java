package javaswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class mainframe extends JFrame {

    public mainframe() {

        // ===== WINDOW =====
        setTitle("Hệ Thống Booking Phòng Họp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, 55));
        topBar.setBackground(new Color(32, 38, 50)); // xám đậm
        JLabel title = new JLabel("HỆ THỐNG BOOKING PHÒNG HỌP", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        topBar.add(title, BorderLayout.CENTER);

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(8, 1, 0, 8));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(new Color(44, 52, 66));

        // Các nút menu
        sidebar.add(createMenuItem("Đăng nhập / Đăng ký"));
        sidebar.add(createMenuItem("Quản lý Phòng họp"));
        sidebar.add(createMenuItem("Quản lý Yêu cầu thuê"));
        sidebar.add(createMenuItem("Quản lý Cơ sở vật chất"));
        sidebar.add(createMenuItem("Thống kê & Báo cáo"));
        sidebar.add(createMenuItem("Cài đặt"));
        sidebar.add(createMenuItem("Trợ giúp"));
        sidebar.add(createMenuItem("Thoát"));

        // ===== MAIN CONTENT =====
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(245, 247, 250)); // nền sáng

        // Card chính giữa
        JPanel welcomeCard = new JPanel();
        welcomeCard.setBackground(Color.WHITE);
        welcomeCard.setBounds(200, 100, 600, 350);
        welcomeCard.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        welcomeCard.setLayout(new BorderLayout());

        JLabel welcomeText = new JLabel("Chào mừng bạn đến hệ thống quản lý phòng họp",
                SwingConstants.CENTER);
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeCard.add(welcomeText, BorderLayout.CENTER);

        mainPanel.add(welcomeCard);

        // ===== ADD TO FRAME =====
        add(topBar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Tạo từng item menu chuyên nghiệp hơn
    private JPanel createMenuItem(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(44, 52, 66));

        JLabel label = new JLabel("   " + text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        panel.add(label, BorderLayout.CENTER);

        // Hover Effect
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(60, 70, 85)); // sáng hơn
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(44, 52, 66));
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new mainframe());
    }
}
