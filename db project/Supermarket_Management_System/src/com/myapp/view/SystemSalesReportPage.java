package com.myapp.view;

import com.myapp.db.DBConnection;
import com.myapp.session.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SystemSalesReportPage extends JDialog {

    private DefaultTableModel salesModel;
    private JLabel lblTotalRevenue, lblTotalTransactions;

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TEXT_LIGHT = new Color(241, 245, 249);
    private final Color TEXT_MUTED = new Color(148, 163, 184);
    private final Color ACCENT_GREEN = new Color(16, 185, 129);
  public SystemSalesReportPage(Window owner) {
    this(); // မူလ ရှိပြီးသား ManageUsersPage() Constructor ကို ပြန်ခေါ်ပေးလိုက်ခြင်း
}
    public SystemSalesReportPage() {
        setModal(true);
        
        setTitle("HQ Global Analytics - System Sales Reports");
        setSize(950, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

     // Header Panel & Back Button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Header Panel နှင့် Summary Panel တို့ကို စုစည်းပေးမည့် topContainer
        JPanel topContainer = new JPanel(new BorderLayout(0, 15));
        topContainer.setOpaque(false);
        topContainer.add(headerPanel, BorderLayout.NORTH);

        // Header Panel with Stats Summary
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        summaryPanel.setOpaque(false);

        lblTotalRevenue = createStatCard("Total System Revenue", "0.00 MMK", ACCENT_GREEN);
        lblTotalTransactions = createStatCard("Total Transactions", "0 Sales", new Color(37, 99, 235));

        summaryPanel.add(lblTotalRevenue.getParent());
        summaryPanel.add(lblTotalTransactions.getParent());

        // Sales Table
        salesModel = new DefaultTableModel(new String[]{"Sale ID", "Branch Name", "Cashier Name", "Total Amount", "Date & Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableSales = new JTable(salesModel);
        styleTable(tableSales);

        JScrollPane scrollPane = new JScrollPane(tableSales);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));

        topContainer.add(summaryPanel, BorderLayout.CENTER);
        mainPanel.add(topContainer, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        loadSystemSalesData();
    }

    private JLabel createStatCard(String title, String defaultValue, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1, 4, 4));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(TEXT_MUTED);

        JLabel lblValue = new JLabel(defaultValue);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setForeground(color);

        card.add(lblTitle);
        card.add(lblValue);

        return lblValue;
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
    private void loadSystemSalesData() {
        String query = "SELECT id, branch_id, cashier_name, total_amount, sale_date FROM sales ORDER BY sale_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            salesModel.setRowCount(0);
            double grandTotal = 0;
            int totalSalesCount = 0;

            while (rs.next()) {
                double amount = rs.getDouble("total_amount");
                grandTotal += amount;
                totalSalesCount++;

                salesModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("branch_id"),
                        rs.getString("cashier_name"),
                        String.format("%.2f MMK", amount),
                        rs.getTimestamp("sale_date")
                });
            }

            lblTotalRevenue.setText(String.format("%.2f MMK", grandTotal));
            lblTotalTransactions.setText(totalSalesCount + " Sales");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading sales analytics: " + ex.getMessage());
        }
    }
}