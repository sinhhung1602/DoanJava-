package javaswingg.newpackage.Giaodien.Function;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Data.FacilityDAO;
import javaswingg.newpackage.Entity.Facility;
import javaswingg.newpackage.Giaodien.UI.UI;

public class FacilityManagement extends JPanel {
    private JFrame parentFrame;
    private JTable table;
    private DefaultTableModel model;
    private FacilityDAO facilityDAO = new FacilityDAO();
    private Dashboard dashboard;
    public FacilityManagement(JFrame parentFrame, Dashboard dashboard) {
        this.parentFrame = parentFrame;
        this.dashboard = dashboard;
        initializeUI();
    }
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UI.BACKGROUND_COLOR);

        add(UI.createTitleLabel("Quản lý cơ sở vật chất"), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"Mã CSVC", "Tên", "Danh mục", "Số lượng", "Tình trạng"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        refreshTable();
        table = UI.createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UI.BORDER_COLOR));

        return scrollPane;
    }
    public void refreshTable() {
        model.setRowCount(0);

        List<Facility> list = facilityDAO.getAllFacilities();

        for (Facility f : list) {
            model.addRow(new Object[]{
                    f.getId(),
                    f.getName(),
                    f.getCategory(),
                    f.getQuantity(),
                    f.getCondition()
            });
        }
    }
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(UI.BACKGROUND_COLOR);

        JButton addBtn = UI.createStyledButton("➕ Thêm CSVC", UI.SUCCESS_COLOR);
        JButton editBtn = UI.createStyledButton("✏️ Chỉnh sửa", UI.WARNING_COLOR);
        JButton deleteBtn = UI.createStyledButton("❌ Xóa", UI.DANGER_COLOR);

        addBtn.addActionListener(e -> showAddDialog());
        editBtn.addActionListener(e -> showEditDialog());
        deleteBtn.addActionListener(e -> deleteFacility());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        return panel;
    }
    public void showAddDialog() {
        FacilityDialog dialog = new FacilityDialog(parentFrame, null, facilityDAO);
        dialog.setVisible(true);
        refreshTable();
        if (dashboard != null) {
            dashboard.refreshStats();
        }
    }
    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn CSVC cần chỉnh sửa!");
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        Facility f = facilityDAO.getFacilityById(id);
        FacilityDialog dialog = new FacilityDialog(parentFrame, f, facilityDAO);
        dialog.setVisible(true);
        refreshTable();
        if (dashboard != null) {
            dashboard.refreshStats();
        }
    }
    private void deleteFacility() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn CSVC cần xóa!");
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(parentFrame,
                "Bạn có chắc chắn muốn xóa CSVC này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            facilityDAO.deleteFacility(id);
            JOptionPane.showMessageDialog(parentFrame, "Xóa thành công!");
            refreshTable();
            if (dashboard != null) {
                dashboard.refreshStats();
            }
        }
    }   
    public class FacilityDialog extends JDialog {
        private Facility facility;
        private FacilityDAO facilityDAO;
        private JTextField idField;
        private JTextField nameField;
        private JComboBox<String> categoryCombo;
        private JSpinner quantitySpinner;
        private JComboBox<String> conditionCombo;

        public FacilityDialog(JFrame parent, Facility facility, FacilityDAO dao) {
            super(parent, facility == null ? "Thêm cơ sở vật chất" : "Chỉnh sửa CSVC", true);

            this.facility = facility;
            this.facilityDAO = dao;

            initializeUI();   
        }
        private void initializeUI() {
            setSize(400, 350);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            idField = new JTextField(20);
            nameField = new JTextField(20);
            categoryCombo = new JComboBox<>(new String[] {"Máy móc", "Thiết bị", "Nội thất"});
            conditionCombo = new JComboBox<>(new String[] {"Tốt", "Hư hỏng", "Đang sửa"});
            quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

            int row = 0;

            gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Mã CSVC:"), gbc);
            gbc.gridx = 1; panel.add(idField, gbc);

            gbc.gridx = 0; gbc.gridy = ++row; panel.add(new JLabel("Tên:"), gbc);
            gbc.gridx = 1; panel.add(nameField, gbc);

            gbc.gridx = 0; gbc.gridy = ++row; panel.add(new JLabel("Danh mục:"), gbc);
            gbc.gridx = 1; panel.add(categoryCombo, gbc);

            gbc.gridx = 0; gbc.gridy = ++row; panel.add(new JLabel("Số lượng:"), gbc);
            gbc.gridx = 1; panel.add(quantitySpinner, gbc);

            gbc.gridx = 0; gbc.gridy = ++row; panel.add(new JLabel("Tình trạng:"), gbc);
            gbc.gridx = 1; panel.add(conditionCombo, gbc);

            gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2;
            panel.add(createButtonPanel(), gbc);
            add(panel);
            if (facility != null) loadFacilityData();
        }
        private void loadFacilityData() {
            idField.setText(facility.getId());
            idField.setEditable(false);
            nameField.setText(facility.getName());
            categoryCombo.setSelectedItem(facility.getCategory());
            quantitySpinner.setValue(facility.getQuantity());
            conditionCombo.setSelectedItem(facility.getCondition());
        }
        private JPanel createButtonPanel() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));

            JButton saveBtn = UI.createStyledButton("Lưu", UI.SUCCESS_COLOR);
            JButton cancelBtn = UI.createStyledButton("Hủy", UI.GREY_COLOR);

            saveBtn.addActionListener(e -> saveFacility());
            cancelBtn.addActionListener(e -> dispose());

            p.add(saveBtn);
            p.add(cancelBtn);

            return p;
        }
        private void saveFacility() {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập đầy đủ thông tin!");
                return;
            }
            Facility f = new Facility(
                    id,
                    name,
                    (String) categoryCombo.getSelectedItem(),
                    (Integer) quantitySpinner.getValue(),
                    (String) conditionCombo.getSelectedItem()
            );
            if (facility == null) {
                facilityDAO.addFacility(f);
                JOptionPane.showMessageDialog(this, "Thêm CSVC thành công!");
            } else {
                facilityDAO.updateFacility(f);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            }
            dispose();
        }
    }
}