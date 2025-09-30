package GUI;

import org.example.Contacts;
import org.example.Main;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ContactForm extends JFrame {
    private JPanel root;
    private JPanel titlePanel;
    private JLabel titleLabel;
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
    private final ContactsApp parentApp;
    private Contacts contactToEdit; // If not null, we are in "Edit Mode"
    private final Map<String, Integer> groupMap = new HashMap<>(); // To store GroupName -> GroupID

    // Constructor for ADDING a new contact
    public ContactForm(ContactsApp parentApp) {
        this.parentApp = parentApp;
        this.contactToEdit = null; // Ensure we are in "Add Mode"
        setupFrame("Add New Contact");
        loadGroupsIntoComboBox();
        configureButtons();
    }

    // Constructor for EDITING an existing contact
    public ContactForm(ContactsApp parentApp, Contacts contactToEdit) {
        this.parentApp = parentApp;
        this.contactToEdit = contactToEdit; // Set the contact to edit
        setupFrame("Edit Contact");
        loadGroupsIntoComboBox();
        populateFields();
        configureButtons();
    }

    private void setupFrame(String title) {
        setTitle(title);
        setContentPane(root);
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void loadGroupsIntoComboBox() {
        comboBox1.removeAllItems();
        groupMap.clear();

        Map<String, Integer> loadedGroups = Main.loadGroups();
        for (Map.Entry<String, Integer> entry : loadedGroups.entrySet()) {
            String groupName = entry.getKey();
            Integer groupId = entry.getValue();
            comboBox1.addItem(groupName);
            groupMap.put(groupName, groupId);
        }
    }

    private void populateFields() {
        if (contactToEdit != null) {
            nameField.setText(contactToEdit.name);
            phoneField.setText(contactToEdit.phone);
            emailField.setText(contactToEdit.email);
            textArea1.setText(contactToEdit.note);
            if (contactToEdit.groupName != null) {
                comboBox1.setSelectedItem(contactToEdit.groupName);
            }
        }
    }

    private void configureButtons() {
        // Remove existing listeners to prevent multiple fires
        for (var al : saveButton.getActionListeners()) {
            saveButton.removeActionListener(al);
        }
        for (var al : cancelButton.getActionListeners()) {
            cancelButton.removeActionListener(al);
        }

        saveButton.addActionListener(e -> saveContact());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedGroupName = (String) comboBox1.getSelectedItem();
        int group_id = groupMap.getOrDefault(selectedGroupName, -1); // Get the ID for the selected group name

        if (contactToEdit == null) { // ADD MODE
            Contacts newContact = new Contacts(name, phone, emailField.getText().trim(), group_id, textArea1.getText().trim());
            Main.addContacts(newContact);
            JOptionPane.showMessageDialog(this, "Contact saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else { // EDIT MODE
            contactToEdit.name = name;
            contactToEdit.phone = phone;
            contactToEdit.email = emailField.getText().trim();
            contactToEdit.group_id = group_id;
            contactToEdit.note = textArea1.getText().trim();
            Main.editContacts(contactToEdit);
            JOptionPane.showMessageDialog(this, "Contact updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        parentApp.refreshTable();
        dispose();
    }
}