package com.myapp.view;

import com.myapp.db.DBConnection;
import com.myapp.session.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SystemSettingsPage extends JPanel {

    private JTextField txtTaxRate, txtCurrency;

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TEXT_LIGHT = new Color(241, 245, 249);
    private final Color TEXT_MUTED = new Color(148, 163, 184);
    private final Color INPUT_BG = new Color(51, 65, 85);

    public SystemSettingsPage() {
        if (!"GLOBAL_ADMIN".equals(UserSession.getRole())) {
            JOptionPane.showMessageDialog(null, "Access Denied: Global Admins Only!");
            return;
        }

        // Form ကို Dashboard ၏ အလယ်ဗဟိုတွင် ထားရှိရန် GridBagLayout သုံးထားပါသည်
        setLayout(new GridBagLayout());
        setBackground(BG_DARK);

        // Center Card Panel ဖန်တီးခြင်း
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(25, 30, 25, 30)
        ));
        cardPanel.setPreferredSize(new Dimension(480, 360));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Title Section
        JLabel lblTitle = new JLabel("Global ERP Parameters");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_LIGHT);
        gbc.gridy = 0; 
        cardPanel.add(lblTitle, gbc);

        JLabel lblSubTitle = new JLabel("Configure global tax percentage and base system currency");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubTitle.setForeground(TEXT_MUTED);
        gbc.gridy = 1; 
        cardPanel.add(lblSubTitle, gbc);

        // Tax Rate Field
        gbc.gridy = 2; 
        gbc.insets = new Insets(12, 6, 4, 6);
        cardPanel.add(createLabel("Global Tax Rate (%) :"), gbc);
        
        txtTaxRate = createTextField("5.0");
        gbc.gridy = 3; 
        gbc.insets = new Insets(0, 6, 6, 6);
        cardPanel.add(txtTaxRate, gbc);

        // Currency Field
        gbc.gridy = 4; 
        gbc.insets = new Insets(8, 6, 4, 6);
        cardPanel.add(createLabel("Base Currency Symbol :"), gbc);
        
        txtCurrency = createTextField("MMK");
        gbc.gridy = 5; 
        gbc.insets = new Insets(0, 6, 6, 6);
        cardPanel.add(txtCurrency, gbc);

        // Save Button
        JButton btnSave = new JButton("Save Configurations");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(new Color(37, 99, 235));
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(0, 40));

        // မူလ Save Action & Database Query (မပြောင်းလဲဘဲ ထိန်းသိမ်းထားပါသည်)
        btnSave.addActionListener(e -> {
            String taxStr = txtTaxRate.getText().trim();
            String currStr = txtCurrency.getText().trim();
            
            String query = "UPDATE system_settings SET tax_rate = ?, currency_symbol = ? WHERE id = 1";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                
                stmt.setDouble(1, Double.parseDouble(taxStr));
                stmt.setString(2, currStr);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Global System Configurations Saved Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Save Error: " + ex.getMessage());
            }
        });

        gbc.gridy = 6;
        gbc.insets = new Insets(18, 6, 6, 6);
        cardPanel.add(btnSave, gbc);

        // Main Panel ထဲသို့ အလယ်တည့်တည့်ရောက်အောင် ထည့်သွင်းခြင်း
        add(cardPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_LIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    private JTextField createTextField(String initialValue) {
        JTextField tf = new JTextField(initialValue);
        tf.setBackground(INPUT_BG);
        tf.setForeground(TEXT_LIGHT);
        tf.setCaretColor(TEXT_LIGHT);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(0, 36));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return tf;
    }

    // Dashboard ထဲမှ .getContentPane() ခေါ်ယူမှုနှင့် မလွဲစေရန် Helper Method
    public Container getContentPane() {
        return this;
    }
}