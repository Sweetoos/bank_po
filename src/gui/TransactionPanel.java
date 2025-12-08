package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class TransactionPanel extends JPanel {

    private Color colorBackground = new Color(51, 48, 46);
    private Color colorCard = new Color(61, 54, 50);
    private Color colorButton = new Color(255, 98, 0);
    private Color colorTextField = new Color(79, 71, 65);
    private Color colorSelection = new Color(128, 88, 64);

    private JPanel menuPanel;
    private CardLayout cardLayout;

    private double balance = 1000.00;
    private double overdraftLimit = 300.00;

    public TransactionPanel(JPanel menuPanel, CardLayout cardLayout) {
        this.menuPanel = menuPanel;
        this.cardLayout = cardLayout;
        initPanel();
    }

    public TransactionPanel() {
        initPanel();
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
        backBtn.addActionListener(e -> {
            if (cardLayout != null && menuPanel != null) {
                cardLayout.show(menuPanel, "MAIN");
            }
        });

        JButton changeLimitBtn = new JButton("Change Overdraft");
        styleMenuButton(changeLimitBtn);
        changeLimitBtn.setPreferredSize(new Dimension(180, 28));
        changeLimitBtn.addActionListener(e -> changeOverdraftLimit());

        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(changeLimitBtn, BorderLayout.EAST);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioPanel.setOpaque(false);

        JLabel lblType = new JLabel("model.Transaction Type:");
        lblType.setForeground(colorButton);

        JRadioButton withdraw = new JRadioButton("Withdraw");
        JRadioButton deposit = new JRadioButton("model.Deposit");
        JRadioButton transfer = new JRadioButton("Transfer");

        styleRadioButton(withdraw);
        styleRadioButton(deposit);
        styleRadioButton(transfer);

        ButtonGroup group = new ButtonGroup();
        group.add(withdraw);
        group.add(deposit);
        group.add(transfer);

        radioPanel.add(lblType);
        radioPanel.add(withdraw);
        radioPanel.add(deposit);
        radioPanel.add(transfer);

        card.add(radioPanel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        amountPanel.setOpaque(false);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setForeground(colorButton);

        JTextField amountField = new JTextField();
        styleTextField(amountField);

        ((AbstractDocument) amountField.getDocument()).setDocumentFilter(new DecimalDocumentFilter());

        amountPanel.add(amountLabel);
        amountPanel.add(amountField);
        card.add(amountPanel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel accPanel = new JPanel();
        accPanel.setLayout(new BoxLayout(accPanel, BoxLayout.Y_AXIS));
        accPanel.setOpaque(false);

        JLabel accLabel = new JLabel("model.Bank model.Account ID:");
        accLabel.setForeground(colorButton);

        JTextField accField = new JTextField();
        styleTextField(accField);

        accPanel.add(accLabel);
        accPanel.add(accField);
        accPanel.setVisible(false);

        transfer.addActionListener(e -> accPanel.setVisible(true));
        withdraw.addActionListener(e -> accPanel.setVisible(false));
        deposit.addActionListener(e -> accPanel.setVisible(false));

        card.add(accPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton submit = new JButton("Submit");
        styleMenuButton(submit);

        JPanel submitPanel = new JPanel();
        submitPanel.setOpaque(false);
        submitPanel.add(submit);

        card.add(submitPanel);

        JLabel resultLabel = new JLabel("");
        resultLabel.setForeground(colorButton);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(resultLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(card);

        add(centerWrap, BorderLayout.CENTER);

        submit.addActionListener(e -> {

            if (!withdraw.isSelected() && !deposit.isSelected() && !transfer.isSelected()) {
                JOptionPane.showMessageDialog(this, "Select a transaction type.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (amountField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Amount is required.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountField.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (deposit.isSelected()) {
                depositOperation(amount);
            } else if (withdraw.isSelected()) {
                withdrawOperation(amount);
            } else if (transfer.isSelected()) {
                if (accField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Target account ID is required.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                transferOperation(amount);
            }
        });
    }

    private void styleArrowButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btn.setBackground(colorCard);
        btn.setForeground(colorButton);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setPreferredSize(new Dimension(60, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.getModel().addChangeListener(e -> {
            ButtonModel model = btn.getModel();
            if (model.isPressed()) {
                btn.setForeground(colorButton.darker());
            } else {
                btn.setForeground(colorButton);
            }
        });
    }

    private void styleMenuButton(JButton btn) {
        btn.setBackground(colorCard);
        btn.setForeground(colorButton);
        btn.setBorder(BorderFactory.createLineBorder(colorButton, 3, true));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.getModel().addChangeListener(e -> {
            ButtonModel model = (ButtonModel) e.getSource();
            if (model.isPressed()) {
                btn.setForeground(colorButton.darker());
                btn.setBorder(BorderFactory.createLineBorder(colorButton.darker(), 3, true));
            } else {
                btn.setForeground(colorButton);
                btn.setBorder(BorderFactory.createLineBorder(colorButton, 3, true));
            }
        });
    }

    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setBackground(colorTextField);
        field.setForeground(colorButton);
        field.setCaretColor(colorButton);
        field.setBorder(BorderFactory.createLineBorder(colorButton, 2, true));

    }

    private void styleRadioButton(JRadioButton btn) {
        btn.setOpaque(true);
        btn.setBackground(colorCard);
        btn.setForeground(colorButton);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(colorButton, 1, true));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 28));

        btn.setUI(new javax.swing.plaf.basic.BasicRadioButtonUI());

        btn.getModel().addChangeListener(e -> {
            ButtonModel model = btn.getModel();
            if (model.isPressed()) {
                btn.setForeground(colorButton.darker());
                btn.setBorder(BorderFactory.createLineBorder(colorButton.darker(), 1, true));
            } else {
                btn.setForeground(colorButton);
                btn.setBorder(BorderFactory.createLineBorder(colorButton, 1, true));
            }
        });
    }

    private void depositOperation(double amount) {
        balance += amount;
        JOptionPane.showMessageDialog(this, "Deposited: " + amount + "\nNew balance: " + balance, "model.Deposit", JOptionPane.INFORMATION_MESSAGE);
    }

    private void withdrawOperation(double amount) {
        int tries = 0;
        while (tries < 3) {
            if (firstCheck(amount)) {
                balance -= amount;
                JOptionPane.showMessageDialog(this, "Withdrawn: " + amount + "\nNew balance: " + balance, "Withdraw", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (secondCheck(amount)) {
                balance -= amount;
                JOptionPane.showMessageDialog(this, "Withdrawn (overdraft): " + amount + "\nNew balance: " + balance, "Withdraw", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            tries++;
        }
        JOptionPane.showMessageDialog(this, "Insufficient funds. Too many attempts.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void transferOperation(double amount) {
        int tries = 0;
        while (tries < 3) {
            if (firstCheck(amount)) {
                balance -= amount;
                JOptionPane.showMessageDialog(this, "Transferred: " + amount + "\nNew balance: " + balance, "Transfer", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (secondCheck(amount)) {
                balance -= amount;
                JOptionPane.showMessageDialog(this, "Transferred (overdraft): " + amount + "\nNew balance: " + balance, "Transfer", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            tries++;
        }
        JOptionPane.showMessageDialog(this, "Insufficient funds. Too many attempts.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void changeOverdraftLimit() {
        String input = JOptionPane.showInputDialog(this, "Enter new overdraft limit:", overdraftLimit);
        if (input != null) {
            try {
                double newLimit = Double.parseDouble(input);
                if (newLimit < 0) throw new NumberFormatException();
                overdraftLimit = newLimit;
                JOptionPane.showMessageDialog(this, "New overdraft limit: " + overdraftLimit, "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean firstCheck(double amount) {
        return amount <= balance;
    }

    private boolean secondCheck(double amount) {
        return amount <= balance + overdraftLimit;
    }

    private static class DecimalDocumentFilter extends DocumentFilter {

        private final String regex = "\\d{0,7}(\\.\\d{0,2})?";

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            String newText = fb.getDocument().getText(0, fb.getDocument().getLength());
            newText = newText.substring(0, offset) + text + newText.substring(offset + length);

            if (newText.matches(regex) || newText.isEmpty()) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {

            String newText = fb.getDocument().getText(0, fb.getDocument().getLength());
            newText = newText.substring(0, offset) + text + newText;

            if (newText.matches(regex) || newText.isEmpty()) {
                super.insertString(fb, offset, text, attr);
            }
        }
    }
}
