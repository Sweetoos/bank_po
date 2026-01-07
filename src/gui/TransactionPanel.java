package gui;

import model.Account;
import model.Bank;
import model.Client;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.List;

public class TransactionPanel extends JPanel {
    private final Color colorBackground = new Color(51, 48, 46);
    private final Color colorCard = new Color(61, 54, 50);
    private final Color colorButton = new Color(255, 98, 0);
    private final Color colorTextField = new Color(79, 71, 65);
    private final Color colorSelection = new Color(128, 88, 64);

    private final MainMenu mainMenu;
    private final CardLayout cardLayout;
    private final Bank bank;
    private Client currentClient;

    private JTextField amountField;
    private JTextField accField;
    private JRadioButton withdrawRadio;
    private JRadioButton depositRadio;
    private JRadioButton transferRadio;
    private JPanel accPanel;

    public TransactionPanel(MainMenu mainMenu, CardLayout cardLayout, Bank bank, Client client) {
        this.mainMenu = mainMenu;
        this.cardLayout = cardLayout;
        this.bank = bank;
        this.currentClient = client;
        initPanel();
    }

    public void setCurrentClient(Client client) {
        this.currentClient = client;
    }

    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(colorBackground);
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(colorCard);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JButton backBtn = new JButton("<");
        styleArrowButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(mainMenu.getMenuPanel(), "MAIN"));
        headerPanel.add(backBtn, BorderLayout.WEST);
        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioPanel.setOpaque(false);
        JLabel lblType = new JLabel("Transaction Type:");
        lblType.setForeground(colorButton);
        withdrawRadio = new JRadioButton("Withdraw");
        depositRadio = new JRadioButton("Deposit");
        transferRadio = new JRadioButton("Transfer");
        styleRadioButton(withdrawRadio);
        styleRadioButton(depositRadio);
        styleRadioButton(transferRadio);
        ButtonGroup group = new ButtonGroup();
        group.add(withdrawRadio);
        group.add(depositRadio);
        group.add(transferRadio);
        radioPanel.add(lblType);
        radioPanel.add(depositRadio);
        radioPanel.add(withdrawRadio);
        radioPanel.add(transferRadio);
        card.add(radioPanel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        amountPanel.setOpaque(false);
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setForeground(colorButton);
        amountField = new JTextField();
        styleTextField(amountField);
        ((AbstractDocument) amountField.getDocument()).setDocumentFilter(new DecimalDocumentFilter());
        amountPanel.add(amountLabel);
        amountPanel.add(amountField);
        card.add(amountPanel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        accPanel = new JPanel();
        accPanel.setLayout(new BoxLayout(accPanel, BoxLayout.Y_AXIS));
        accPanel.setOpaque(false);
        JLabel accLabel = new JLabel("Target Account Number:");
        accLabel.setForeground(colorButton);
        accField = new JTextField();
        styleTextField(accField);
        ((AbstractDocument) accField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
        accPanel.add(accLabel);
        accPanel.add(accField);
        accPanel.setVisible(false);
        transferRadio.addActionListener(e -> accPanel.setVisible(true));
        withdrawRadio.addActionListener(e -> accPanel.setVisible(false));
        depositRadio.addActionListener(e -> accPanel.setVisible(false));
        card.add(accPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton submitBtn = new JButton("Submit");
        styleMenuButton(submitBtn);
        submitBtn.addActionListener(e -> executeTransaction());
        JPanel submitPanel = new JPanel();
        submitPanel.setOpaque(false);
        submitPanel.add(submitBtn);
        card.add(submitPanel);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(card);
        add(centerWrap, BorderLayout.CENTER);
    }

    private void executeTransaction() {
        if (currentClient == null) {
            JOptionPane.showMessageDialog(this, "No client selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid or non-positive amount entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (depositRadio.isSelected()) {
            performDeposit(amount);
        } else if (withdrawRadio.isSelected()) {
            performWithdraw(amount);
        } else if (transferRadio.isSelected()) {
            performTransfer(amount);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a transaction type.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void performDeposit(double amount) {
        Account mainAccount = currentClient.getMainAccount().orElse(null);
        if (mainAccount == null) {
            JOptionPane.showMessageDialog(this, "Client has no main account to deposit to.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mainAccount.deposit(amount);
        JOptionPane.showMessageDialog(this, "Deposit successful!\nNew balance: " + String.format("%.2f PLN", mainAccount.getBalance()), "Success", JOptionPane.INFORMATION_MESSAGE);
        resetFields();
    }

    private void performWithdraw(double amount) {
        Account mainAccount = currentClient.getMainAccount().orElse(null);
        if (mainAccount == null) {
            JOptionPane.showMessageDialog(this, "Client has no main account to withdraw from.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean success = mainAccount.withdraw(amount);
        if (success) {
            JOptionPane.showMessageDialog(this, "Withdrawal successful!\nNew balance: " + String.format("%.2f PLN", mainAccount.getBalance()), "Success", JOptionPane.INFORMATION_MESSAGE);
            resetFields();
        } else {
            JOptionPane.showMessageDialog(this, "Withdrawal failed. Insufficient funds.", "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performTransfer(double amount) {
        List<Account> accounts = currentClient.getAccounts();
        if (accounts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have no accounts to transfer from.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] accountChoices = accounts.stream()
                .map(acc -> "Acc. " + acc.getAccountNumber() + " (" + acc.accountType + ")")
                .toArray();

        int choiceIndex = JOptionPane.showOptionDialog(this, "Select source account for the transfer:",
                "Source Account", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, accountChoices, accountChoices[0]);

        if (choiceIndex == -1) return;

        Account sourceAccount = accounts.get(choiceIndex);
        int targetAccNum;
        try {
            targetAccNum = Integer.parseInt(accField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid target account number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = sourceAccount.transfer(amount, targetAccNum, bank);
        if (success) {
            JOptionPane.showMessageDialog(this, "Transfer successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetFields();
        } else {
            JOptionPane.showMessageDialog(this, "Transfer failed. Check funds or target account number.", "Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFields() {
        amountField.setText("");
        accField.setText("");
        mainMenu.refreshAllViews();
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

    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setBackground(colorTextField);
        field.setForeground(colorButton);
        field.setCaretColor(colorButton);
        field.setBorder(BorderFactory.createLineBorder(colorButton, 2, true));
    }

    private void styleRadioButton(JRadioButton btn) {
        btn.setOpaque(false);
        btn.setForeground(colorButton);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static class DecimalDocumentFilter extends DocumentFilter {
        private final String regex = "\\d{0,10}(\\.\\d{0,2})?";
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
            if (newText.matches(regex)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    private static class IntegerDocumentFilter extends DocumentFilter {
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
            if (newText.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}