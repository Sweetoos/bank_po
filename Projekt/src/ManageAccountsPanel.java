import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ManageAccountsPanel extends JPanel {

    /* ============================================================
       =============== COLOR PALETTE & UI CONSTANTS ===============
       ============================================================ */

    private Color colorBackground = new Color(51, 48, 46);
    private Color colorCard = new Color(51, 45, 41);
    private Color colorButton = new Color(255, 98, 0);
    private Color colorTableRow = new Color(61, 54, 50);
    private Color colorTableHeader = new Color(128, 88, 64);
    private Color colorSelection = new Color(90, 76, 65);

    /* ============================================================
       =================== PANEL NAVIGATION DATA ===================
       ============================================================ */

    private JPanel menuPanel;
    private CardLayout cardLayout;

    /* ============================================================
       ======================= ACCOUNT DATA =========================
       ============================================================ */

    private ArrayList<Account> accounts;
    private int lastAccountId = 0;

    /* ============================================================
       ========================= TABLE DATA =========================
       ============================================================ */

    private DefaultTableModel tableModel;
    private JTable accountTable;

    /* ============================================================
       ========================= CONSTRUCTOR ========================
       ============================================================ */

    public ManageAccountsPanel(JPanel menuPanel, CardLayout cardLayout) {
        this.menuPanel = menuPanel;
        this.cardLayout = cardLayout;
        initDemoData();   // Load demo accounts
        initPanel();      // Build full UI layout
    }

    /* ============================================================
       =============== INITIAL DEMO DATA GENERATION =================
       ============================================================ */

    private void initDemoData() {
        /* Creates demo accounts for preview and testing */
        accounts = new ArrayList<>();
        addAccountDemo("Admin", "Admin", 3039.76, "admin@example.com");
        addAccountDemo("Demo1", "Standardowe", 200.13, "demo1@example.com");
        addAccountDemo("Demo2", "Oszczędnościowe", 0.0, "demo2@example.com");
        addAccountDemo("Demo3", "Standardowe", 1.20, "demo3@example.com");
    }

    private void addAccountDemo(String name, String type, double balance, String address) {
        /* Helper method that creates a preconfigured demo account */
        AccountData ad = new AccountData();
        ad.accountId = ++lastAccountId;
        ad.name = name;
        ad.type = type;
        ad.balance = balance;
        ad.address = address;
        accounts.add(new Account(ad));
    }

    /* ============================================================
       ======================= MAIN PANEL UI ========================
       ============================================================ */

    private void initPanel() {
        /* Base panel layout setup */
        setLayout(new BorderLayout());
        setBackground(colorBackground);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(colorCard);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        /* ========================= HEADER ========================= */

        JButton backBtn = new JButton("<");
        styleArrowButton(backBtn);

        /* Navigate back to MAIN screen */
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

        /* ========================= TABLE ========================== */

        String[] columns = {"ID", "Name", "Type", "Balance"};

        tableModel = new DefaultTableModel(columns, 0) {
            /* Prevent table cells from being editable */
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        /* JTable with row coloring and custom rendering */
        accountTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                /* Selected row highlight */
                if (isRowSelected(row)) {
                    c.setBackground(colorSelection);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(colorTableRow);
                    c.setForeground(Color.WHITE);
                }

                /* Remove default cell border */
                if (c instanceof JComponent jc)
                    jc.setBorder(BorderFactory.createEmptyBorder());

                return c;
            }
        };

        /* Table styling */
        accountTable.setFillsViewportHeight(true);
        accountTable.setOpaque(false);  // Remove default background
        accountTable.getTableHeader().setReorderingAllowed(false);
        accountTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accountTable.setRowHeight(36);

        /* Header styling */
        accountTable.getTableHeader().setBackground(colorTableHeader);
        accountTable.getTableHeader().setForeground(colorButton);
        accountTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));

        accountTable.setSelectionForeground(Color.WHITE);
        accountTable.setGridColor(colorBackground);
        accountTable.setShowGrid(true);
        accountTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        /* Column width configuration */
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

        /* ======================== BUTTONS ========================= */

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        JButton addBtn = new JButton("Add Account");
        JButton removeBtn = new JButton("Remove Account");
        JButton infoBtn = new JButton("Account Information");

        /* Apply unified styling */
        styleMenuButton(addBtn);
        styleMenuButton(removeBtn);
        styleMenuButton(infoBtn);

        /* Button actions */
        addBtn.addActionListener(e -> addAccount());
        removeBtn.addActionListener(e -> removeAccount());
        infoBtn.addActionListener(e -> showAccountInformation());

        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(infoBtn);

        /* Wrapping and centering the card */
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(card);

        add(centerWrap, BorderLayout.CENTER);
    }

    /* ============================================================
       ======================== TABLE UPDATE ========================
       ============================================================ */

    private void refreshTable() {
        /* Clears table and re-adds account rows */
        tableModel.setRowCount(0);

        int minRows = 6;
        int i = 0;

        /* Insert existing accounts */
        for (; i < accounts.size(); i++) {
            Account acc = accounts.get(i);

            tableModel.addRow(new Object[]{
                    acc.getId(),
                    acc.getName(),
                    acc.getType(),
                    String.format("%.2f PLN", acc.getBalance())
            });
        }

        /* Add empty background rows to keep consistent height */
        for (; i < minRows; i++) {
            tableModel.addRow(new Object[]{"", "", "", ""});
        }
    }

    /* ============================================================
       ===================== ACCOUNT MANAGEMENT =====================
       ============================================================ */

    private void addAccount() {
        /* Collect account creation data via dialogs */

        AccountData ad = new AccountData();
        ad.accountId = ++lastAccountId;

        ad.name = JOptionPane.showInputDialog(this, "Enter account name:");
        if (ad.name == null || ad.name.trim().isEmpty()) return;

        /* Account type selection */
        String[] types = {"Admin", "Standardowe", "Oszczędnościowe"};
        ad.type = (String) JOptionPane.showInputDialog(
                this, "Select account type:", "Account Type",
                JOptionPane.QUESTION_MESSAGE, null, types, types[1]
        );
        if (ad.type == null) return;

        /* Account address */
        String addr = JOptionPane.showInputDialog(this, "Enter account address:");
        ad.address = (addr == null ? "" : addr.trim());

        ad.balance = 0.0;

        accounts.add(new Account(ad));
        refreshTable();
    }

    private void removeAccount() {
        /* Removes the currently selected account */
        int selected = accountTable.getSelectedRow();
        if (selected != -1) {
            accounts.remove(selected);
            refreshTable();
        }
    }

    private void showAccountInformation() {
        /* Displays detailed info of selected account */
        int selected = accountTable.getSelectedRow();
        if (selected != -1) {
            Account acc = accounts.get(selected);

            String info =
                    "ID: " + acc.getId() +
                            "   Name: " + acc.getName() +
                            "   Type: " + acc.getType() +
                            "   Balance: " + String.format("%.2f PLN", acc.getBalance()) +
                            "   Address: " + acc.getAddress();

            JOptionPane.showMessageDialog(this, info, "Account Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /* ============================================================
       ======================== BUTTON STYLING ======================
       ============================================================ */

    private void styleArrowButton(JButton btn) {
        /* Styling for back-navigation arrow */
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

        /* Press animation */
        btn.getModel().addChangeListener(e -> {
            ButtonModel model = btn.getModel();
            if (model.isPressed())
                btn.setForeground(colorButton.darker());
            else
                btn.setForeground(colorButton);
        });
    }

    private void styleMenuButton(JButton btn) {
        /* Styling for action buttons (Add / Remove / Info) */
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

        /* Press animation */
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

    /* ============================================================
       ========================= DATA CLASSES ========================
       ============================================================ */

    private static class Account {
        /* Represents a single account entry */

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

        /* Getter methods */
        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public double getBalance() { return balance; }
        public String getAddress() { return address; }
    }

    private static class AccountData {
        /* Mutable data container used when creating accounts */
        int accountId;
        String name;
        String type;
        double balance;
        String address;
    }
}
