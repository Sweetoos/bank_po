package gui;

import model.Account;
import model.Bank;
import model.Client;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ClientAccountsPanel extends JPanel {
    private final Color colorBackground = new Color(51, 48, 46);
    private final Color colorCard = new Color(51, 45, 41);
    private final Color colorButton = new Color(255, 98, 0);
    private final Color colorTableRow = new Color(61, 54, 50);
    private final Color colorTableHeader = new Color(128, 88, 64);
    private final Color colorSelection = new Color(90, 76, 65);

    private final MainMenu mainMenu;
    private final CardLayout cardLayout;
    private final Client loggedInClient;
    private final Bank bank;

    private DefaultTableModel tableModel;
    private JTable accountsTable;

    public ClientAccountsPanel(MainMenu mainMenu, CardLayout cardLayout, Client client) {
        this.mainMenu = mainMenu;
        this.cardLayout = cardLayout;
        this.loggedInClient = client;
        this.bank = mainMenu.getBank();
        initPanel();
    }

    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(colorBackground);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(colorCard);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton backBtn = new JButton("<");
        styleArrowButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(mainMenu.getMenuPanel(), "MAIN"));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.add(backBtn);
        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        String[] columns = {"Account Number", "Type", "Balance", "Is Main"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        accountsTable = new JTable(tableModel);
        setupTableStyle(accountsTable);

        JScrollPane scrollPane = new JScrollPane(accountsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(colorTableRow);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        card.add(scrollPane);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        JButton addAccountBtn = new JButton("Open New Account");
        JButton setMainBtn = new JButton("Set as Main");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton internalTransferBtn = new JButton("Internal Transfer");

        styleMenuButton(addAccountBtn);
        styleMenuButton(setMainBtn);
        styleMenuButton(depositBtn);
        styleMenuButton(withdrawBtn);
        styleMenuButton(internalTransferBtn);

        addAccountBtn.addActionListener(e -> addAccountToClient());
        setMainBtn.addActionListener(e -> setAsMainAccount());
        depositBtn.addActionListener(e -> depositToSelectedAccount());
        withdrawBtn.addActionListener(e -> withdrawFromSelectedAccount());
        internalTransferBtn.addActionListener(e -> performInternalTransfer());

        buttonPanel.add(addAccountBtn);
        buttonPanel.add(setMainBtn);
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(internalTransferBtn);
        card.add(buttonPanel);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(card);
        add(centerWrap, BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        if (loggedInClient != null) {
            for (Account account : loggedInClient.getAccounts()) {
                boolean isMain = (account.getAccountNumber() == loggedInClient.getMainAccountId());
                tableModel.addRow(new Object[]{
                        account.getAccountNumber(),
                        account.accountType,
                        String.format("%.2f PLN", account.getBalance()),
                        isMain ? "YES" : "No"
                });
            }
        }
    }

    private Account getSelectedAccount() {
        int selectedRow = accountsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account from the list first.");
            return null;
        }
        int accountNumber = (int) accountsTable.getValueAt(selectedRow, 0);
        return loggedInClient.getAccountById(accountNumber).orElse(null);
    }

    private void performInternalTransfer() {
        Account sourceAccount = getSelectedAccount();
        if (sourceAccount == null) return;

        List<Account> otherAccounts = loggedInClient.getAccounts().stream()
                .filter(acc -> acc.getAccountNumber() != sourceAccount.getAccountNumber())
                .collect(Collectors.toList());

        if (otherAccounts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have no other accounts to transfer to.");
            return;
        }

        Object[] accountChoices = otherAccounts.stream()
                .map(acc -> "Acc. " + acc.getAccountNumber() + " (" + acc.accountType + ")")
                .toArray();

        int choiceIndex = JOptionPane.showOptionDialog(this, "Transfer from " + sourceAccount.getAccountNumber() + " to:",
                "Select Target Account", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, accountChoices, accountChoices[0]);

        if (choiceIndex == -1) return;

        Account targetAccount = otherAccounts.get(choiceIndex);

        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to transfer:");
        try {
            double amount = Double.parseDouble(amountStr);
            boolean success = sourceAccount.transfer(amount, targetAccount.getAccountNumber(), bank);

            if (success) {
                mainMenu.refreshAllViews();
                JOptionPane.showMessageDialog(this, "Internal transfer successful.");
            } else {
                JOptionPane.showMessageDialog(this, "Transfer failed. Insufficient funds.");
            }
        } catch (NumberFormatException | NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    private void addAccountToClient() {
        String[] accountTypes = {Client.ACCOUNT_TYPE_CHECKING, Client.ACCOUNT_TYPE_SAVINGS};
        String chosenType = (String) JOptionPane.showInputDialog(this, "Select account type to open:", "Open New Account", JOptionPane.QUESTION_MESSAGE, null, accountTypes, accountTypes[0]);
        if (chosenType != null) {
            loggedInClient.addAccount(chosenType, mainMenu.getBank().getSavingsAccountInterestRate());
            mainMenu.refreshAllViews();
            JOptionPane.showMessageDialog(this, chosenType + " account has been opened.");
        }
    }

    private void setAsMainAccount() {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount != null) {
            boolean success = loggedInClient.setMainAccountByNumber(selectedAccount.getAccountNumber());
            if (success) {
                mainMenu.refreshAllViews();
                JOptionPane.showMessageDialog(this, "Account " + selectedAccount.getAccountNumber() + " is now your main account.");
            } else {
                JOptionPane.showMessageDialog(this, "Cannot set a Savings Account as the main account.", "Operation Denied", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void depositToSelectedAccount() {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount != null) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit to account " + selectedAccount.getAccountNumber() + ":");
            try {
                double amount = Double.parseDouble(amountStr);
                selectedAccount.deposit(amount);
                mainMenu.refreshAllViews();
                JOptionPane.showMessageDialog(this, "Deposit successful.");
            } catch (NumberFormatException | NullPointerException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        }
    }

    private void withdrawFromSelectedAccount() {
        Account selectedAccount = getSelectedAccount();
        if (selectedAccount != null) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw from account " + selectedAccount.getAccountNumber() + ":");
            try {
                double amount = Double.parseDouble(amountStr);
                boolean success = selectedAccount.withdraw(amount);
                if (success) {
                    mainMenu.refreshAllViews();
                    JOptionPane.showMessageDialog(this, "Withdrawal successful.");
                } else {
                    JOptionPane.showMessageDialog(this, "Withdrawal failed. Insufficient funds or operation not permitted for this account type.", "Failure", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException | NullPointerException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        }
    }

    private void setupTableStyle(JTable table) {
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(36);
        table.getTableHeader().setBackground(colorTableHeader);
        table.getTableHeader().setForeground(colorButton);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.setSelectionBackground(colorSelection);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(colorBackground);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void styleArrowButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btn.setForeground(colorButton);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setPreferredSize(new Dimension(60, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
    }

    private void styleMenuButton(JButton btn) {
        btn.setForeground(colorButton);
        btn.setBorder(new LineBorder(colorButton, 3, true));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
    }
}