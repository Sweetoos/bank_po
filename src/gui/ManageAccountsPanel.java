package gui;

import model.Bank;
import model.Client;
import model.ClientData;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class ManageAccountsPanel extends JPanel {
    private final Color colorBackground = new Color(51, 48, 46);
    private final Color colorCard = new Color(51, 45, 41);
    private final Color colorButton = new Color(255, 98, 0);
    private final Color colorTableRow = new Color(61, 54, 50);
    private final Color colorTableHeader = new Color(128, 88, 64);
    private final Color colorSelection = new Color(90, 76, 65);

    private final MainMenu mainMenu;
    private final CardLayout cardLayout;
    private final Bank bank;

    private DefaultTableModel tableModel;
    private JTable accountTable;
    private List<Client> clientList;

    public ManageAccountsPanel(MainMenu mainMenu, CardLayout cardLayout, Bank bank) {
        this.mainMenu = mainMenu;
        this.cardLayout = cardLayout;
        this.bank = bank;
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

        String[] columns = {"ID", "Name", "Address", "Total Balance"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        accountTable = new JTable(tableModel);
        setupTableStyle();

        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(colorTableRow);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        card.add(scrollPane);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        JButton addBtn = new JButton("Add Client");
        JButton removeBtn = new JButton("Remove Client");
        styleMenuButton(addBtn);
        styleMenuButton(removeBtn);

        addBtn.addActionListener(e -> addClient());
        removeBtn.addActionListener(e -> removeClient());

        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        card.add(buttonPanel);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(card);
        add(centerWrap, BorderLayout.CENTER);
    }

    public void refreshTable() {
        clientList = new ArrayList<>(bank.getClients().values());
        tableModel.setRowCount(0);

        for (Client client : clientList) {
            tableModel.addRow(new Object[]{
                    client.clientId,
                    client.clientName,
                    client.clientAddress,
                    String.format("%.2f PLN", client.getTotalBalance())
            });
        }
    }

    private void addClient() {
        String name = JOptionPane.showInputDialog(this, "Enter client's full name:");
        if (name == null || name.trim().isEmpty()) return;

        String address = JOptionPane.showInputDialog(this, "Enter client's address:");
        if (address == null) return;

        String username = JOptionPane.showInputDialog(this, "Enter a username for the client:");
        if (username == null || username.trim().isEmpty()) return;

        String password = JOptionPane.showInputDialog(this, "Enter a password for the client:");
        if (password == null || password.trim().isEmpty()) return;

        ClientData cd = new ClientData();
        cd.clientName = name;
        cd.clientAddress = address;
        cd.username = username;
        cd.password = password;

        bank.addClient(cd);
        mainMenu.refreshAllViews();
    }

    private void removeClient() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow != -1) {
            Client clientToRemove = clientList.get(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove client: " + clientToRemove.clientName + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                bank.removeClient(clientToRemove.clientId);
                mainMenu.refreshAllViews();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a client to remove.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setupTableStyle() {
        accountTable.setFillsViewportHeight(true);
        accountTable.getTableHeader().setReorderingAllowed(false);
        accountTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accountTable.setRowHeight(36);
        accountTable.getTableHeader().setBackground(colorTableHeader);
        accountTable.getTableHeader().setForeground(colorButton);
        accountTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        accountTable.setSelectionBackground(colorSelection);
        accountTable.setSelectionForeground(Color.WHITE);
        accountTable.setGridColor(colorBackground);
        accountTable.setShowGrid(true);
        accountTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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