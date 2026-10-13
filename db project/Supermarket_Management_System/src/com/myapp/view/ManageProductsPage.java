package com.myapp.view;

import com.myapp.db.DBConnection;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableCellRenderer;
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

public class ManageProductsPage extends JDialog {

    private JTextField txtName, txtCategory, txtPrice, txtQty;
    private JComboBox<String> cbBranch;
    private JTable tableProducts;
    private DefaultTableModel tableModel;
    private int selectedProductId = -1; // ရွေးချယ်ထားသော Product ID သိမ်းရန်

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TEXT_LIGHT = new Color(241, 245, 249);
    private final Color TEXT_MUTED = new Color(148, 163, 184);
    private final Color INPUT_BG = new Color(51, 65, 85);
 // ManageProductsPage.java ရဲ့ အပေါ်ဆုံး Class Variables များတွင် ထည့်ရန်
    private JLabel lblFormTitle;
    private JButton btnAdd, btnUpdate, btnDelete;
    private boolean isReadOnly = false;
    public ManageProductsPage() {
        this(false);
    }

    public ManageProductsPage(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        setModal(true);
        setTitle("Inventory Management - Products");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

     // Header Panel & Back Button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        if (!isReadOnly) {
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(CARD_BG);
            formPanel.setPreferredSize(new Dimension(310, 0));
            formPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                    new EmptyBorder(15, 15, 15, 15)
            ));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(3, 3, 3, 3);
            gbc.weightx = 1.0;
            gbc.gridx = 0;

            JLabel lblFormTitle = new JLabel("Product Details");
            lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblFormTitle.setForeground(TEXT_LIGHT);
            gbc.gridy = 0;
            formPanel.add(lblFormTitle, gbc);

            gbc.gridy = 1;
            formPanel.add(createLabel("Product Name:"), gbc);
            txtName = createTextField();
            gbc.gridy = 2;
            formPanel.add(txtName, gbc);

            gbc.gridy = 3;
            formPanel.add(createLabel("Category:"), gbc);
            txtCategory = createTextField();
            gbc.gridy = 4;
            formPanel.add(txtCategory, gbc);

            gbc.gridy = 5;
            formPanel.add(createLabel("Price (MMK):"), gbc);
            txtPrice = createTextField();
            gbc.gridy = 6;
            formPanel.add(txtPrice, gbc);

            gbc.gridy = 7;
            formPanel.add(createLabel("Stock Quantity:"), gbc);
            txtQty = createTextField();
            gbc.gridy = 8;
            formPanel.add(txtQty, gbc);

            gbc.gridy = 9;
            formPanel.add(createLabel("Branch:"), gbc);
            cbBranch = new JComboBox<>(new String[]{"Yangon_Branch", "Mandalay_Branch"});
            cbBranch.setBackground(INPUT_BG);
            cbBranch.setForeground(TEXT_LIGHT);
            cbBranch.setPreferredSize(new Dimension(0, 32));
            
            if ("LOCAL_ADMIN".equals(UserSession.getRole())) {
                cbBranch.setSelectedItem(UserSession.getCurrentBranch());
                cbBranch.setEnabled(false);
            }
            gbc.gridy = 10;
            formPanel.add(cbBranch, gbc);

            // Buttons Panel (Add, Update, Delete)
            JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
            btnPanel.setOpaque(false);

            JButton btnAdd = new JButton("Add");
            styleButton(btnAdd, new Color(16, 185, 129));
            btnAdd.addActionListener(e -> addProduct());

            JButton btnUpdate = new JButton("Update");
            styleButton(btnUpdate, new Color(37, 99, 235));
            btnUpdate.addActionListener(e -> updateProduct());

            JButton btnDelete = new JButton("Delete");
            styleButton(btnDelete, new Color(239, 68, 68));
            btnDelete.addActionListener(e -> deleteProduct());

            JButton btnClear = new JButton("Clear");
            styleButton(btnClear, new Color(100, 116, 139));
            btnClear.addActionListener(e -> clearFields());

            btnPanel.add(btnAdd);
            btnPanel.add(btnUpdate);
            btnPanel.add(btnDelete);
            btnPanel.add(btnClear);

            gbc.gridy = 11;
            gbc.insets = new Insets(12, 3, 3, 3);
            formPanel.add(btnPanel, gbc);

            // Left Side Form Panel ကို mainPanel ထဲထည့်ခြင်း
            mainPanel.add(formPanel, BorderLayout.WEST);
        }

        // Table Panel (Right Side) - ReadOnly ဖြစ်လည်း အလုပ်လုပ်ရန် အပြင်သို့ ထုတ်ပေးထားပါသည်
        String[] columns = {"ID", "Product Name", "Category", "Price", "Stock", "Branch"};
        tableModel = new DefaultTableModel(columns, 0);
        tableProducts = new JTable(tableModel);
        styleTable(tableProducts);

        // Table Row Click Event
        tableProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableProducts.getSelectedRow();
                if (!isReadOnly && row != -1) {
                    selectedProductId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtCategory.setText(tableModel.getValueAt(row, 2).toString());
                    txtPrice.setText(tableModel.getValueAt(row, 3).toString());
                    txtQty.setText(tableModel.getValueAt(row, 4).toString());
                    cbBranch.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableProducts);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        
        // Table ကို Center မှာ အမြဲ ပေါ်စေမည်
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        loadProducts();
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

        // Default Look-and-Feel ကို ကျော်ပြီး Header Background နဲ့ စာသားအရောင်ကို တိုက်ရိုက် ဆွဲပေးသည့် Renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(30, 41, 59)); // Header Background
                setForeground(Color.WHITE);            // စာသားအရောင်
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(51, 65, 85)));
                return this;
            }
        };

        header.setDefaultRenderer(headerRenderer);
    }
    private void loadProducts() {
        String query;
        if ("GLOBAL_ADMIN".equals(UserSession.getRole())) {
            query = "SELECT id, product_name, category, price, stock_qty, branch_id FROM products";
        } else {
            query = "SELECT id, product_name, category, price, stock_qty, branch_id FROM products WHERE branch_id = '" + UserSession.getCurrentBranch() + "'";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock_qty"),
                        rs.getString("branch_id")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void addProduct() {
        String name = txtName.getText().trim();
        String cat = txtCategory.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String qtyStr = txtQty.getText().trim();
        String branch = cbBranch.getSelectedItem().toString();

        if (name.isEmpty() || cat.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int qty = Integer.parseInt(qtyStr);

            String query = "INSERT INTO products (product_name, category, price, stock_qty, branch_id) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, name);
                stmt.setString(2, cat);
                stmt.setDouble(3, price);
                stmt.setInt(4, qty);
                stmt.setString(5, branch);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product Added Successfully!");
                clearFields();
                loadProducts();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and Stock Quantity must be numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateProduct() {
        if (selectedProductId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String query = "UPDATE products SET product_name=?, category=?, price=?, stock_qty=?, branch_id=? WHERE id=?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, txtName.getText().trim());
                stmt.setString(2, txtCategory.getText().trim());
                stmt.setDouble(3, Double.parseDouble(txtPrice.getText().trim()));
                stmt.setInt(4, Integer.parseInt(txtQty.getText().trim()));
                stmt.setString(5, cbBranch.getSelectedItem().toString());
                stmt.setInt(6, selectedProductId);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product Updated Successfully!");
                clearFields();
                loadProducts();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + ex.getMessage());
        }
    }

    private void deleteProduct() {
        if (selectedProductId == -1) {
            JOptionPane.showMessageDialog(this, "Please click on a product from the table to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM products WHERE id=?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedProductId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Product Deleted Successfully!");
                clearFields();
                loadProducts();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage());
            }
        }
    }

    private void clearFields() {
        selectedProductId = -1;
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtQty.setText("");
        tableProducts.clearSelection();
    }
}