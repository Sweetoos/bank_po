package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MainMenu extends JFrame {
    Color color1 = new Color(255, 98, 0);
    Color color4 = new Color(179, 101, 54);
    Color color6 = new Color(128, 88, 64);
    Color color9 = new Color(51, 45, 41);
    Color color10 = new Color(51, 48, 46);
    Color colorTableRow = new Color(61, 54, 50);

    String[] accounts = {"Admin", "Demo1", "Demo2", "Demo3"};
    String[] accountsMoney = {"3039.76 PLN", "200.13 PLN", "0.00 PLN", "1.20 PLN"};
    int accountNumber = 0;

    JPanel menuPanel = new JPanel();
    CardLayout cardLayout = new CardLayout();

    JPanel mainMenuView = new JPanel();
    TransactionPanel transactionPanel;
    ManageAccountsPanel manageAccountsPanel;

    JPanel accountPanel = new JPanel();
    JPanel accountInfoPanel = new JPanel();
    JPanel transactionHistoryPanel = new JPanel();
    JPanel optionsPanel = new JPanel();

    JLabel currentAccountLabel = new JLabel();
    JLabel moneyAmountLabel = new JLabel();

    JButton nextButton = new JButton(">");
    JButton backButton = new JButton("<");
    JButton logoutButton = new JButton("Logout");

    JButton transactionOptionButton = new JButton("model.Transaction");
    JButton manageAccountsButton = new JButton("Manage Accounts");

    private DefaultTableModel transactionModel;
    private JTable transactionTable;
    private java.util.List<Transaction> transactions;

    public MainMenu() {
        super("Main Menu");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);

        menuPanel.setLayout(cardLayout);

        transactionPanel = new TransactionPanel(menuPanel, cardLayout);
        manageAccountsPanel = new ManageAccountsPanel(menuPanel, cardLayout);

        transactions = new ArrayList<>();
        initDemoTransactions();

        buildMainMenuView();

        menuPanel.add(mainMenuView, "MAIN");
        menuPanel.add(transactionPanel, "TRANSACTION");
        menuPanel.add(manageAccountsPanel, "ACCOUNTS");

        add(menuPanel);
        setVisible(true);
    }

    private void initDemoTransactions() {
        transactions.add(new Transaction(2, "Demo1", -50.0, "Withdraw"));
        transactions.add(new Transaction(1, "Admin", +100.0, "model.Deposit"));
        transactions.add(new Transaction(3, "Demo2", -20.0, "Transfer"));
        transactions.add(new Transaction(1, "Admin", +20.0, "Transfer"));
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

        currentAccountLabel.setText(accounts[accountNumber]);
        currentAccountLabel.setForeground(color1);
        currentAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentAccountLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        moneyAmountLabel.setText(accountsMoney[accountNumber]);
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

        String[] columns = {"ID", "model.Account", "Amount", "Type"};

        transactionModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        transactionTable = new JTable(transactionModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(colorTableRow);
                c.setForeground(Color.WHITE);
                if (c instanceof JComponent jc) jc.setBorder(BorderFactory.createEmptyBorder());
                return c;
            }
        };

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

        refreshTransactionTable();

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

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(20,150,20,20);
        mainMenuView.add(accountPanel, c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 3;
        c.fill = GridBagConstraints.BOTH;
        mainMenuView.add(transactionHistoryPanel, c);

        c.gridx = 1;
        c.gridy = 2;
        mainMenuView.add(optionsPanel, c);

        backButton.addActionListener(e -> changeAccount(-1));
        nextButton.addActionListener(e -> changeAccount(1));

        transactionOptionButton.addActionListener(e -> cardLayout.show(menuPanel, "TRANSACTION"));
        manageAccountsButton.addActionListener(e -> cardLayout.show(menuPanel, "ACCOUNTS"));

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage();
            }
        });
    }

    private void styleArrowButton(JButton btn) {
        Color normalText = color1;
        Color pressedText = color1.darker();

        btn.setFont(new Font("Segoe UI", Font.BOLD, 44));
        btn.setBackground(new Color(0,0,0,0)); // transparent
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

        btn.setBackground(new Color(0,0,0,0)); // transparent
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

    private void changeAccount(int direction) {
        accountNumber = (accountNumber + direction + accounts.length) % accounts.length;
        currentAccountLabel.setText(accounts[accountNumber]);
        moneyAmountLabel.setText(accountsMoney[accountNumber]);
        refreshTransactionTable();
    }

    private void refreshTransactionTable() {
        transactionModel.setRowCount(0);
        for (Transaction t : transactions) {
            if (t.accountId == accountNumber + 1) {
                String amountStr = (t.amount >= 0 ? "+" : "") + String.format("%.2f PLN", t.amount);
                transactionModel.addRow(new Object[]{t.accountId, t.accountName, amountStr, t.type});
            }
        }
    }

    private static class Transaction {
        int accountId;
        String accountName;
        double amount;
        String type;

        public Transaction(int accountId, String accountName, double amount, String type) {
            this.accountId = accountId;
            this.accountName = accountName;
            this.amount = amount;
            this.type = type;
        }
    }
}
