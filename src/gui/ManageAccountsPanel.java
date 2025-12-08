package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ManageAccountsPanel extends JPanel {
    private Color colorBackground = new Color(51, 48, 46);
    private Color colorCard = new Color(51, 45, 41);
    private Color colorButton = new Color(255, 98, 0);
    private Color colorTableRow = new Color(61, 54, 50);
    private Color colorTableHeader = new Color(128, 88, 64);
    private Color colorSelection = new Color(90, 76, 65);
    private JPanel menuPanel;
    private CardLayout cardLayout;
    private ArrayList<Account> accounts;
    private int lastAccountId = 0;
    private DefaultTableModel tableModel;
    private JTable accountTable;

    public ManageAccountsPanel(JPanel menuPanel, CardLayout cardLayout) {
        this.menuPanel = menuPanel;
        this.cardLayout = cardLayout;
        initDemoData();
        initPanel();
    }

    private void initDemoData() {
        accounts = new ArrayList<>();
        addAccountDemo("Admin", "Admin", 3039.76, "admin@example.com");
        addAccountDemo("Demo1", "Standardowe", 200.13, "demo1@example.com");
        addAccountDemo("Demo2", "Oszczędnościowe", 0.0, "demo2@example.com");
        addAccountDemo("Demo3", "Standardowe", 1.20, "demo3@example.com");
    }

    private void addAccountDemo(String name, String type, double balance, String address) {
        AccountData ad = new AccountData();
        ad.accountId = ++lastAccountId;
        ad.name = name;
        ad.type = type;
        ad.balance = balance;
        ad.address = address;
        accounts.add(new Account(ad));
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

        backBtn.addActionListener(e -> {
            if (cardLayout != null && menuPanel != null) {
                cardLayout.show(menuPanel, "MAIN");
            }
        });

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.add(backBtn);

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        String[] columns = {"ID", "Name", "Type", "Balance"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        accountTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(colorSelection);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(colorTableRow);
                    c.setForeground(Color.WHITE);
                }

                if (c instanceof JComponent jc)
                    jc.setBorder(BorderFactory.createEmptyBorder());

                return c;
            }
        };

        accountTable.setFillsViewportHeight(true);
        accountTable.setOpaque(false);  // Remove default background
        accountTable.getTableHeader().setReorderingAllowed(false);
        accountTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accountTable.setRowHeight(36);

        accountTable.getTableHeader().setBackground(colorTableHeader);
        accountTable.getTableHeader().setForeground(colorButton);
        accountTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));

        accountTable.setSelectionForeground(Color.WHITE);
        accountTable.setGridColor(colorBackground);
        accountTable.setShowGrid(true);
        accountTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] columnWidths = {50, 200, 200, 150};
        for (int i = 0; i < columnWidths.length; i++) {
            accountTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        refreshTable(); // Load rows

        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(colorTableRow);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        card.add(scrollPane);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        JButton addBtn = new JButton("Add model.Account");
        JButton removeBtn = new JButton("Remove model.Account");
        JButton infoBtn = new JButton("model.Account Information");

        styleMenuButton(addBtn);
        styleMenuButton(removeBtn);
        styleMenuButton(infoBtn);

        addBtn.addActionListener(e -> addAccount());
        removeBtn.addActionListener(e -> removeAccount());
        infoBtn.addActionListener(e -> showAccountInformation());

        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(infoBtn);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(card);

        add(centerWrap, BorderLayout.CENTER);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        int minRows = 6;
        int i = 0;

        for (; i < accounts.size(); i++) {
            Account acc = accounts.get(i);

            tableModel.addRow(new Object[]{
                    acc.getId(),
                    acc.getName(),
                    acc.getType(),
                    String.format("%.2f PLN", acc.getBalance())
            });
        }

        for (; i < minRows; i++) {
            tableModel.addRow(new Object[]{"", "", "", ""});
        }
    }

    private void addAccount() {
        AccountData ad = new AccountData();
        ad.accountId = ++lastAccountId;

        ad.name = JOptionPane.showInputDialog(this, "Enter account name:");
        if (ad.name == null || ad.name.trim().isEmpty()) return;

        String[] types = {"Admin", "Standardowe", "Oszczędnościowe"};
        ad.type = (String) JOptionPane.showInputDialog(
                this, "Select account type:", "model.Account Type",
                JOptionPane.QUESTION_MESSAGE, null, types, types[1]
        );
        if (ad.type == null) return;

        String addr = JOptionPane.showInputDialog(this, "Enter account address:");
        ad.address = (addr == null ? "" : addr.trim());

        ad.balance = 0.0;

        accounts.add(new Account(ad));
        refreshTable();
    }

    private void removeAccount() {
        int selected = accountTable.getSelectedRow();
        if (selected != -1) {
            accounts.remove(selected);
            refreshTable();
        }
    }

    private void showAccountInformation() {
        int selected = accountTable.getSelectedRow();
        if (selected != -1) {
            Account acc = accounts.get(selected);

            String info =
                    "ID: " + acc.getId() +
                            "   Name: " + acc.getName() +
                            "   Type: " + acc.getType() +
                            "   Balance: " + String.format("%.2f PLN", acc.getBalance()) +
                            "   Address: " + acc.getAddress();

            JOptionPane.showMessageDialog(this, info, "model.Account Information", JOptionPane.INFORMATION_MESSAGE);
        }
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
            if (model.isPressed())
                btn.setForeground(colorButton.darker());
            else
                btn.setForeground(colorButton);
        });
    }

    private void styleMenuButton(JButton btn) {
        btn.setBackground(colorBackground);
        btn.setForeground(colorButton);
        btn.setBorder(new LineBorder(colorButton, 3, true));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.getModel().addChangeListener(e -> {
            ButtonModel model = btn.getModel();
            if (model.isPressed()) {
                btn.setForeground(colorButton.darker());
                btn.setBorder(new LineBorder(colorButton.darker(), 3, true));
            } else {
                btn.setForeground(colorButton);
                btn.setBorder(new LineBorder(colorButton, 3, true));
            }
        });
    }

    private static class Account {
        private int id;
        private String name;
        private String type;
        private double balance;
        private String address;

        public Account(AccountData data) {
            this.id = data.accountId;
            this.name = data.name;
            this.type = data.type;
            this.balance = data.balance;
            this.address = data.address;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public double getBalance() {
            return balance;
        }

        public String getAddress() {
            return address;
        }
    }

    private static class AccountData {
        int accountId;
        String name;
        String type;
        double balance;
        String address;
    }
}