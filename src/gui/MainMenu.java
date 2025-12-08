package gui;

import model.Account;
import model.Bank;
import model.Client;
import model.Transaction;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends JFrame {
    // Pola kolorów i komponentów (bez zmian)
    private final Color color1 = new Color(255, 98, 0);
    private final Color color6 = new Color(128, 88, 64);
    private final Color color9 = new Color(51, 45, 41);
    private final Color color10 = new Color(51, 48, 46);
    private final Color colorTableRow = new Color(61, 54, 50);

    // Główne komponenty i layout
    private final JPanel menuPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainMenuView = new JPanel();

    // Pod-panele
    private final TransactionPanel transactionPanel;
    private final ManageAccountsPanel manageAccountsPanel;

    // Komponenty widoku głównego
    private final JPanel accountPanel = new JPanel();
    private final JPanel accountInfoPanel = new JPanel();
    private final JPanel transactionHistoryPanel = new JPanel();
    private final JPanel optionsPanel = new JPanel();
    private final JLabel currentAccountLabel = new JLabel();
    private final JLabel moneyAmountLabel = new JLabel();
    private final JButton nextButton = new JButton(">");
    private final JButton backButton = new JButton("<");
    private final JButton logoutButton = new JButton("Logout");
    private final JButton transactionOptionButton = new JButton("Transaction");
    private final JButton manageAccountsButton = new JButton("Manage Accounts");

    // Tabela transakcji
    private DefaultTableModel transactionModel;
    private JTable transactionTable;

    // Dane z modelu
    private final Bank bank;
    private Client currentClient;
    private List<Client> clientList;
    private int currentClientIndex = 0;

    public MainMenu(Bank bank) {
        super("Main Menu");
        this.bank = bank;

        // Inicjalizacja listy klientów z modelu
        this.clientList = new ArrayList<>(bank.getClients().values());
        if (!clientList.isEmpty()) {
            this.currentClient = clientList.get(0);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);

        menuPanel.setLayout(cardLayout);

        // Przekazywanie danych z modelu do pod-paneli
        transactionPanel = new TransactionPanel(menuPanel, cardLayout, bank, currentClient);
        manageAccountsPanel = new ManageAccountsPanel(menuPanel, cardLayout, bank);

        buildMainMenuView();
        updateAccountView();

        menuPanel.add(mainMenuView, "MAIN");
        menuPanel.add(transactionPanel, "TRANSACTION");
        menuPanel.add(manageAccountsPanel, "ACCOUNTS");

        add(menuPanel);
        setVisible(true);
    }

    // Aktualizuje widok informacji o kliencie i jego transakcjach
    private void updateAccountView() {
        if (currentClient != null) {
            currentAccountLabel.setText(currentClient.clientName);
            moneyAmountLabel.setText(String.format("%.2f PLN", currentClient.getBalance()));
            transactionPanel.setCurrentClient(currentClient); // Aktualizuj klienta w panelu transakcji
        } else {
            currentAccountLabel.setText("No clients found");
            moneyAmountLabel.setText("0.00 PLN");
        }
        refreshTransactionTable();
    }

    private void buildMainMenuView() {
        mainMenuView.setLayout(new GridBagLayout());
        mainMenuView.setBackground(color10);
        GridBagConstraints c = new GridBagConstraints();

        int panelWidth = (int) (1600 * 0.2);
        int accountInfoWidth = (int) (panelWidth * 0.35);

        accountPanel.setBackground(color9);
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.X_AXIS));
        accountPanel.setPreferredSize(new Dimension(panelWidth, 250));

        accountInfoPanel.setLayout(new BoxLayout(accountInfoPanel, BoxLayout.Y_AXIS));
        accountInfoPanel.setBackground(color9);
        accountInfoPanel.setPreferredSize(new Dimension(accountInfoWidth, 250));

        currentAccountLabel.setForeground(color1);
        currentAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentAccountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        moneyAmountLabel.setForeground(color1);
        moneyAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moneyAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        accountInfoPanel.add(currentAccountLabel);
        accountInfoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        accountInfoPanel.add(moneyAmountLabel);

        styleArrowButton(backButton);
        styleArrowButton(nextButton);

        accountPanel.add(backButton);
        accountPanel.add(Box.createHorizontalGlue());
        accountPanel.add(accountInfoPanel);
        accountPanel.add(Box.createHorizontalGlue());
        accountPanel.add(nextButton);

        transactionHistoryPanel.setBackground(color10);
        transactionHistoryPanel.setPreferredSize(new Dimension(panelWidth + 180, 750));

        String[] columns = {"ID", "Type", "Amount", "Balance After"};
        transactionModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        transactionTable = new JTable(transactionModel);
        setupTransactionTableStyle();

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        transactionHistoryPanel.setLayout(new BorderLayout());
        transactionHistoryPanel.add(scrollPane, BorderLayout.CENTER);

        optionsPanel.setLayout(new FlowLayout());
        optionsPanel.setBackground(color10);
        optionsPanel.setPreferredSize(new Dimension(panelWidth, 220));

        styleMenuButton(transactionOptionButton);
        styleMenuButton(manageAccountsButton);
        styleMenuButton(logoutButton);

        optionsPanel.add(transactionOptionButton);
        optionsPanel.add(manageAccountsButton);
        optionsPanel.add(logoutButton);

        c.gridx = 1; c.gridy = 1; c.insets = new Insets(20, 150, 20, 20);
        mainMenuView.add(accountPanel, c);

        c.gridx = 2; c.gridy = 1; c.gridheight = 3; c.fill = GridBagConstraints.BOTH;
        mainMenuView.add(transactionHistoryPanel, c);

        c.gridx = 1; c.gridy = 2; c.gridheight = 1; c.fill = GridBagConstraints.NONE;
        mainMenuView.add(optionsPanel, c);

        backButton.addActionListener(e -> changeAccount(-1));
        nextButton.addActionListener(e -> changeAccount(1));

        transactionOptionButton.addActionListener(e -> cardLayout.show(menuPanel, "TRANSACTION"));
        manageAccountsButton.addActionListener(e -> cardLayout.show(menuPanel, "ACCOUNTS"));

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage(bank);
            }
        });
    }

    private void changeAccount(int direction) {
        if (clientList == null || clientList.isEmpty()) {
            return;
        }
        currentClientIndex = (currentClientIndex + direction + clientList.size()) % clientList.size();
        currentClient = clientList.get(currentClientIndex);
        updateAccountView();
    }

    private void refreshTransactionTable() {
        transactionModel.setRowCount(0);
        if (currentClient != null) {
            Account mainAccount = currentClient.getAcc(currentClient.getMainAccountId());
            if (mainAccount != null && mainAccount.getTransactionHistory() != null) {
                for (Transaction t : mainAccount.getTransactionHistory()) {
                    String amountStr = String.format("%s%.2f PLN", (t.amount >= 0 ? "+" : ""), t.amount);
                    transactionModel.addRow(new Object[]{
                            t.transactionId,
                            t.transactionType,
                            amountStr,
                            String.format("%.2f PLN", t.balanceAfter)
                    });
                }
            }
        }
    }

    private void setupTransactionTableStyle() {
        transactionTable.setFillsViewportHeight(true);
        transactionTable.getTableHeader().setReorderingAllowed(false);
        transactionTable.setBackground(colorTableRow);
        transactionTable.setForeground(Color.WHITE);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        transactionTable.setRowHeight(36);
        transactionTable.getTableHeader().setBackground(color6);
        transactionTable.getTableHeader().setForeground(color1);
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        transactionTable.setSelectionBackground(colorTableRow);
        transactionTable.setSelectionForeground(Color.WHITE);
        transactionTable.setGridColor(color10);
        transactionTable.setShowGrid(true);
        transactionTable.setFocusable(false);
        transactionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] columnWidths = {50, 150, 150, 150};
        for (int i = 0; i < columnWidths.length; i++) {
            transactionTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }

    private void styleArrowButton(JButton btn) {
        Color normalText = color1;
        Color pressedText = color1.darker();
        btn.setFont(new Font("Segoe UI", Font.BOLD, 44));
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setForeground(normalText);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 90));
        btn.setOpaque(false);
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.getModel().addChangeListener(e -> {
            if (btn.getModel().isPressed()) {
                btn.setForeground(pressedText);
            } else {
                btn.setForeground(normalText);
            }
        });
    }

    private void styleMenuButton(JButton btn) {
        Color normalText = color1;
        Color pressedText = color1.darker();
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setForeground(normalText);
        btn.setBorder(new LineBorder(normalText, 4, true));
        btn.setPreferredSize(new Dimension(220, 55));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.getModel().addChangeListener(e -> {
            if (btn.getModel().isPressed()) {
                btn.setForeground(pressedText);
                btn.setBorder(new LineBorder(pressedText, 4, true));
            } else {
                btn.setForeground(normalText);
                btn.setBorder(new LineBorder(normalText, 4, true));
            }
        });
    }
}