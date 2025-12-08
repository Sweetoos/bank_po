package org.example.bank_po.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;



public class LoginPage extends JFrame {

    Color color1 = new Color(255, 98, 0);
    Color color2 = new Color(230, 102, 23);
    Color color3 = new Color(204, 103, 41);
    Color color4 = new Color(179, 101, 54);
    Color color5 = new Color(153, 96, 61);
    Color color6 = new Color(128, 88, 64);
    Color color7 = new Color(102, 77, 61);
    Color color8 = new Color(77, 62, 54);
    Color color9 = new Color(51, 45, 41);
    Color color10 = new Color(51, 48, 46);

    JLabel loginLabel = new JLabel("Login:");
    JLabel passwordLabel = new JLabel("Password:");
    JTextField username = new JTextField(3);
    JPasswordField passwd = new JPasswordField(3);
    JButton loginButton = new JButton("Login");
    JPanel loginPanel = new JPanel();
    JPanel passPanel = new JPanel();

    public LoginPage() {
        super("Login Panel"); // tytuł okna
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null); // wyśrodkowanie na ekranie
        int half = this.getWidth() / 2;
        double styler1 = this.getWidth() * 0.76;
        // Tworzymy główny panel z układem pionowym
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Style
        loginButton.setBackground(new Color(98, 136, 148, 0));
        loginButton.setForeground(color1);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(half, 40));
        loginButton.setBorder(new LineBorder(color1, 4, true));


        loginLabel.setBackground(color10);
        loginLabel.setForeground(color1);

        passwordLabel.setBackground(color10);
        passwordLabel.setForeground(color1);

        username.setBackground(color10);
        username.setBorder(new LineBorder(color1, 4, true));
        passwd.setBackground(new Color(98, 136, 148, 0));
        passwd.setBorder(new LineBorder(color1, 4, true));
        mainPanel.setBackground(color10);
        loginPanel.setBackground(color10);
        passPanel.setBackground(color10);






        // Tworzymy panel loginu (etykieta + pole)
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
        loginPanel.setMaximumSize(new Dimension((int)styler1, 30));
        loginLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        loginPanel.add(loginLabel);
        username.setFont(new Font("Courier New", Font.PLAIN, 12));
        loginPanel.add(Box.createRigidArea(new Dimension(37, 0)));
        loginPanel.add(username);

        // Tworzymy panel hasła (etykieta + pole)

        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));
        passPanel.setMaximumSize(new Dimension((int)styler1, 30));
        passwordLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        passPanel.add(passwordLabel);
        passwd.setFont(new Font("Courier New", Font.PLAIN, 12));
        passPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        passPanel.add(passwd);

        // Dodajemy elementy do głównego panelu
        mainPanel.add(loginPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // przerwa
        mainPanel.add(passPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(loginButton);

        // Dodajemy główny panel do ramki
        add(mainPanel);

        // Na końcu – pokazujemy okno
        loginLabel.setOpaque(false);
        passwordLabel.setOpaque(false);
        username.setOpaque(false);
        passwd.setOpaque(false);
        loginButton.setOpaque(false);

        setVisible(true);


    }
}