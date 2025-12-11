package javaswingg.newpackage.Giaodien.Function;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Data.RoomDAO;
import javaswingg.newpackage.Entity.MeetingRoom;
import javaswingg.newpackage.Giaodien.UI.UI;

public class RoomManagement extends JPanel {

    private JFrame parentFrame;
    private JTable table;
    private DefaultTableModel model;
    private RoomDAO roomDAO = new RoomDAO();
    private Dashboard dashboard;

    public RoomManagement(JFrame parentFrame, Dashboard dashboard) {
        this.parentFrame = parentFrame;
        this.dashboard = dashboard;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UI.BACKGROUND_COLOR);

        add(UI.createTitleLabel("Quản lý phòng họp"), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"Mã phòng", "Tên phòng", "Sức chứa", "Vị trí", "Trạng thái", "Giá/giờ"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        refreshTable();
        table = UI.createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UI.BORDER_COLOR));
        return scrollPane;
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<MeetingRoom> rooms = roomDAO.getAllRooms();

        for (MeetingRoom r : rooms) {
            model.addRow(new Object[]{
                    r.getId(),
                    r.getName(),
                    r.getCapacity(),
                    r.getLocation(),
                    r.getStatus(),
                    (int)r.getPricePerHour()//
            });
        }
    }
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UI.BACKGROUND_COLOR);

        JButton addBtn = UI.createStyledButton("➕ Thêm phòng", UI.SUCCESS_COLOR);
        JButton editBtn = UI.createStyledButton("✏️ Sửa", UI.WARNING_COLOR);
        JButton delBtn = UI.createStyledButton("❌ Xóa", UI.DANGER_COLOR);

        addBtn.addActionListener(e -> showAddDialog(null));
        editBtn.addActionListener(e -> editRoom());
        delBtn.addActionListener(e -> deleteRoom());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(delBtn);

        return buttonPanel;
    }
    public void showAddDialog(MeetingRoom room) {
        new RoomDialog(parentFrame, room).setVisible(true);
        refreshTable();
        if (dashboard != null) {
            dashboard.refreshStats();
        }
    }
    private void editRoom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Chọn phòng cần sửa!");
            return;
        }
        String id = model.getValueAt(row, 0).toString();
        MeetingRoom r = roomDAO.getRoomById(id);
        showAddDialog(r);
    }
    private void deleteRoom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Chọn phòng cần xóa!");
            return;
        }
        String id = model.getValueAt(row, 0).toString();
        if (JOptionPane.showConfirmDialog(parentFrame,
                "Xóa phòng này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            if (roomDAO.deleteRoom(id)) {
                JOptionPane.showMessageDialog(parentFrame, "Xóa thành công!");
                refreshTable();
                // ← GỌI refreshStats() SAU KHI XÓA THÀNH CÔNG
                if (dashboard != null) {
                    dashboard.refreshStats();
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Xóa thất bại!");
            }
        }
    }
    public class RoomDialog extends JDialog {

        private MeetingRoom room;
        private JTextField idField, nameField, locationField, priceField;
        private JSpinner capacitySpinner;
        private JComboBox<String> statusCombo;

        public RoomDialog(JFrame parent, MeetingRoom room) {
            super(parent, room == null ? "Thêm phòng" : "Sửa phòng", true);
            this.room = room;
            initializeUI();
            if (room != null) loadRoomData();
        }

        private void initializeUI() {
            setSize(450, 420);
            setLocationRelativeTo(getParent());

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            idField = new JTextField(20);
            nameField = new JTextField(20);
            locationField = new JTextField(20);
            priceField = new JTextField(20);
            capacitySpinner = new JSpinner(new SpinnerNumberModel(10, 1, 200, 1));
            statusCombo = new JComboBox<>(new String[]{"Sẵn sàng", "Đang sử dụng", "Bảo trì"});

            if (room != null) idField.setEditable(false);

            int row = 0;
            addField(panel, gbc, row++, "Mã phòng:", idField);
            addField(panel, gbc, row++, "Tên phòng:", nameField);
            addField(panel, gbc, row++, "Sức chứa:", capacitySpinner);
            addField(panel, gbc, row++, "Vị trí:", locationField);
            addField(panel, gbc, row++, "Trạng thái:", statusCombo);
            addField(panel, gbc, row++, "Giá/giờ:", priceField);

            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
            panel.add(createButtons(), gbc);

            add(panel);
        }

        private void addField(JPanel p, GridBagConstraints gbc, int row, String label, Component comp) {
            gbc.gridx = 0;
            gbc.gridy = row;
            p.add(new JLabel(label), gbc);

            gbc.gridx = 1;
            p.add(comp, gbc);
        }

        private JPanel createButtons() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

            JButton saveBtn = UI.createStyledButton(room == null ? "Thêm" : "Lưu", 
                    room == null ? UI.SUCCESS_COLOR : UI.WARNING_COLOR);
            JButton cancelBtn = UI.createStyledButton("Hủy", UI.GREY_COLOR);

            saveBtn.addActionListener(e -> saveRoom());
            cancelBtn.addActionListener(e -> dispose());

            panel.add(saveBtn);
            panel.add(cancelBtn);
            return panel;
        }

        private void loadRoomData() {
            idField.setText(room.getId());
            nameField.setText(room.getName());
            capacitySpinner.setValue(room.getCapacity());
            locationField.setText(room.getLocation());
            statusCombo.setSelectedItem(room.getStatus());
            priceField.setText(String.valueOf(room.getPricePerHour()));
        }

        private void saveRoom() {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                String location = locationField.getText();
                String status = (String) statusCombo.getSelectedItem();
                int cap = (int) capacitySpinner.getValue();
                double price = Double.parseDouble(priceField.getText());

                MeetingRoom r = new MeetingRoom(id, name, cap, location, status, price);

                boolean ok = (room == null)
                        ? roomDAO.addRoom(r)
                        : roomDAO.updateRoom(r);

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Lưu thành công!");
                    dispose();
                    // Dashboard sẽ được refresh trong showAddDialog()
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu phòng!");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Giá phòng phải là số!");
            }
        }
    }
}