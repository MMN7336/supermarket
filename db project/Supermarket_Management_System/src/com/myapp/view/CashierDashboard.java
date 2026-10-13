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
import java.sql.Statement;

public class CashierDashboard extends JFrame {

    private DefaultTableModel cartModel;
    private JTextField txtSearch, txtPaidAmount;
    private JLabel lblSubtotal, lblTax, lblTotal, lblChange;
    private JPanel productGridPanel;

    private double subtotalAmt = 0.0;

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TEXT_LIGHT = new Color(241, 245, 249);
    private final Color TEXT_MUTED = new Color(148, 163, 184);
    private final Color ACCENT_GREEN = new Color(16, 185, 129);
    private final Color ACCENT_BLUE = new Color(37, 99, 235);
    private final Color INPUT_BG = new Color(51, 65, 85);
    private double taxRate = 0.05; // Default 5%
    private JLabel lblTaxTitle;

    public CashierDashboard() {
        if (!"CASHIER".equals(UserSession.getRole())) {
            JOptionPane.showMessageDialog(null, "Access Denied: Cashiers Only!");
            this.dispose();
            return;
        }

        // 1. UI မဆွဲမီ Database မှ Tax Rate ကို အရင် Load လုပ်ပါ
        loadSystemSettings();

        setTitle("POS Counter - " + UserSession.getCurrentBranch() + " Branch");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header Bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(12, 18, 12, 18)
        ));

        JLabel lblTitle = new JLabel("🛒 POS Terminal | " + UserSession.getCurrentBranch());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_LIGHT);

        JLabel lblCashierInfo = new JLabel("Cashier: " + UserSession.getUsername());
        lblCashierInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCashierInfo.setForeground(TEXT_MUTED);

        JButton btnLogout = createStyledButton("Logout", new Color(239, 68, 68));
        btnLogout.addActionListener(e -> {
            UserSession.logout();
            new LoginPage().setVisible(true);
            this.dispose();
        });

        JPanel headerLeft = new JPanel(new GridLayout(2, 1, 2, 2));
        headerLeft.setOpaque(false);
        headerLeft.add(lblTitle);
        headerLeft.add(lblCashierInfo);

        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);

        // Left Section: Product Selector Catalog
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setOpaque(false);

        // Search Bar
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setBackground(INPUT_BG);
        txtSearch.setForeground(TEXT_LIGHT);
        txtSearch.setCaretColor(TEXT_LIGHT);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(0, 38));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                new EmptyBorder(4, 10, 4, 10)
        ));

        txtSearch.addActionListener(e -> loadProducts(txtSearch.getText().trim()));

        JButton btnSearch = createStyledButton("Search", ACCENT_BLUE);
        btnSearch.addActionListener(e -> loadProducts(txtSearch.getText().trim()));

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        // Grid Product Catalog Container
        productGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productGridPanel.setOpaque(false);

        JScrollPane scrollCatalog = new JScrollPane(productGridPanel);
        scrollCatalog.getViewport().setBackground(BG_DARK);
        scrollCatalog.setBorder(null);

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(scrollCatalog, BorderLayout.CENTER);

        // Right Section: Cart & Billing Area
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setPreferredSize(new Dimension(420, 0));
        rightPanel.setBackground(CARD_BG);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        // Cart Table
        cartModel = new DefaultTableModel(new String[]{"ID", "Item Name", "Qty", "Price", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tableCart = new JTable(cartModel);
        styleTable(tableCart);
        JScrollPane scrollCart = new JScrollPane(tableCart);
        scrollCart.getViewport().setBackground(CARD_BG);
        scrollCart.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));

        // Bill Summary Calculation Box
        JPanel billPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        billPanel.setOpaque(false);
        billPanel.setBorder(new EmptyBorder(10, 5, 10, 5));

        lblSubtotal = createBillLabel("0.00 MMK", Color.WHITE);
        lblTax = createBillLabel("0.00 MMK", Color.WHITE);
        lblTotal = createBillLabel("0.00 MMK", ACCENT_GREEN);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));

        txtPaidAmount = new JTextField();
        txtPaidAmount.setBackground(INPUT_BG);
        txtPaidAmount.setForeground(TEXT_LIGHT);
        txtPaidAmount.setCaretColor(TEXT_LIGHT);
        txtPaidAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtPaidAmount.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

        lblChange = createBillLabel("0.00 MMK", new Color(245, 158, 11));

        lblTaxTitle = createLabel(String.format("Tax (%.1f%%):", taxRate * 100));

        billPanel.add(createLabel("Subtotal:"));
        billPanel.add(lblSubtotal);
        billPanel.add(lblTaxTitle);
        billPanel.add(lblTax);
        billPanel.add(createLabel("Grand Total:"));
        billPanel.add(lblTotal);
        billPanel.add(createLabel("Paid Amount:"));
        billPanel.add(txtPaidAmount);
        billPanel.add(createLabel("Change / ပြန်အမ်းငွေ:"));
        billPanel.add(lblChange);

        // Action Buttons
        JPanel cartBtnPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        cartBtnPanel.setOpaque(false);

        JButton btnClearCart = createStyledButton("Clear Cart", new Color(239, 68, 68));
        btnClearCart.addActionListener(e -> clearCart());

        JButton btnPay = createStyledButton("Pay & Print Receipt", ACCENT_GREEN);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPay.addActionListener(e -> processCheckout());

        cartBtnPanel.add(btnClearCart);
        cartBtnPanel.add(btnPay);

        JPanel rightBottomGroup = new JPanel(new BorderLayout(5, 5));
        rightBottomGroup.setOpaque(false);
        rightBottomGroup.add(billPanel, BorderLayout.CENTER);
        rightBottomGroup.add(cartBtnPanel, BorderLayout.SOUTH);

        rightPanel.add(scrollCart, BorderLayout.CENTER);
        rightPanel.add(rightBottomGroup, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel);

        // 2. Load Products Catalog
        loadProducts("");
    }

    private void loadSystemSettings() {
        String query = "SELECT tax_rate FROM system_settings WHERE id = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                double rateFromDB = rs.getDouble("tax_rate"); 
                taxRate = rateFromDB / 100.0;            
                
                if (lblTaxTitle != null) {
                    lblTaxTitle.setText(String.format("Tax (%.1f%%):", rateFromDB));
                }
            }
        } catch (Exception ex) {
            System.out.println("Settings Load Error: " + ex.getMessage());
        }
    }

    private void loadProducts(String keyword) {
        productGridPanel.removeAll();

        String sql = "SELECT * FROM products WHERE branch_id = ?";
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " AND (product_name LIKE ? OR id LIKE ?)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UserSession.getCurrentBranch());

            if (keyword != null && !keyword.trim().isEmpty()) {
                stmt.setString(2, "%" + keyword.trim() + "%");
                stmt.setString(3, "%" + keyword.trim() + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("product_name");
                
                String category = "General";
                try {
                    category = rs.getString("category");
                } catch (Exception e) {
                    category = "General";
                }

                double price = rs.getDouble("price");
                int stock = rs.getInt("stock_qty");

                productGridPanel.add(createProductCard(id, name, category, price, stock));
            }

            productGridPanel.revalidate();
            productGridPanel.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JPanel createProductCard(int id, String name, String category, double price, int stock) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(new Color(30, 41, 59));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
            new EmptyBorder(12, 12, 12, 12)
        ));

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setOpaque(false);

        JLabel lblCategory = new JLabel(" " + category.toUpperCase() + " ");
        lblCategory.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblCategory.setForeground(Color.WHITE);
        lblCategory.setOpaque(true);
        lblCategory.setBackground(getCategoryColor(category));
        topPanel.add(lblCategory, BorderLayout.NORTH);

        JLabel lblName = new JLabel("<html><b>" + name + "</b></html>");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblName.setForeground(Color.WHITE);
        topPanel.add(lblName, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        infoPanel.setOpaque(false);

        JLabel lblPrice = new JLabel(String.format("%.2f MMK", price));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPrice.setForeground(new Color(52, 211, 153));

        JLabel lblStock = new JLabel("Stock: " + stock + " pcs");
        lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStock.setForeground(stock < 10 ? new Color(248, 113, 113) : new Color(148, 163, 184));

        infoPanel.add(lblPrice);
        infoPanel.add(lblStock);

        JButton btnAdd = createStyledButton("+ Add to Cart", new Color(59, 130, 246));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setPreferredSize(new Dimension(0, 32));
        btnAdd.addActionListener(e -> addToCart(id, name, price, stock));

        card.add(topPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(btnAdd, BorderLayout.SOUTH);

        return card;
    }

    private Color getCategoryColor(String category) {
        if (category == null) return new Color(100, 116, 139);
        switch (category.toLowerCase()) {
            case "beverage": return new Color(239, 68, 68);
            case "household": return new Color(16, 185, 129);
            case "grocery": return new Color(245, 158, 11);
            case "noodle": return new Color(139, 92, 246);
            default: return new Color(100, 116, 139);
        }
    }

    private void addToCart(int pId, String pName, double price, int maxStock) {
        String input = JOptionPane.showInputDialog(
                this, 
                "'" + pName + "' အတွက် အရေအတွက် (Quantity) ရိုက်ထည့်ပါ:\n(လက်ကျန် Stock: " + maxStock + ")", 
                "Select Quantity", 
                JOptionPane.QUESTION_MESSAGE
        );

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        int inputQty;
        try {
            inputQty = Integer.parseInt(input.trim());
            if (inputQty <= 0) {
                JOptionPane.showMessageDialog(this, "ကျေးဇူးပြု၍ 1 သို့မဟုတ် 1 ထက်ကြီးသော အရေအတွက် ရိုက်ထည့်ပါ!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ဂဏန်း (Number) သာ ရိုက်ထည့်ပါ!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if (Integer.parseInt(cartModel.getValueAt(i, 0).toString()) == pId) {
                int currentQty = Integer.parseInt(cartModel.getValueAt(i, 2).toString());
                int newTotalQty = currentQty + inputQty;

                if (newTotalQty > maxStock) {
                    JOptionPane.showMessageDialog(this, "Stock မလုံလောက်ပါ။ (လက်ရှိ ရှိသမျှ: " + maxStock + ")", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                cartModel.setValueAt(newTotalQty, i, 2);
                cartModel.setValueAt(String.format("%.2f", newTotalQty * price), i, 4);
                updateTotals();
                return;
            }
        }

        if (inputQty > maxStock) {
            JOptionPane.showMessageDialog(this, "Stock မလုံလောက်ပါ။ (လက်ရှိ ရှိသမျှ: " + maxStock + ")", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cartModel.addRow(new Object[]{pId, pName, inputQty, price, String.format("%.2f", inputQty * price)});
        updateTotals();
    }

    private void updateTotals() {
        subtotalAmt = 0.0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            subtotalAmt += Double.parseDouble(cartModel.getValueAt(i, 4).toString());
        }

        double taxAmt = subtotalAmt * taxRate;
        double grandTotal = subtotalAmt + taxAmt;

        lblSubtotal.setText(String.format("%.2f MMK", subtotalAmt));
        lblTax.setText(String.format("%.2f MMK", taxAmt));
        lblTotal.setText(String.format("%.2f MMK", grandTotal));
    }

    private void processCheckout() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart ထဲတွင် ပစ္စည်းမရှိပါ။", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String paidStr = txtPaidAmount.getText().trim();
        if (paidStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ကျေးဇူးပြု၍ ပေးချေငွေ (Paid Amount) ရိုက်ထည့်ပါ။", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double paidAmt = Double.parseDouble(paidStr);
            double subtotal = 0.0;
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                subtotal += Double.parseDouble(cartModel.getValueAt(i, 4).toString());
            }
            double taxAmt = subtotal * taxRate;
            double grandTotal = subtotal + taxAmt;

            if (paidAmt < grandTotal) {
                JOptionPane.showMessageDialog(this, "ပေးချေငွေ မလောက်ပါခင်ဗျာ!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double changeAmt = paidAmt - grandTotal;
            int saleId = -1;

            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false); 

                // 1. Sales Table ထဲထည့်ခြင်း
                String insertSaleQuery = "INSERT INTO sales (cashier_name, branch_id, total_amount, sale_date, cashier_username) VALUES (?, ?, ?, NOW(), ?)";
                try (PreparedStatement stmtSale = conn.prepareStatement(insertSaleQuery, Statement.RETURN_GENERATED_KEYS)) {
                    stmtSale.setString(1, UserSession.getUsername());
                    stmtSale.setString(2, UserSession.getCurrentBranch());
                    stmtSale.setDouble(3, grandTotal);
                    stmtSale.setString(4, UserSession.getUsername());
                    stmtSale.executeUpdate();

                    ResultSet generatedKeys = stmtSale.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        saleId = generatedKeys.getInt(1);
                    }
                }
                System.out.println("DEBUG: Sale ID got = " + saleId);

                // 2. Payments Table ထဲထည့်ခြင်း (ဒီနေရာမှာ Error တက်မတက် စစ်ဖို့)
                String insertPaymentQuery = "INSERT INTO payments (sale_id, amount, payment_date) VALUES (?, ?, NOW())";
                try (PreparedStatement stmtPayment = conn.prepareStatement(insertPaymentQuery)) {
                    stmtPayment.setInt(1, saleId);
                    stmtPayment.setDouble(2, grandTotal);
                    stmtPayment.executeUpdate();
                    System.out.println("DEBUG: Payment inserted successfully!");
                } catch (Exception payEx) {
                    System.out.println("DEBUG ERROR in Payments: " + payEx.getMessage());
                    payEx.printStackTrace();
                    throw payEx; // Transaction ကို rollback ဖြစ်သွားစေရန်
                }

                // 3. Sale Products Table
                String insertSaleProdQuery = "INSERT INTO sale_products (sale_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmtSaleProd = conn.prepareStatement(insertSaleProdQuery)) {
                    for (int i = 0; i < cartModel.getRowCount(); i++) {
                        int productId = Integer.parseInt(cartModel.getValueAt(i, 0).toString());
                        int qty = Integer.parseInt(cartModel.getValueAt(i, 2).toString());
                        double price = Double.parseDouble(cartModel.getValueAt(i, 3).toString());
                        double lineTotal = Double.parseDouble(cartModel.getValueAt(i, 4).toString());

                        stmtSaleProd.setInt(1, saleId);
                        stmtSaleProd.setInt(2, productId);
                        stmtSaleProd.setInt(3, qty);
                        stmtSaleProd.setDouble(4, price);
                        stmtSaleProd.setDouble(5, lineTotal);
                        stmtSaleProd.addBatch();
                    }
                    stmtSaleProd.executeBatch();
                }

                // 4. Stock Update
                String updateQuery = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ?";
                try (PreparedStatement stmtUpdate = conn.prepareStatement(updateQuery)) {
                    for (int i = 0; i < cartModel.getRowCount(); i++) {
                        int productId = Integer.parseInt(cartModel.getValueAt(i, 0).toString());
                        int buyQty = Integer.parseInt(cartModel.getValueAt(i, 2).toString());

                        stmtUpdate.setInt(1, buyQty);
                        stmtUpdate.setInt(2, productId);
                        stmtUpdate.addBatch();
                    }
                    stmtUpdate.executeBatch();
                }

                conn.commit();
                JOptionPane.showMessageDialog(this, "Payment Successful & Saved!\nChange: " + String.format("%.2f MMK", changeAmt));
                clearCart();       
                loadProducts(""); 

            }

        } catch (Exception e) {
            System.out.println("DEBUG MAIN ERROR: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Transaction Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearCart() {
        cartModel.setRowCount(0);
        txtPaidAmount.setText("");
        lblChange.setText("0.00 MMK");
        updateTotals();
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_LIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    private JLabel createBillLabel(String text, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setForeground(color);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
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
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 32));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(51, 65, 85));
                setForeground(Color.WHITE);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(30, 41, 59)));
                return this;
            }
        };

        header.setDefaultRenderer(headerRenderer);
    }
}