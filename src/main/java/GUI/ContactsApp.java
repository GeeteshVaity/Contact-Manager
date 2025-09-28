package GUI;

import org.example.Contacts;
import org.example.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

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

    public ContactsApp() {
        setTitle("Contact Manager");
        setContentPane(root);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load table data
        refreshTable();

        // Action for New Button → Open ContactForm
        newButton.addActionListener((ActionEvent e) -> {
            ContactForm form = new ContactForm(this); // pass reference for refresh
            form.setVisible(true);
        });

        // Action for Edit Button → Open ContactForm prefilled
        editButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(root, "Please select a contact to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int contactId = (int) table1.getValueAt(selectedRow, 0);
            String name = (String) table1.getValueAt(selectedRow, 1);
            String phone = (String) table1.getValueAt(selectedRow, 2);
            String email = (String) table1.getValueAt(selectedRow, 3);
            String group = (String) table1.getValueAt(selectedRow, 4);
            String note = (String) table1.getValueAt(selectedRow, 5);

            ContactForm form = new ContactForm(this); // pass reference for refresh
            form.setVisible(true);

            // Prefill form
            form.nameField.setText(name);
            form.phoneField.setText(phone);
            form.emailField.setText(email);
            form.comboBox1.setSelectedItem(group);
            form.textArea1.setText(note);

            // Save button for update
            form.saveButton.addActionListener(ev -> {
                Contacts contact = new Contacts(contactId, form.nameField.getText(), form.phoneField.getText(),
                        form.emailField.getText(), (String) form.comboBox1.getSelectedItem(),
                        form.textArea1.getText());
                Main.editContacts(contact);
                JOptionPane.showMessageDialog(form, "Contact updated successfully.", "Updated", JOptionPane.INFORMATION_MESSAGE);
                form.dispose();
                refreshTable();
            });
        });

        // Action for Delete Button
        deleteButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(root, "Please select a contact to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(root, "Are you sure you want to delete this contact?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int contactId = (int) table1.getValueAt(selectedRow, 0);
                Contacts contact = new Contacts(contactId, null, null, null, null, null);
                Main.deleteContacts(contact);
                JOptionPane.showMessageDialog(root, "Contact deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            }
        });

        // Action for Search Button → Optional
        searchButton.addActionListener((ActionEvent e) -> {
            String query = searchBar.getText().trim().toLowerCase();
            DefaultTableModel model = Main.loadContacts();
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                boolean match = false;
                for (int j = 1; j <= 4; j++) { // check name, phone, email, group
                    if (model.getValueAt(i, j).toString().toLowerCase().contains(query)) {
                        match = true;
                        break;
                    }
                }
                if (!match) model.removeRow(i);
            }
            table1.setModel(model);
        });
    }

    // Refresh table method
    public void refreshTable() {
        table1.setModel(Main.loadContacts());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ContactsApp app = new ContactsApp();
            app.setVisible(true);
        });
    }
}
