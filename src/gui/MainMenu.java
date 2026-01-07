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
    private final Color color1 = new Color(255, 98, 0);
    private final Color color6 = new Color(128, 88, 64);
    private final Color color9 = new Color(51, 45, 41);
    private final Color color10 = new Color(51, 48, 46);
    private final Color colorTableRow = new Color(61, 54, 50);

    private final JPanel menuPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final TransactionPanel transactionPanel;
    private final ManageAccountsPanel manageAccountsPanel;
    private final ClientAccountsPanel clientAccountsPanel;

    private final JLabel currentAccountLabel = new JLabel();
    private final JLabel accountNumberLabel = new JLabel();
    private final JLabel moneyAmountLabel = new JLabel();
    private final JLabel dateLabel = new JLabel();

    private DefaultTableModel transactionModel;

    private final Bank bank;
    private final Client loggedInClient;
    private final boolean isAdminView;

    private Client clientForView;
    private List<Client> allClientsList;
    private int currentClientIndex = 0;

    public MainMenu(Bank bank, Client loggedInClient) {
        super("Main Menu");
        this.bank = bank;
        this.loggedInClient = loggedInClient;
        this.isAdminView = (loggedInClient == null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));

        JPanel mainMenuView = buildMainMenuView();

        transactionPanel = new TransactionPanel(this, cardLayout, bank, clientForView);
        manageAccountsPanel = new ManageAccountsPanel(this, cardLayout, bank);
        clientAccountsPanel = new ClientAccountsPanel(this, cardLayout, loggedInClient);

        menuPanel.setLayout(cardLayout);
        menuPanel.add(mainMenuView, "MAIN");
        menuPanel.add(transactionPanel, "TRANSACTION");
        menuPanel.add(manageAccountsPanel, "ACCOUNTS_ADMIN");
        menuPanel.add(clientAccountsPanel, "ACCOUNTS_CLIENT");

        add(menuPanel);

        refreshAllViews();
        setVisible(true);
    }

    public Bank getBank() {
        return this.bank;
    }

    public JPanel getMenuPanel() {
        return this.menuPanel;
    }

    public void refreshAllViews() {
        if (isAdminView) {
            allClientsList = new ArrayList<>(bank.getClients().values());
            if (allClientsList.isEmpty()) {
                clientForView = null;
                currentClientIndex = 0;
            } else {
                if (currentClientIndex >= allClientsList.size()) {
                    currentClientIndex = allClientsList.size() > 0 ? allClientsList.size() - 1 : 0;
                }
                if (!allClientsList.isEmpty()) {
                    clientForView = allClientsList.get(currentClientIndex);
                }
            }
        } else {
            clientForView = loggedInClient;
        }

        dateLabel.setText("Sim Date: " + bank.getCurrentDate().toString());
        updateClientInfoView();
        transactionPanel.setCurrentClient(clientForView);
        if (manageAccountsPanel != null) manageAccountsPanel.refreshTable();
        if (clientAccountsPanel != null) clientAccountsPanel.refreshTable();
    }

    private void updateClientInfoView() {
        if (clientForView != null) {
            currentAccountLabel.setText(clientForView.clientName);
            moneyAmountLabel.setText(String.format("%.2f PLN", clientForView.getBalance()));
            clientForView.getMainAccount().ifPresentOrElse(
                    account -> accountNumberLabel.setText("Main Account No: " + account.getAccountNumber()),
                    () -> accountNumberLabel.setText("No main account assigned")
            );
        } else {
            currentAccountLabel.setText(isAdminView ? "No Clients" : "Welcome");
            accountNumberLabel.setText("");
            moneyAmountLabel.setText("0.00 PLN");
        }
        refreshTransactionTable();
    }

    private void changeClientView(int direction) {
        if (!isAdminView || allClientsList.isEmpty()) return;
        currentClientIndex = (currentClientIndex + direction + allClientsList.size()) % allClientsList.size();
        clientForView = allClientsList.get(currentClientIndex);
        refreshAllViews();
    }

    private void refreshTransactionTable() {
        if (transactionModel == null) return;
        transactionModel.setRowCount(0);
        if (clientForView != null) {
            clientForView.getMainAccount().ifPresent(mainAccount -> {
                if (mainAccount.getTransactionHistory() != null) {
                    for (Transaction t : mainAccount.getTransactionHistory()) {
                        String amountStr = String.format("%s%.2f", t.amount >= 0 ? "+" : "", t.amount);
                        transactionModel.addRow(new Object[]{
                                t.transactionId,
                                t.transactionType,
                                amountStr,
                                String.format("%.2f", t.balanceAfter)
                        });
                    }
                }
            });
        }
    }

    private JPanel buildMainMenuView() {
        JPanel mainMenuView = new JPanel(new GridBagLayout());
        mainMenuView.setBackground(color10);
        GridBagConstraints c = new GridBagConstraints();

        JPanel accountPanel = new JPanel(new BoxLayout(this, BoxLayout.X_AXIS));
        accountPanel.setBackground(color9);
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.X_AXIS));

        JPanel accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new BoxLayout(accountInfoPanel, BoxLayout.Y_AXIS));
        accountInfoPanel.setOpaque(false);

        currentAccountLabel.setForeground(color1);
        currentAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentAccountLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        accountNumberLabel.setForeground(color1);
        accountNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        moneyAmountLabel.setForeground(color1);
        moneyAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moneyAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        accountInfoPanel.add(Box.createVerticalGlue());
        accountInfoPanel.add(currentAccountLabel);
        accountInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        accountInfoPanel.add(accountNumberLabel);
        accountInfoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        accountInfoPanel.add(moneyAmountLabel);
        accountInfoPanel.add(Box.createVerticalGlue());

        JButton backButton = new JButton("<");
        JButton nextButton = new JButton(">");
        styleArrowButton(backButton);
        styleArrowButton(nextButton);

        backButton.setVisible(isAdminView);
        nextButton.setVisible(isAdminView);

        accountPanel.add(backButton);
        accountPanel.add(Box.createHorizontalGlue());
        accountPanel.add(accountInfoPanel);
        accountPanel.add(Box.createHorizontalGlue());
        accountPanel.add(nextButton);

        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBackground(color10);

        JButton transactionOptionButton = new JButton("Transaction");
        JButton manageClientsButton = new JButton("Manage Clients");
        JButton myAccountsButton = new JButton("My Accounts");
        JButton setRateButton = new JButton("Set Interest Rate");
        JButton advanceDayBtn = new JButton("+1 Day");
        JButton advanceMonthBtn = new JButton("+30 Days");
        JButton advanceYearBtn = new JButton("+1 Year");
        JButton logoutButton = new JButton("Logout");

        styleMenuButton(transactionOptionButton);
        styleMenuButton(manageClientsButton);
        styleMenuButton(myAccountsButton);
        styleMenuButton(setRateButton);
        styleMenuButton(advanceDayBtn);
        styleMenuButton(advanceMonthBtn);
        styleMenuButton(advanceYearBtn);
        styleMenuButton(logoutButton);

        manageClientsButton.setVisible(isAdminView);
        myAccountsButton.setVisible(!isAdminView);
        setRateButton.setVisible(isAdminView);
        advanceDayBtn.setVisible(isAdminView);
        advanceMonthBtn.setVisible(isAdminView);
        advanceYearBtn.setVisible(isAdminView);

        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        GridBagConstraints gbcOptions = new GridBagConstraints();
        gbcOptions.gridwidth = GridBagConstraints.REMAINDER;
        gbcOptions.fill = GridBagConstraints.HORIZONTAL;
        gbcOptions.insets = new Insets(5, 10, 5, 10);

        optionsPanel.add(transactionOptionButton, gbcOptions);
        optionsPanel.add(manageClientsButton, gbcOptions);
        optionsPanel.add(myAccountsButton, gbcOptions);
        optionsPanel.add(setRateButton, gbcOptions);
        optionsPanel.add(logoutButton, gbcOptions);

        gbcOptions.insets = new Insets(20, 10, 5, 10);
        optionsPanel.add(dateLabel, gbcOptions);
        gbcOptions.insets = new Insets(5, 10, 5, 10);
        optionsPanel.add(advanceDayBtn, gbcOptions);
        optionsPanel.add(advanceMonthBtn, gbcOptions);
        optionsPanel.add(advanceYearBtn, gbcOptions);

        backButton.addActionListener(e -> changeClientView(-1));
        nextButton.addActionListener(e -> changeClientView(1));
        transactionOptionButton.addActionListener(e -> cardLayout.show(menuPanel, "TRANSACTION"));
        manageClientsButton.addActionListener(e -> {
            manageAccountsPanel.refreshTable();
            cardLayout.show(menuPanel, "ACCOUNTS_ADMIN");
        });
        myAccountsButton.addActionListener(e -> {
            clientAccountsPanel.refreshTable();
            cardLayout.show(menuPanel, "ACCOUNTS_CLIENT");
        });
        setRateButton.addActionListener(e -> setGlobalInterestRate());
        advanceDayBtn.addActionListener(e -> advanceTime(1));
        advanceMonthBtn.addActionListener(e -> advanceTime(30));
        advanceYearBtn.addActionListener(e -> advanceTime(365));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage(bank);
            }
        });

        JPanel transactionHistoryPanel = new JPanel(new BorderLayout());
        transactionHistoryPanel.setBackground(color10);
        transactionHistoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] columns = {"ID", "Type", "Amount", "Balance After"};
        transactionModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable transactionTable = new JTable(transactionModel);
        setupTransactionTableStyle(transactionTable);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(color9, 2));
        transactionHistoryPanel.add(scrollPane, BorderLayout.CENTER);

        c.gridx = 0; c.gridy = 0;
        c.weightx = 0.3; c.weighty = 0.4;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 5, 5);
        mainMenuView.add(accountPanel, c);
        c.gridx = 0; c.gridy = 1;
        c.weighty = 0.6;
        c.insets = new Insets(5, 10, 10, 5);
        mainMenuView.add(optionsPanel, c);
        c.gridx = 1; c.gridy = 0;
        c.gridheight = 2;
        c.weightx = 0.7; c.weighty = 1.0;
        c.insets = new Insets(10, 5, 10, 10);
        mainMenuView.add(transactionHistoryPanel, c);

        return mainMenuView;
    }

    private void setGlobalInterestRate() {
        String currentRateStr = String.valueOf(bank.getSavingsAccountInterestRate());
        String newRateStr = JOptionPane.showInputDialog(this, "Enter the new global interest rate for Savings Accounts (%):", currentRateStr);
        if (newRateStr != null) {
            try {
                double newRate = Double.parseDouble(newRateStr);
                if (newRate < 0) throw new NumberFormatException();
                bank.setSavingsAccountInterestRate(newRate);
                JOptionPane.showMessageDialog(this, "Global interest rate has been updated to " + newRate + "%.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid rate entered. Please enter a non-negative number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void advanceTime(int days) {
        bank.advanceTimeByDays(days);
        refreshAllViews();
        JOptionPane.showMessageDialog(this, "Time advanced by " + days + " days.", "Simulation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setupTransactionTableStyle(JTable table) {
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setBackground(colorTableRow);
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(36);
        table.getTableHeader().setBackground(color6);
        table.getTableHeader().setForeground(color1);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setSelectionBackground(colorTableRow);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(color10);
        table.setShowGrid(true);
        table.setFocusable(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void styleArrowButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 44));
        btn.setForeground(color1);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
    }

    private void styleMenuButton(JButton btn) {
        btn.setForeground(color1);
        btn.setBorder(new LineBorder(color1, 4, true));
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
    }
}