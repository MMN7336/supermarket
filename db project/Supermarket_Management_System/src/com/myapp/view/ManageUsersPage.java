package com.myapp.view;

import com.myapp.db.DBConnection;
import com.myapp.session.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageUsersPage extends JDialog {

    private JTextField txtUsername, txtPassword;
    private JComboBox<String> cbRole, cbBranch;
    private JTable tableUsers;
    private DefaultTableModel tableModel;
    private int selectedUserId = -1;

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TEXT_LIGHT = new Color(241, 245, 249);
    private final Color TEXT_MUTED = new Color(148, 163, 184);
    private final Color INPUT_BG = new Color(51, 65, 85);
 // ဒီ 3 လိုင်းကို ManageUsersPage.java ထဲ ထပ်တိုးပေးလိုက်ပါ
    public ManageUsersPage(Window owner) {
        this(); // မူလ ရှိပြီးသား ManageUsersPage() Constructor ကို ပြန်ခေါ်ပေးလိုက်ခြင်း
    }
    public ManageUsersPage() {
    	setModal(true);
    	UIManager.put("ComboBox.disabledForeground", Color.DARK_GRAY);
        UIManager.put("ComboBox.disabledBackground", new Color(226, 232, 240));
        if (!"GLOBAL_ADMIN".equals(UserSession.getRole())) {
            JOptionPane.showMessageDialog(null, "Access Denied: Global Admins Only!");
            this.dispose();
            return;
        }

        setTitle("HQ System - Employee & Staff Management");
        setSize(950, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Panel & Back Button

        // Left Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setPreferredSize(new Dimension(300, 0));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel(" Staff Account Form");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_LIGHT);
        gbc.gridy = 0;
        formPanel.add(lblTitle, gbc);

        // Inputs
        gbc.gridy = 1; formPanel.add(createLabel("Username:"), gbc);
        txtUsername = createTextField();
        gbc.gridy = 2; formPanel.add(txtUsername, gbc);

        gbc.gridy = 3; formPanel.add(createLabel("Password:"), gbc);
        txtPassword = createTextField();
        gbc.gridy = 4; formPanel.add(txtPassword, gbc);

        gbc.gridy = 5; formPanel.add(createLabel("Role:"), gbc);
        cbRole = new JComboBox<>(new String[]{"LOCAL_ADMIN", "CASHIER"});
        styleComboBox(cbRole);
        gbc.gridy = 6; formPanel.add(cbRole, gbc);

        gbc.gridy = 7; formPanel.add(createLabel("Assigned Branch:"), gbc);
        cbBranch = new JComboBox<>(new String[]{"Yangon_Branch", "Mandalay_Branch"});
    
        styleComboBox(cbBranch);
        gbc.gridy = 8; formPanel.add(cbBranch, gbc);

        // Action Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);

        JButton btnAdd = new JButton("Create");
        styleButton(btnAdd, new Color(16, 185, 129));
        btnAdd.addActionListener(e -> addUser());

        JButton btnUpdate = new JButton("Update");
        styleButton(btnUpdate, new Color(37, 99, 235));
        btnUpdate.addActionListener(e -> updateUser());

        JButton btnDelete = new JButton("Delete");
        styleButton(btnDelete, new Color(239, 68, 68));
        btnDelete.addActionListener(e -> deleteUser());

        JButton btnClear = new JButton("Clear");
        styleButton(btnClear, new Color(100, 116, 139));
        btnClear.addActionListener(e -> clearFields());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridy = 9;
        gbc.insets = new Insets(15, 4, 4, 4);
        formPanel.add(btnPanel, gbc);

        // Right Table Panel
        String[] columns = {"ID", "Username", "Role", "Branch"};
        tableModel = new DefaultTableModel(columns, 0);
        tableUsers = new JTable(tableModel);
        styleTable(tableUsers);

        tableUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableUsers.getSelectedRow();
                if (row != -1) {
                    selectedUserId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtUsername.setText(tableModel.getValueAt(row, 1).toString());
                    cbRole.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    cbBranch.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableUsers);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        loadUsers();
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_LIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setBackground(INPUT_BG);
        tf.setForeground(TEXT_LIGHT);
        tf.setCaretColor(TEXT_LIGHT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(0, 30));
        tf.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        return tf;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setBackground(INPUT_BG);
        combo.setForeground(Color.BLACK);
        
        // Disabled ဖြစ်ချိန်မှာ စာသားကို အမည်းရောင်ဖြင့် ထင်ထင်ရှားရှား ပေါ်စေရန်
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setForeground(Color.BLACK); // စာသားအရောင်ကို အမည်းရောင် ပြောင်းမည်
                return label;
            }
        });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleTable(JTable table) {
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_LIGHT);
        table.setGridColor(new Color(51, 65, 85));
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(51, 65, 85));
        table.setSelectionForeground(TEXT_LIGHT);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 35));

        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(30, 41, 59));
                setForeground(Color.WHITE);
                setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(51, 65, 85)));
                return this;
            }
        });
    }

    private void loadUsers() {
        String query = "SELECT id, username, role, branch_id FROM users WHERE role != 'GLOBAL_ADMIN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("branch_id")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void addUser() {
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();
        String role = cbRole.getSelectedItem().toString();
        String branch = cbBranch.getSelectedItem().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO users (username, password, role, branch_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user);
            stmt.setString(2, pass);
            stmt.setString(3, role);
            stmt.setString(4, branch);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Staff Account Created!");
            clearFields();
            loadUsers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateUser() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "UPDATE users SET username=?, role=?, branch_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, txtUsername.getText().trim());
            stmt.setString(2, cbRole.getSelectedItem().toString());
            stmt.setString(3, cbBranch.getSelectedItem().toString());
            stmt.setInt(4, selectedUserId);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account Updated!");
            clearFields();
            loadUsers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteUser() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this staff account?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM users WHERE id=?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedUserId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Account Deleted!");
                clearFields();
                loadUsers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void clearFields() {
        selectedUserId = -1;
        txtUsername.setText("");
        txtPassword.setText("");
        tableUsers.clearSelection();
    }
}