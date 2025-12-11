package javaswingg.newpackage.Giaodien;
import javax.swing.*;
import java.awt.*;
import Data.DAOadmin;
import javaswingg.newpackage.Entity.Admin;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private DAOadmin adminDAO = new DAOadmin();   
    public LoginFrame() {
        setTitle("Hệ thống Booking Phòng Họp");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(450, 350));
      
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(0xF5F5F5));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(new Color(0x1976D2));
        headerPanel.setPreferredSize(new Dimension(0, 100));
        JLabel titleLabel = new JLabel("HỆ THỐNG BOOKING PHÒNG HỌP");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
       
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(Box.createVerticalGlue());
 
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(450, 300));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel loginLabel = new JLabel("Đăng nhập");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginLabel.setForeground(new Color(0x333333));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(250, 35));
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
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCCCCCC)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(passwordField, gbc);
        
        // Thêm Enter key listener cho password field
        passwordField.addActionListener(e -> login());
        
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
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalGlue());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
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
        // Thêm hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }   
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());       
        // Kiểm tra trống
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng điền đầy đủ thông tin!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }       
        // Kiểm tra đăng nhập và LẤY THÔNG TIN ADMIN (bao gồm fullname)
        try {
            Admin admin = adminDAO.login(username, password);
            
            if (admin != null) {
                JOptionPane.showMessageDialog(this, 
                    "Chào mừng " + admin.getFullName() + "!", 
                    "Đăng nhập thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Tên đăng nhập hoặc mật khẩu không đúng!\nVui lòng thử lại.", 
                    "Đăng nhập thất bại", 
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi kết nối database:\n" + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }   
    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Đăng ký tài khoản", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setMinimumSize(new Dimension(400, 320));
        
        JPanel mainDialogPanel = new JPanel();
        mainDialogPanel.setLayout(new BoxLayout(mainDialogPanel, BoxLayout.Y_AXIS));
        mainDialogPanel.setBackground(Color.WHITE);
        
        mainDialogPanel.add(Box.createVerticalGlue());
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(400, 280));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Đăng ký tài khoản mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField regUsername = new JTextField(20);
        JPasswordField regPassword = new JPasswordField(20);
        JTextField regFullName = new JTextField(20);
        
        regUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        regPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        regFullName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        regUsername.setPreferredSize(new Dimension(250, 35));
        regPassword.setPreferredSize(new Dimension(250, 35));
        regFullName.setPreferredSize(new Dimension(250, 35));
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblUsername, gbc);
        gbc.gridx = 1;
        panel.add(regUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblPassword, gbc);
        gbc.gridx = 1;
        panel.add(regPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblFullName = new JLabel("Họ và tên:");
        lblFullName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblFullName, gbc);
        gbc.gridx = 1;
        panel.add(regFullName, gbc);
        
        JButton registerBtn = createStyledButton("Đăng ký", new Color(0x43A047));
        registerBtn.addActionListener(e -> {
            String username = regUsername.getText().trim();
            String password = new String(regPassword.getPassword());
            String fullName = regFullName.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                if (adminDAO.registerUser(username, password, fullName)) {
                    JOptionPane.showMessageDialog(dialog, "Đăng ký thành công!\nBạn có thể đăng nhập ngay bây giờ.", "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "Tên đăng nhập đã tồn tại!\nVui lòng chọn tên khác.", "Đăng ký thất bại", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi đăng ký:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(registerBtn, gbc);        
        mainDialogPanel.add(panel);
        mainDialogPanel.add(Box.createVerticalGlue());       
        dialog.add(mainDialogPanel);
        dialog.setVisible(true);
    }
}