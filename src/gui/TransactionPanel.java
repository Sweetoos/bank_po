package gui;

import model.Account;
import model.Bank;
import model.Client;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

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
        radioPanel.add(withdrawRadio);
        radioPanel.add(depositRadio);
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

        JPanel accPanel = new JPanel();
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

        Account mainAccount = currentClient.getMainAccount().orElse(null);
        if (mainAccount == null) {
            JOptionPane.showMessageDialog(this, "Selected client has no main account.", "Error", JOptionPane.ERROR_MESSAGE);
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

        boolean success = false;
        String operationType = "";

        if (depositRadio.isSelected()) {
            operationType = "Deposit";
            mainAccount.deposit(amount);
            success = true;
        } else if (withdrawRadio.isSelected()) {
            operationType = "Withdrawal";
            success = mainAccount.withdraw(amount);
        } else if (transferRadio.isSelected()) {
            operationType = "Transfer";
            try {
                int targetAccNum = Integer.parseInt(accField.getText());
                success = mainAccount.transfer(amount, targetAccNum, bank);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid target account number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a transaction type.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (success) {
            String message = operationType + " successful!\nNew balance: " + String.format("%.2f PLN", mainAccount.getBalance());
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
            amountField.setText("");
            accField.setText("");
            mainMenu.refreshAllViews();
        } else {
            String message = operationType + " failed.\nPlease check available funds or target account number.";
            JOptionPane.showMessageDialog(this, message, "Failure", JOptionPane.ERROR_MESSAGE);
        }
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
        btn.setBorder(BorderFactory.createLineBorder(colorButton, 3, true));
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