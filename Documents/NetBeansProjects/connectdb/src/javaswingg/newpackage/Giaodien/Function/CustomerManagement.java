package javaswingg.newpackage.Giaodien.Function;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Data.CustomerDAO;
import javaswingg.newpackage.Entity.Customer;
import javaswingg.newpackage.Giaodien.UI.UI;
public class CustomerManagement extends JPanel {
    private JFrame parentFrame;
    private JTable table;
    private DefaultTableModel model;
    private CustomerDAO customerDAO = new CustomerDAO();
    
    public CustomerManagement(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializeUI();
    }
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(UI.BACKGROUND_COLOR);
        add(UI.createTitleLabel("Quản lý khách hàng"), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    private JScrollPane createTablePanel() {
        String[] columns = {"Mã KH", "Tên khách", "Số điện thoại", "CCCD"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        refreshTable();
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(UI.PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        return new JScrollPane(table);
    }
    public void refreshTable() {
        model.setRowCount(0);
        List<Customer> list = customerDAO.getAllCustomers();
        for (Customer c : list) {
            model.addRow(new Object[]{
                c.getCustomerId(),
                c.getCustomerName(),
                c.getPhoneNumber(),
                c.getIdentityCard()
            });
        }
    }
    private JPanel createButtonPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl.setBackground(UI.BACKGROUND_COLOR);
        JButton addBtn = UI.createStyledButton("➕ Thêm", UI.SUCCESS_COLOR);
        JButton editBtn = UI.createStyledButton("✏️ Sửa", UI.WARNING_COLOR);
        JButton delBtn = UI.createStyledButton("❌ Xóa", UI.DANGER_COLOR);
        addBtn.addActionListener(e -> showAddDialog());
        editBtn.addActionListener(e -> showEditDialog());
        delBtn.addActionListener(e -> deleteCustomer());
        pnl.add(addBtn);
        pnl.add(editBtn);
        pnl.add(delBtn);
        return pnl;
    }
    private void showAddDialog() {
        CustomerDialog dialog = new CustomerDialog(parentFrame, null);
        dialog.setVisible(true);
        refreshTable();
    }
    private void showEditDialog() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        Customer c = new Customer(
                model.getValueAt(r, 0).toString(),
                model.getValueAt(r, 1).toString(),
                model.getValueAt(r, 2).toString(),
                model.getValueAt(r, 3).toString()
        );
        CustomerDialog dialog = new CustomerDialog(parentFrame, c);
        dialog.setVisible(true);
        refreshTable();
    }

    private void deleteCustomer() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(parentFrame, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        String id = model.getValueAt(r, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(parentFrame,
                "Bạn có chắc chắn muốn xóa KH này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            customerDAO.deleteCustomer(id);
            JOptionPane.showMessageDialog(parentFrame, "Xóa thành công!");
            refreshTable();
        }
    }
    public class CustomerDialog extends JDialog {
        private Customer customer;
        private JTextField idField, nameField, phoneField, cccdField;
        public CustomerDialog(JFrame parent, Customer customer) {
            super(parent, customer == null ? "Thêm khách hàng" : "Sửa khách hàng", true);
            this.customer = customer;
            initializeUI();
            if (customer != null) loadCustomerData();
        }
        private void initializeUI() {
            setSize(450, 330);
            setLocationRelativeTo(parentFrame);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            idField = new JTextField(20);
            nameField = new JTextField(20);
            phoneField = new JTextField(20);
            cccdField = new JTextField(20);
            if (customer == null) {
            idField.setText(generateId());
                idField.setEditable(true);
                
            } else {
            idField.setEditable(false);
            }
      int row = 0;
            gbc.gridx=0; gbc.gridy=row; panel.add(new JLabel("Mã KH:"), gbc);
            gbc.gridx=1; panel.add(idField, gbc);
            gbc.gridx=0; gbc.gridy=++row; panel.add(new JLabel("Tên KH:"), gbc);
            gbc.gridx=1; panel.add(nameField, gbc);
            gbc.gridx=0; gbc.gridy=++row; panel.add(new JLabel("Số điện thoại:"), gbc);
            gbc.gridx=1; panel.add(phoneField, gbc);
          gbc.gridx=0; gbc.gridy=++row; panel.add(new JLabel("CCCD:"), gbc);
            gbc.gridx=1; panel.add(cccdField, gbc);
            gbc.gridx=0; gbc.gridy=++row; gbc.gridwidth=2;
            gbc.insets = new Insets(20, 0, 0, 0);
            panel.add(createButtonPanelDialog(), gbc);
            add(panel);
        }
        private JPanel createButtonPanelDialog() {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            p.setBackground(Color.WHITE);
            JButton saveBtn = UI.createStyledButton("Lưu", UI.SUCCESS_COLOR);
            JButton cancelBtn = UI.createStyledButton("Hủy", UI.GREY_COLOR);
            saveBtn.addActionListener(e -> saveCustomer());
            cancelBtn.addActionListener(e -> dispose());
            p.add(saveBtn);
            p.add(cancelBtn);
            return p;
        }
        private void loadCustomerData() {
            idField.setText(customer.getCustomerId());
            nameField.setText(customer.getCustomerName());
            phoneField.setText(customer.getPhoneNumber());
            cccdField.setText(customer.getIdentityCard());
        }
        private void saveCustomer() {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String cccd = cccdField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || cccd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy nhập đầy đủ thông tin!");
                return;
            }
            Customer c = new Customer(id, name, phone, cccd);
            boolean success = (customer == null)
                    ? customerDAO.addCustomer(c)
                    : customerDAO.updateCustomer(c);
            if (success)
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
            else
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu!");
            dispose();
        }
        private String generateId() {
            return "CUS" + (System.currentTimeMillis() % 100000);
        }
    }
}
