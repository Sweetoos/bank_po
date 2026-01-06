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
    private final Color color4 = new Color(179, 101, 54);
    private final Color color6 = new Color(128, 88, 64);
    private final Color color9 = new Color(51, 45, 41);
    private final Color color10 = new Color(51, 48, 46);
    private final Color colorTableRow = new Color(61, 54, 50);


    private final JPanel menuPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final TransactionPanel transactionPanel;
    private final ManageAccountsPanel manageAccountsPanel;

    private final JLabel currentAccountLabel = new JLabel();
    private final JLabel accountNumberLabel = new JLabel();
    private final JLabel moneyAmountLabel = new JLabel();

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

        JPanel mainMenuView = buildMainMenuView();

        transactionPanel = new TransactionPanel(this, cardLayout, bank, clientForView);
        manageAccountsPanel = new ManageAccountsPanel(this, cardLayout, bank);

        menuPanel.setLayout(cardLayout);
        menuPanel.add(mainMenuView, "MAIN");
        menuPanel.add(transactionPanel, "TRANSACTION");
        menuPanel.add(manageAccountsPanel, "ACCOUNTS");

        add(menuPanel);

        refreshAllViews();
        setVisible(true);
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

        updateClientInfoView();
        transactionPanel.setCurrentClient(clientForView);
        manageAccountsPanel.refreshTable();
    }

    private void updateClientInfoView() {
        if (clientForView != null) {
            currentAccountLabel.setText(clientForView.clientName);
            moneyAmountLabel.setText(String.format("%.2f PLN", clientForView.getBalance()));

            clientForView.getMainAccount().ifPresentOrElse(
                    account -> accountNumberLabel.setText("Account No: " + account.getAccountNumber()),
                    () -> accountNumberLabel.setText("No account assigned")
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

        int panelWidth = (int) (1600 * 0.2);
        int accountInfoWidth = (int) (panelWidth * 0.35);

        JPanel accountPanel = new JPanel();
        accountPanel.setBackground(color9);
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.X_AXIS));
        accountPanel.setPreferredSize(new Dimension(panelWidth, 250));

        JPanel accountInfoPanel = new JPanel();
        accountInfoPanel.setLayout(new BoxLayout(accountInfoPanel, BoxLayout.Y_AXIS));
        accountInfoPanel.setBackground(color9);
        accountInfoPanel.setPreferredSize(new Dimension(accountInfoWidth, 250));

        currentAccountLabel.setForeground(color1);
        currentAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentAccountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        accountNumberLabel.setForeground(color1);
        accountNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountNumberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        moneyAmountLabel.setForeground(color1);
        moneyAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moneyAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        accountInfoPanel.add(currentAccountLabel);
        accountInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        accountInfoPanel.add(accountNumberLabel);
        accountInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        accountInfoPanel.add(moneyAmountLabel);

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

        JPanel transactionHistoryPanel = new JPanel(new BorderLayout());
        transactionHistoryPanel.setBackground(color10);
        transactionHistoryPanel.setPreferredSize(new Dimension(panelWidth + 180, 750));

        String[] columns = {"ID", "Type", "Amount", "Balance After"};
        transactionModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable transactionTable = new JTable(transactionModel);
        setupTransactionTableStyle(transactionTable);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        transactionHistoryPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel optionsPanel = new JPanel(new FlowLayout());
        optionsPanel.setBackground(color10);
        optionsPanel.setPreferredSize(new Dimension(panelWidth, 220));

        JButton transactionOptionButton = new JButton("Transaction");
        JButton manageAccountsButton = new JButton("Manage Accounts");
        JButton logoutButton = new JButton("Logout");

        styleMenuButton(transactionOptionButton);
        styleMenuButton(manageAccountsButton);
        styleMenuButton(logoutButton);

        manageAccountsButton.setVisible(isAdminView);

        optionsPanel.add(transactionOptionButton);
        optionsPanel.add(manageAccountsButton);
        optionsPanel.add(logoutButton);

        c.gridx = 1; c.gridy = 1; c.insets = new Insets(20, 150, 20, 20);
        mainMenuView.add(accountPanel, c);
        c.gridx = 2; c.gridy = 1; c.gridheight = 3; c.fill = GridBagConstraints.BOTH;
        mainMenuView.add(transactionHistoryPanel, c);
        c.gridx = 1; c.gridy = 2; c.gridheight = 1; c.fill = GridBagConstraints.NONE;
        mainMenuView.add(optionsPanel, c);

        backButton.addActionListener(e -> changeClientView(-1));
        nextButton.addActionListener(e -> changeClientView(1));

        transactionOptionButton.addActionListener(e -> cardLayout.show(menuPanel, "TRANSACTION"));
        manageAccountsButton.addActionListener(e -> {
            manageAccountsPanel.refreshTable();
            cardLayout.show(menuPanel, "ACCOUNTS");
        });

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage(bank);
            }
        });

        return mainMenuView;
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
        btn.setPreferredSize(new Dimension(220, 55));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
    }
}