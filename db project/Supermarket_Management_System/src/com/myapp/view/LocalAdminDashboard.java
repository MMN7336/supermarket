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

public class LocalAdminDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentArea;
    
    private DefaultTableModel cashierModel, salesModel;
    private JTextField txtCashierUser, txtCashierPass;
    private JLabel lblTotalRevenue;
    private int selectedCashierId = -1;

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TEXT_LIGHT = new Color(241, 245, 249);
    private final Color ACCENT_GREEN = new Color(16, 185, 129);
    private final Color ACCENT_BLUE = new Color(37, 99, 235);
    private final Color INACTIVE_TAB = new Color(71, 85, 105);
    private final Color INPUT_BG = new Color(51, 65, 85);

    public LocalAdminDashboard() {
        if (!"LOCAL_ADMIN".equals(UserSession.getRole())) {
            JOptionPane.showMessageDialog(null, "Access Denied: Local Admins Only!");
            this.dispose();
            return;
        }

        setTitle("Branch Manager Center - " + UserSession.getCurrentBranch());
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Frame level Container Setup
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel("Manager Dashboard (" + UserSession.getCurrentBranch() + ")");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_LIGHT);

        lblTotalRevenue = new JLabel("Total Branch Revenue: 0 MMK");
        lblTotalRevenue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalRevenue.setForeground(ACCENT_GREEN);

        JPanel titleGroup = new JPanel(new GridLayout(2, 1, 2, 2));
        titleGroup.setOpaque(false);
        titleGroup.add(lblTitle);
        titleGroup.add(lblTotalRevenue);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnGroup.setOpaque(false);

        JButton btnLogout = createStyledButton("Logout", new Color(239, 68, 68));
        btnLogout.addActionListener(e -> {
            UserSession.logout();
            new LoginPage().setVisible(true);
            this.dispose();
        });

        btnGroup.add(btnLogout);

        headerPanel.add(titleGroup, BorderLayout.WEST);
        headerPanel.add(btnGroup, BorderLayout.EAST);

        // 2. Tabs Switcher Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navPanel.setOpaque(false);

        JButton btnTabSales = createStyledButton("Sales History", ACCENT_BLUE);
        JButton btnTabCashiers = createStyledButton("Manage Cashiers", INACTIVE_TAB);
        JButton btnTabStock = createStyledButton("Manage Branch Stock", INACTIVE_TAB);

        btnTabSales.addActionListener(e -> {
            cardLayout.show(contentArea, "SALES");
            btnTabSales.setBackground(ACCENT_BLUE);
            btnTabCashiers.setBackground(INACTIVE_TAB);
            btnTabStock.setBackground(INACTIVE_TAB);
        });

        btnTabCashiers.addActionListener(e -> {
            cardLayout.show(contentArea, "CASHIER");
            btnTabCashiers.setBackground(ACCENT_BLUE);
            btnTabSales.setBackground(INACTIVE_TAB);
            btnTabStock.setBackground(INACTIVE_TAB);
        });

        btnTabStock.addActionListener(e -> {
            cardLayout.show(contentArea, "STOCK");
            btnTabStock.setBackground(ACCENT_BLUE);
            btnTabSales.setBackground(INACTIVE_TAB);
            btnTabCashiers.setBackground(INACTIVE_TAB);
        });

        navPanel.add(btnTabSales);
        navPanel.add(btnTabCashiers);
        navPanel.add(btnTabStock);

        // 3. Card Panel Container
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setOpaque(false);

        // Tab 1: Sales History Panel
        salesModel = new DefaultTableModel(new String[]{"Sale ID", "Cashier Name", "Total Amount", "Date & Time"}, 0);
        JTable tableSales = new JTable(salesModel);
        styleTable(tableSales);
        contentArea.add(createScrollPane(tableSales), "SALES");

        // Tab 2: Manage Cashiers Panel
        contentArea.add(createCashierManagementPanel(), "CASHIER");

        // Tab 3: Manage Branch Stock Panel (Margin ညှိရန် Outer Container ဖြုတ်ထားပါသည်)
        ManageProductsPage productsPage = new ManageProductsPage();
        Container stockContainer = productsPage.getContentPane();
        if (stockContainer instanceof JPanel) {
            ((JPanel) stockContainer).setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
        contentArea.add(stockContainer, "STOCK");

        // Layout Assembly
        JPanel bodyContainer = new JPanel(new BorderLayout(0, 10));
        bodyContainer.setOpaque(false);
        bodyContainer.add(navPanel, BorderLayout.NORTH);
        bodyContainer.add(contentArea, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(bodyContainer, BorderLayout.CENTER);

        add(mainPanel);
        loadAllData();
    }

    private JPanel createCashierManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);

        // Form (Left)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setPreferredSize(new Dimension(280, 0));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel("Cashier Account Form");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(TEXT_LIGHT);
        gbc.gridy = 0; formPanel.add(lblTitle, gbc);

        gbc.gridy = 1; formPanel.add(createFormLabel("Username:"), gbc);
        txtCashierUser = createTextField();
        gbc.gridy = 2; formPanel.add(txtCashierUser, gbc);

        gbc.gridy = 3; formPanel.add(createFormLabel("Password:"), gbc);
        txtCashierPass = createTextField();
        gbc.gridy = 4; formPanel.add(txtCashierPass, gbc);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        btnPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("Add", ACCENT_GREEN);
        btnAdd.addActionListener(e -> addCashier());

        JButton btnUpdate = createStyledButton("Edit", ACCENT_BLUE);
        btnUpdate.addActionListener(e -> updateCashier());

        JButton btnDelete = createStyledButton("Delete", new Color(239, 68, 68));
        btnDelete.addActionListener(e -> deleteCashier());

        JButton btnClear = createStyledButton("Clear", new Color(100, 116, 139));
        btnClear.addActionListener(e -> clearCashierFields());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridy = 5;
        gbc.insets = new Insets(15, 4, 4, 4);
        formPanel.add(btnPanel, gbc);

        // Table (Right)
        cashierModel = new DefaultTableModel(new String[]{"ID", "Cashier Username", "Branch"}, 0);
        JTable tableCashiers = new JTable(cashierModel);
        styleTable(tableCashiers);

        tableCashiers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableCashiers.getSelectedRow();
                if (row != -1) {
                    selectedCashierId = Integer.parseInt(cashierModel.getValueAt(row, 0).toString());
                    txtCashierUser.setText(cashierModel.getValueAt(row, 1).toString());
                }
            }
        });

        panel.add(formPanel, BorderLayout.WEST);
        panel.add(createScrollPane(tableCashiers), BorderLayout.CENTER);

        return panel;
    }

    private JLabel createFormLabel(String text) {
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
        tf.setPreferredSize(new Dimension(0, 32));
        tf.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        return tf;
    }

    private JScrollPane createScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        return scrollPane;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_LIGHT);
        table.setGridColor(new Color(51, 65, 85));
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 38));

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

    private void loadAllData() {
        String currentBranch = UserSession.getCurrentBranch();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, username, branch_id FROM users WHERE role = 'CASHIER' AND branch_id = ?")) {
            stmt.setString(1, currentBranch);
            ResultSet rs = stmt.executeQuery();
            cashierModel.setRowCount(0);
            while (rs.next()) {
                cashierModel.addRow(new Object[]{rs.getInt("id"), rs.getString("username"), rs.getString("branch_id")});
            }
        } catch (Exception ex) {
            System.out.println("Cashier Load Error: " + ex.getMessage());
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, cashier_name, total_amount, sale_date FROM sales WHERE branch_id = ? ORDER BY sale_date DESC")) {
            stmt.setString(1, currentBranch);
            ResultSet rs = stmt.executeQuery();
            salesModel.setRowCount(0);
            double totalRev = 0;
            while (rs.next()) {
                double amt = rs.getDouble("total_amount");
                totalRev += amt;
                salesModel.addRow(new Object[]{rs.getInt("id"), rs.getString("cashier_name"), amt + " MMK", rs.getTimestamp("sale_date")});
            }
            lblTotalRevenue.setText("Total Branch Revenue: " + String.format("%.2f", totalRev) + " MMK");
        } catch (Exception ex) {
            System.out.println("Sales Load Error: " + ex.getMessage());
        }
    }

    private void addCashier() {
        String user = txtCashierUser.getText().trim();
        String pass = txtCashierPass.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO users (username, password, role, branch_id) VALUES (?, ?, 'CASHIER', ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user);
            stmt.setString(2, pass);
            stmt.setString(3, UserSession.getCurrentBranch());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cashier Created Successfully!");
            clearCashierFields();
            loadAllData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateCashier() {
        if (selectedCashierId == -1) {
            JOptionPane.showMessageDialog(this, "Select a cashier from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "UPDATE users SET username=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, txtCashierUser.getText().trim());
            stmt.setInt(2, selectedCashierId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cashier Updated!");
            clearCashierFields();
            loadAllData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteCashier() {
        if (selectedCashierId == -1) {
            JOptionPane.showMessageDialog(this, "Select a cashier to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this cashier account?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
                stmt.setInt(1, selectedCashierId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Cashier Deleted!");
                clearCashierFields();
                loadAllData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void clearCashierFields() {
        selectedCashierId = -1;
        txtCashierUser.setText("");
        txtCashierPass.setText("");
    }
}