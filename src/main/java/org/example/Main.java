package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Main {

    private static final String url = "jdbc:mysql://localhost:3306/contact_manager";
    private static final String user = "root";
    private static final String password = "MAZASQL";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI.ContactsApp app = new GUI.ContactsApp();
            app.setVisible(true);
        });
    }

    // Add a new contact
    public static void addContacts(Contacts contact) {
        String sql = "INSERT INTO contacts (name, phone_number, email_address, group_name, notes) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.name);
            pstmt.setString(2, contact.phone);
            pstmt.setString(3, contact.email);
            pstmt.setString(4, contact.group);
            pstmt.setString(5, contact.note);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Edit existing contact
    public static void editContacts(Contacts contact) {
        String sql = "UPDATE contacts SET name = ?, phone_number = ?, email_address = ?, group_name = ?, notes = ? WHERE contact_id = ?;";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.name);
            pstmt.setString(2, contact.phone);
            pstmt.setString(3, contact.email);
            pstmt.setString(4, contact.group);
            pstmt.setString(5, contact.note);
            pstmt.setInt(6, contact.contactId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Delete a contact
    public static void deleteContacts(Contacts contact) {
        String sql = "DELETE FROM contacts WHERE contact_id = ?;";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contact.contactId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Load all contacts into a table model
    public static DefaultTableModel loadContacts() {
        String sql = "SELECT * FROM contacts";
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Phone", "Email", "Group", "Note"}, 0);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("contact_id");
                String name = rs.getString("name");
                String phone = rs.getString("phone_number");
                String email = rs.getString("email_address");
                String group = rs.getString("group_name");
                String note = rs.getString("notes");

                model.addRow(new Object[]{id, name, phone, email, group, note});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error loading contacts: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return model;
    }
}
