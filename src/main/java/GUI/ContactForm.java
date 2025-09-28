package GUI;

import org.example.Contacts;
import org.example.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ContactForm extends JFrame {
    private JPanel root;
    private JPanel title;
    private JPanel content;
    JTextField nameField;
    JTextField phoneField;
    JTextField emailField;
    JComboBox<String> comboBox1;
    JTextArea textArea1;
    private JPanel buttonsPanel;
    private JButton cancelButton;
    JButton saveButton;
    private JLabel name;
    private JLabel phone;
    private JLabel email;
    private JLabel group;
    private JLabel note;

    private ContactsApp parentApp; // reference to refresh table

    // Constructor for adding new contact
    public ContactForm(ContactsApp parentApp) {
        this.parentApp = parentApp;

        setTitle("Contact Form");
        setContentPane(root);
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add groups to comboBox
        comboBox1.addItem("Family");
        comboBox1.addItem("Friends");
        comboBox1.addItem("Work");
        comboBox1.addItem("Others");

        // Save button action
        saveButton.addActionListener(e -> saveContact());

        // Cancel button action
        cancelButton.addActionListener((ActionEvent e) -> clearFields());
    }

    // Constructor for editing an existing contact (optional)
    public ContactForm() {
        this(null); // call main constructor without parent
    }

    // Save contact method
    private void saveContact() {
        String contactName = nameField.getText().trim();
        String contactPhone = phoneField.getText().trim();
        String contactEmail = emailField.getText().trim();
        String contactGroup = (String) comboBox1.getSelectedItem();
        String contactNote = textArea1.getText().trim();

        if (contactName.isEmpty() || contactPhone.isEmpty()) {
            JOptionPane.showMessageDialog(root,
                    "Name and Phone are required!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create Contacts object
        Contacts contact = new Contacts(contactName, contactPhone, contactEmail, contactGroup, contactNote);

        // Add to DB
        Main.addContacts(contact);
        JOptionPane.showMessageDialog(root, "Contact saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Refresh table if parent exists
        if (parentApp != null) {
            parentApp.refreshTable();
        }

        // Close form
        dispose();
    }

    // Clear all fields
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        comboBox1.setSelectedIndex(0);
        textArea1.setText("");
    }
}
