package GUI;

import org.example.Contacts;
import org.example.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class ContactsApp extends JFrame {
    private JPanel root;
    private JTextField searchBar;
    private JButton searchButton;
    private JButton editButton;
    private JButton deleteButton;
    public JTable table1; // public to allow refreshing
    private JButton newButton;
    private JPanel title;
    private JLabel contactManager;
    private JPanel tools;
    private JPanel contacts;
    private JComboBox<String> Sortby; // Use generics for type safety


    public ContactsApp() {
        setTitle("Contact Manager");
        setContentPane(root);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        refreshTable();

        // --- SORTING ---
        // CHANGED: Simplified options
        Sortby.setModel(new DefaultComboBoxModel<>(new String[]{"Default", "Name", "Group"}));
        Sortby.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selection = (String) e.getItem();
                // CHANGED: Correctly calls the right sort method
                switch (selection) {
                    case "Name":
                        table1.setModel(Main.sortByName());
                        break;
                    case "Group":
                        table1.setModel(Main.sortByGroup());
                        break;
                    default:
                        refreshTable();
                        break;
                }
            }
        });

        // --- BUTTONS ---
        newButton.addActionListener(e -> {
            // Opens form in "Add Mode"
            new ContactForm(this).setVisible(true);
        });

        editButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(root, "Please select a contact to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int modelRow = table1.convertRowIndexToModel(selectedRow);
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            int id = (int) model.getValueAt(modelRow, 0);
            String name = (String) model.getValueAt(modelRow, 1);
            String phone = (String) model.getValueAt(modelRow, 2);
            String email = (String) model.getValueAt(modelRow, 3);
            String group = (String) model.getValueAt(modelRow, 4);
            String note = (String) model.getValueAt(modelRow, 5);

            Contacts contactToEdit = new Contacts(id, name, phone, email, group, note);

            // CHANGED: Opens form in "Edit Mode" by passing the contact object
            new ContactForm(this, contactToEdit).setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(root, "Please select a contact to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(root, "Delete this contact?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int modelRow = table1.convertRowIndexToModel(selectedRow);
                int id = (int) table1.getModel().getValueAt(modelRow, 0);
                // A temporary contact object with only the ID is needed for deletion
                Contacts contactToDelete = new Contacts(id, null, null, null, null, null);
                Main.deleteContacts(contactToDelete);
                refreshTable();
                Sortby.setSelectedIndex(0);
            }
        });

        // Search logic remains the same
        searchButton.addActionListener((ActionEvent e) -> {
            // ... your existing search code ...
            String query = searchBar.getText().trim().toLowerCase();
            DefaultTableModel model = Main.loadContacts();
            DefaultTableModel searchResultModel = new DefaultTableModel();
            for (int i = 0; i < model.getColumnCount(); i++) {
                searchResultModel.addColumn(model.getColumnName(i));
            }
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean match = false;
                for (int j = 1; j <= 4; j++) {
                    Object cellValue = model.getValueAt(i, j);
                    if (cellValue != null && cellValue.toString().toLowerCase().contains(query)) {
                        match = true;
                        break;
                    }
                }
                if (match) {
                    searchResultModel.addRow((java.util.Vector) model.getDataVector().elementAt(i));
                }
            }
            table1.setModel(searchResultModel);
            Sortby.setSelectedIndex(0);
        });
    }

    public void refreshTable() {
        table1.setModel(Main.loadContacts());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContactsApp().setVisible(true));
    }
}