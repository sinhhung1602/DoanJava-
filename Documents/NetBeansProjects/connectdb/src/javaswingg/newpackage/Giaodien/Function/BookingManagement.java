package javaswingg.newpackage.Giaodien.Function;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import Data.BookingDAO;
import Data.RoomDAO;
import javaswingg.newpackage.Entity.BookingRequest;
import javaswingg.newpackage.Entity.MeetingRoom;
import javaswingg.newpackage.Giaodien.UI.UI;

public class BookingManagement extends JPanel {
   private JFrame parentFrame;
    private JTable table;
    private DefaultTableModel model;
    private final BookingDAO bookingDAO = new BookingDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private Dashboard dashboard;
    
    public BookingManagement(JFrame parentFrame, Dashboard dashboard) {
        this.parentFrame = parentFrame;
        this.dashboard = dashboard;
        initializeUI();
    }
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UI.BACKGROUND_COLOR);
        add(UI.createTitleLabel("Quản lý yêu cầu thuê phòng"), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane createTablePanel() {
        String[] columns = {
                "Mã YC","Mã KH", "Phòng", "Người yêu cầu", "Phòng ban",
                "Thời gian bắt đầu", "Thời gian kết thúc",
                "Mục đích", "Trạng thái"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        refreshTable();
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(UI.PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        return new JScrollPane(table);
    }
    public void refreshTable() {
        model.setRowCount(0);
        List<BookingRequest> bookings = bookingDAO.getAllBookings();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (BookingRequest booking : bookings) {
            MeetingRoom room = roomDAO.getRoomById(booking.getRoomId());

            model.addRow(new Object[]{booking.getId(),
                    booking.getCustomerId(),
                    room != null ? room.getName() : booking.getRoomId(),
                    booking.getRequesterName(),
                    booking.getDepartment(),
                    booking.getStartTime().format(fmt),
                    booking.getEndTime().format(fmt),
                    booking.getPurpose(),
                    booking.getStatus()
            });
        }
    }
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UI.BACKGROUND_COLOR);
        JButton addButton = UI.createStyledButton("➕ Tạo yêu cầu mới", UI.SUCCESS_COLOR);
        JButton approveButton = UI.createStyledButton("✅ Duyệt", UI.INFO_COLOR);
        JButton deleteButton = UI.createStyledButton("❌ Xóa yêu cầu", UI.DANGER_COLOR);
        addButton.addActionListener(e -> showNewBookingDialog());
        approveButton.addActionListener(e -> approveBooking());
        deleteButton.addActionListener(e -> deleteBooking());
        buttonPanel.add(addButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(deleteButton);
        return buttonPanel;
    }
    public void showNewBookingDialog() {
        BookingDialog dialog = new BookingDialog(parentFrame);
        dialog.setVisible(true);
        refreshTable();
        if (dashboard != null) {
            dashboard.refreshStats();
        }
    }
    private void approveBooking() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn yêu cầu cần duyệt!");
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        bookingDAO.updateStatus(id, "Đã duyệt");
        JOptionPane.showMessageDialog(parentFrame, "Đã duyệt yêu cầu!");
        refreshTable();
        if (dashboard != null) {
            dashboard.refreshStats();
        }
    }
    private void deleteBooking() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn yêu cầu cần xóa!");
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
            parentFrame,
            "Bạn có chắc chắn muốn xóa yêu cầu này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );      
        if (confirm == JOptionPane.YES_OPTION) {
            bookingDAO.deleteBooking(id);
            JOptionPane.showMessageDialog(parentFrame, "Đã xóa yêu cầu!");
            refreshTable();
            if (dashboard != null) {
                dashboard.refreshStats();
            }
        }
    }
    public class BookingDialog extends JDialog {
        private JTextField idField, customerIdField, nameField, deptField;
        private JComboBox<String> roomCombo;
        private JSpinner startSpinner, endSpinner;
        private JTextArea purposeArea;

        public BookingDialog(JFrame parent) {
            super(parent, "Tạo yêu cầu thuê phòng mới", true);
            initializeUI();
        }
        private void initializeUI() {
            setSize(550, 550);
            setLocationRelativeTo(getParent());
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            idField = new JTextField(20);
            customerIdField = new JTextField(20);
            nameField = new JTextField(20);
            deptField = new JTextField(20);

            roomCombo = new JComboBox<>();
            List<MeetingRoom> rooms = roomDAO.getAllRooms();
            for (MeetingRoom r : rooms) {
                roomCombo.addItem(r.getId() + " - " + r.getName());
            }
            startSpinner = new JSpinner(new SpinnerDateModel());
            endSpinner = new JSpinner(new SpinnerDateModel());

            purposeArea = new JTextArea(3, 20);
            JScrollPane purposeScroll = new JScrollPane(purposeArea);
            int row = 0;
            addField(panel, gbc, row++, "Mã yêu cầu:", idField);
            addField(panel, gbc, row++, "Phòng họp:", roomCombo);
            addField(panel, gbc, row++, "Mã KH:", customerIdField);
            addField(panel, gbc, row++, "Người yêu cầu:", nameField);
            addField(panel, gbc, row++, "Phòng ban:", deptField);
            addField(panel, gbc, row++, "Thời gian bắt đầu:", startSpinner);
            addField(panel, gbc, row++, "Thời gian kết thúc:", endSpinner);
            addField(panel, gbc, row++, "Mục đích:", purposeScroll);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(20, 0, 0, 0);
            panel.add(createButtonPanelDialog(), gbc);

            add(panel);
        }
        private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, Component comp) {
            gbc.gridx = 0;
            gbc.gridy = row;
            panel.add(new JLabel(label), gbc);

            gbc.gridx = 1;
            panel.add(comp, gbc);
        }

        private JPanel createButtonPanelDialog() {
            JPanel btnPanel = new JPanel(new FlowLayout());
            btnPanel.setBackground(Color.WHITE);

            JButton saveBtn = UI.createStyledButton("✅ Tạo yêu cầu", UI.SUCCESS_COLOR);
            JButton cancelBtn = UI.createStyledButton("❌ Hủy", UI.GREY_COLOR);

            saveBtn.addActionListener(e -> saveBooking());
            cancelBtn.addActionListener(e -> dispose());

            btnPanel.add(saveBtn);
            btnPanel.add(cancelBtn);
            return btnPanel;
        }

        private void saveBooking() {
            try {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập mã yêu cầu!");
                    return;
                }
                
                String customerId = customerIdField.getText().trim();
                if (customerId.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập mã KH!");
                    return;
                }

                if (roomCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Không có phòng họp nào để chọn!");
                    return;
                }

                String roomId = ((String) roomCombo.getSelectedItem()).split(" - ")[0];

                String requester = nameField.getText().trim();
                String dept = deptField.getText().trim();
                String purpose = purposeArea.getText().trim();

                if (requester.isEmpty() || dept.isEmpty() || purpose.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }
                var start = ((java.util.Date) startSpinner.getValue())
                        .toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                var end = ((java.util.Date) endSpinner.getValue())
                        .toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();

                if (end.isBefore(start)) {
                    JOptionPane.showMessageDialog(this, "Thời gian kết thúc phải sau thời gian bắt đầu!");
                    return;
                }
                BookingRequest booking = new BookingRequest(
                        id, roomId, customerId, requester, dept, start, end, purpose, "Chờ duyệt"
                );
                
                bookingDAO.addBooking(booking);
                JOptionPane.showMessageDialog(this, "Tạo yêu cầu thành công!");
                dispose();

            } catch (HeadlessException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi tạo yêu cầu: " + ex.getMessage());
            }
        }
    }
}