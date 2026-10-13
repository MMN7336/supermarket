package com.myapp.view;

import com.myapp.session.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GlobalAdminDashboard extends JFrame {

    private final Color BG_DARK = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TEXT_LIGHT = new Color(241, 245, 249);
    private final Color TEXT_MUTED = new Color(148, 163, 184);

    // CardLayout ဖြင့် Screen မထပ်စေဘဲ Panel လှဲရန် Variable များ
    private CardLayout cardLayout;
    private JPanel rootPanel;

    public GlobalAdminDashboard() {
        if (!"GLOBAL_ADMIN".equals(UserSession.getRole())) {
            JOptionPane.showMessageDialog(null, "Access Denied: Global Admins Only!");
            this.dispose();
            return;
        }

        setTitle("Supermarket ERP - HQ Global Admin Dashboard");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. CardLayout ပြင်ဆင်ခြင်း
        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        // 2. Main Dashboard Panel ဆောက်ခြင်း
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header Control Center
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(18, 24, 18, 24)
        ));

        JLabel lblTitle = new JLabel("HQ Global Control Center");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_LIGHT);

        JLabel lblUser = new JLabel("Logged in as: " + UserSession.getUsername() + " (HQ SuperAdmin)");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(TEXT_MUTED);

        JPanel titleGroup = new JPanel(new GridLayout(2, 1, 4, 4));
        titleGroup.setOpaque(false);
        titleGroup.add(lblTitle);
        titleGroup.add(lblUser);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(239, 68, 68));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(100, 40));
        btnLogout.addActionListener(e -> {
            UserSession.logout();
            new LoginPage().setVisible(true);
            this.dispose();
        });

        headerPanel.add(titleGroup, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);

        // Cards Grid Container
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setOpaque(false);

        // Card 1: View Products (Window အသစ်မပွင့်ဘဲ Panel အဖြစ် ပြောင်းလဲမည်)
        JPanel cardProducts = createDashboardCard(
                "View All Products",
                "Monitor real-time product inventory across all branch stores",
                new Color(37, 99, 235)
        );
        cardProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Window အသစ်မဖွင့်ဘဲ Dashboard ထဲမှာတင် Products Page ကို ချိန်းပြမည်
                cardLayout.show(rootPanel, "PRODUCTS_PANEL");
            }
        });

        // Card 2: Sales Reports
        JPanel cardReports = createDashboardCard(
                "System Sales Reports",
                "Real-time Revenue Analytics & Sales History across all branches",
                new Color(16, 185, 129)
        );
        cardReports.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!"GLOBAL_ADMIN".equals(UserSession.getRole())) {
                    JOptionPane.showMessageDialog(GlobalAdminDashboard.this,
                            "Access Denied: Global Admins Only!",
                            "Access Denied",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // Window အသစ်မဖွင့်ဘဲ Panel Switch အဖြစ် ပြောင်းလဲထားပါသည်
                cardLayout.show(rootPanel, "REPORTS_PANEL");
            }
        });

        // Card 3: Manage Users & Staff
        JPanel cardUsers = createDashboardCard(
                "Manage Staff & Users",
                "Create, Update, and Control Admins & Cashier Accounts",
                new Color(168, 85, 247)
        );
        cardUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!"GLOBAL_ADMIN".equals(UserSession.getRole())) {
                    JOptionPane.showMessageDialog(GlobalAdminDashboard.this,
                            "Access Denied: Global Admins Only!",
                            "Access Denied",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // Window အသစ်မဖွင့်ဘဲ Panel Switch အဖြစ် ပြောင်းလဲထားပါသည်
                cardLayout.show(rootPanel, "USERS_PANEL");
            }
        });

        // Card 4: System Settings
        JPanel cardSettings = createDashboardCard(
                "System Settings",
                "Configure Global Tax rates, Currency, and Database parameters",
                new Color(245, 158, 11)
        );
        cardSettings.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Window အသစ်မဖွင့်ဘဲ Panel Switch အဖြစ် ပြောင်းလဲထားပါသည်
                cardLayout.show(rootPanel, "SETTINGS_PANEL");
            }
        });

        gridPanel.add(cardProducts);
        gridPanel.add(cardReports);
        gridPanel.add(cardUsers);
        gridPanel.add(cardSettings);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // Page တစ်ခုချင်းစီအတွက် Back Button ပါဝင်သည့် Wrapper Panel များ ဆောက်ခြင်း
        JPanel productsWrapperPanel = createProductsWrapperPanel();
        JPanel reportsWrapperPanel = createWrapperPanel(new SystemSalesReportPage(this).getContentPane());
        JPanel usersWrapperPanel = createWrapperPanel(new ManageUsersPage(this).getContentPane());
        JPanel settingsWrapperPanel = createWrapperPanel(new SystemSettingsPage().getContentPane());

        // Root Panel ထဲသို့ စာမျက်နှာ Panels များကို Card အဖြစ် ထည့်သွင်းခြင်း
        rootPanel.add(mainPanel, "DASHBOARD_PANEL");
        rootPanel.add(productsWrapperPanel, "PRODUCTS_PANEL");
        rootPanel.add(reportsWrapperPanel, "REPORTS_PANEL");
        rootPanel.add(usersWrapperPanel, "USERS_PANEL");
        rootPanel.add(settingsWrapperPanel, "SETTINGS_PANEL");

        add(rootPanel);
    }

    // Products Page Panel ထဲတွင် Back Button ပါဝင်အောင် ထည့်ပေးသည့် Method
    private JPanel createProductsWrapperPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);

        // Back Button Panel
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(CARD_BG);
        
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setForeground(Color.BLACK);
        btnBack.setBackground(new Color(51, 65, 85));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Back Button နှိပ်ပါက Dashboard သို့ ပြန်သွားမည်
        btnBack.addActionListener(e -> cardLayout.show(rootPanel, "DASHBOARD_PANEL"));
        
        topBar.add(btnBack);

        ManageProductsPage productsPage = new ManageProductsPage(true);
        
        panel.add(topBar, BorderLayout.NORTH);
        panel.add(productsPage.getContentPane(), BorderLayout.CENTER);

        return panel;
    }

    // အခြား Page များ (Sales Reports, Users, Settings) အတွက် Back Button ပါဝင်သော Wrapper Panel Method
    private JPanel createWrapperPanel(Container pageContent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(CARD_BG);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setForeground(Color.BLACK);
        btnBack.setBackground(new Color(51, 65, 85));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Back Button နှိပ်ပါက Dashboard မူလစာမျက်နှာသို့ ပြန်သွားမည်
        btnBack.addActionListener(e -> cardLayout.show(rootPanel, "DASHBOARD_PANEL"));

        topBar.add(btnBack);

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(pageContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDashboardCard(String title, String description, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Left accent border bar
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accentColor);
        accentBar.setPreferredSize(new Dimension(5, 0));

        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        contentPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(TEXT_LIGHT);

        JLabel lblDesc = new JLabel("<html><body style='width: 250px;'>" + description + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(TEXT_MUTED);

        contentPanel.add(lblTitle);
        contentPanel.add(lblDesc);

        card.add(accentBar, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);

        // Hover Effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(40, 53, 72));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BG);
            }
        });

        return card;
    }
}