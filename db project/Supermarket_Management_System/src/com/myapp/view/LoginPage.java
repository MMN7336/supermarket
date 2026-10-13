package com.myapp.view;

import com.myapp.db.DBConnection;
import com.myapp.session.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPage extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    // Theme Colors
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);    // Modern Royal Blue
    private final Color PRIMARY_HOVER = new Color(29, 78, 216);    // Darker Blue
    private final Color BG_DARK = new Color(15, 23, 42);           // Slate 900
    private final Color CARD_BG = new Color(30, 41, 59);           // Slate 800
    private final Color TEXT_LIGHT = new Color(241, 245, 249);    // Slate 100
    private final Color TEXT_MUTED = new Color(148, 163, 184);    // Slate 400
    private final Color INPUT_BG = new Color(51, 65, 85);          // Slate 700

    public LoginPage() {
        setTitle("Supermarket Management System - Login");
        setSize(450, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Container Background Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BG_DARK);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Center Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(CARD_BG);
        cardPanel.setPreferredSize(new Dimension(370, 480));
        cardPanel.setLayout(null);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // 1. App Logo / Icon Header
        JLabel lblLogo = new JLabel("🛒", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));
        lblLogo.setBounds(0, 25, 370, 50);
        cardPanel.add(lblLogo);

        // Title Label
        JLabel lblTitle = new JLabel("Supermarket System Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_LIGHT);
        lblTitle.setBounds(0, 80, 370, 30);
        cardPanel.add(lblTitle);

        JLabel lblSubtitle = new JLabel("Enter your credentials to access System", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(TEXT_MUTED);
        lblSubtitle.setBounds(0, 110, 370, 20);
        cardPanel.add(lblSubtitle);

        // 2. Username Input Field
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(TEXT_LIGHT);
        lblUser.setBounds(25, 145, 270, 20);
        cardPanel.add(lblUser);

        txtUsername = createStyledTextField();
        txtUsername.setBounds(25, 170, 320, 42);
        cardPanel.add(txtUsername);

        // 3. Password Input Field
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(TEXT_LIGHT);
        lblPass.setBounds(25, 225, 320, 20);
        cardPanel.add(lblPass);

        txtPassword = createStyledPasswordField();
        txtPassword.setBounds(25, 250, 320, 42);
        cardPanel.add(txtPassword);

        // 4. Styled Login Button
        btnLogin = new JButton("LOG IN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBounds(25, 325, 320, 45);

        // Button Hover Effect
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(PRIMARY_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(PRIMARY_COLOR);
            }
        });

        btnLogin.addActionListener(e -> handleLogin());
        cardPanel.add(btnLogin);

        // Footer Text
        JLabel lblFooter = new JLabel("Supermarket Multi-Branch ERP v1.0", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFooter.setForeground(TEXT_MUTED);
        lblFooter.setBounds(0, 400, 320, 20);
        cardPanel.add(lblFooter);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        styleInputComponent(tf);
        return tf;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField pf = new JPasswordField();
        styleInputComponent(pf);
        return pf;
    }

    private void styleInputComponent(JTextField field) {
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_LIGHT);
        field.setCaretColor(TEXT_LIGHT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showCustomMessage("Warning", "Please enter both Username and Password!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                String branchId = rs.getString("branch_id");

                UserSession.createSession(id, username, role, branchId);
                showCustomMessage("Success", "Welcome back, " + username + " (" + role + ")", JOptionPane.INFORMATION_MESSAGE);

                this.dispose();

                // Open Dashboard Based on Role
                switch (role) {
                    case "GLOBAL_ADMIN":
                        new GlobalAdminDashboard().setVisible(true);
                        break;
                    case "LOCAL_ADMIN":
                        new LocalAdminDashboard().setVisible(true);
                        break;
                    case "CASHIER":
                        new CashierDashboard().setVisible(true);
                        break;
                }
            } else {
                showCustomMessage("Error", "Invalid Username or Password!", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            showCustomMessage("Database Error", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCustomMessage(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    public static void main(String[] args) {
        // Set System Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}