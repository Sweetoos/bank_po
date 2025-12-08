package gui;

import model.Bank;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginPage extends JFrame {
    Color color1 = new Color(255, 98, 0);
    Color color2 = new Color(230, 102, 23);
    Color color3 = new Color(204, 103, 41);
    Color color4 = new Color(179, 101, 54);
    Color color5 = new Color(153, 96, 61);
    Color color6 = new Color(128, 88, 64);
    Color color7 = new Color(102, 77, 61);
    Color color8 = new Color(58, 53, 49);
    Color color9 = new Color(44, 40, 37);
    Color color10 = new Color(51, 48, 46);

    JLabel loginLabel = new JLabel("Login:");
    JLabel passwordLabel = new JLabel("Password:");
    JTextField username = new JTextField(3);
    JPasswordField passwd = new JPasswordField(3);
    JButton loginButton = new JButton("Log in");
    JPanel mainPanel = new JPanel();
    JPanel loginPanel = new JPanel();
    JPanel passPanel = new JPanel();

    private Bank bank;
    public LoginPage(Bank bank) {
        this.bank=bank;
        super("Login Panel");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        int half = this.getWidth() / 2;
        int styler1 = (int) (this.getWidth() * 0.76);

        loginButton.setBackground(color10);
        loginButton.setForeground(color1);
        loginButton.setBorder(new LineBorder(color1, 4, true));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(half, 50));
        loginButton.setPreferredSize(new Dimension(half, 50));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setOpaque(true);

        loginLabel.setBackground(color10);
        loginLabel.setForeground(color1);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        passwordLabel.setBackground(color10);
        passwordLabel.setForeground(color1);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setOpaque(false);

        username.setBackground(color10);
        username.setForeground(color1);
        username.setBorder(new LineBorder(color1, 3, true));
        username.setCaretColor(color1);
        username.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        username.setOpaque(false);

        passwd.setBackground(color1);
        passwd.setForeground(color1);
        passwd.setBorder(new LineBorder(color1, 3, true));
        passwd.setCaretColor(color1);
        passwd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwd.setOpaque(false);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(color10);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(loginPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(passPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalGlue());

        loginPanel.setBackground(color10);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
        loginPanel.setMaximumSize(new Dimension(styler1, 30));
        loginPanel.setPreferredSize(new Dimension(styler1, 30));
        loginPanel.add(loginLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(37, 0)));
        loginLabel.setOpaque(false);
        loginPanel.add(username);

        passPanel.setBackground(color10);
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));
        passPanel.setMaximumSize(new Dimension(styler1, 30));
        passPanel.setPreferredSize(new Dimension(styler1, 30));
        passPanel.add(passwordLabel);
        passPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        passPanel.add(passwd);


        add(mainPanel);

        loginButton.getModel().addChangeListener(e -> {
            ButtonModel model = loginButton.getModel();

            if (model.isPressed()) {
                loginButton.setForeground(color10);
                loginButton.setBackground(color1);
                loginButton.setBorder(new LineBorder(color1, 4, true));
            } else if (model.isRollover()) {
                loginButton.setForeground(color3);
                loginButton.setBorder(new LineBorder(color3, 4, true));
            } else {
                loginButton.setForeground(color1);
                loginButton.setBorder(new LineBorder(color1, 4, true));
                loginButton.setBackground(color10);
            }
        });

        loginButton.addActionListener(e -> {
            String user = username.getText();
            String pass = new String(passwd.getPassword());
            if (user.equals("admin") && pass.equals("admin")) {
                dispose();

                new MainMenu(this.bank);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login or Password!", "Authorization Error", JOptionPane.ERROR_MESSAGE);
            }

        });
        setVisible(true);

    }
}
