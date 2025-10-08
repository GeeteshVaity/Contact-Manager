package org.example;

//import files
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;



public class Main {

    // Defining  the url,user and password
    private static final String url = "jdbc:mysql://localhost:3306/contact_manager";
    private static final String user = "root";
    private static final String password = "MAZASQL";

    //Main Function
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI.SplashScreen app = new GUI.SplashScreen();
            app.setVisible(true);
        });
    }

    // to load groups name
    public static Map<String, Integer> loadGroups() {
        Map<String, Integer> groups = new HashMap<>();
        String sql = "SELECT group_id, group_name FROM contact_groups ORDER BY group_name";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                groups.put(rs.getString("group_name"), rs.getInt("group_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return groups;
    }

    public static void addContacts(Contacts contact) {
        String sql = "INSERT INTO contacts (name, phone_number, email_address, group_id, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.name);
            pstmt.setString(2, contact.phone);
            pstmt.setString(3, contact.email);
            pstmt.setInt(4, contact.group_id); // Use groupId
            pstmt.setString(5, contact.note);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Update with a group_id.
    public static void editContacts(Contacts contact) {
        String sql = "UPDATE contacts SET name = ?, phone_number = ?, email_address = ?, group_id = ?, notes = ? WHERE contact_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contact.name);
            pstmt.setString(2, contact.phone);
            pstmt.setString(3, contact.email);
            pstmt.setInt(4, contact.group_id); // Use groupId
            pstmt.setString(5, contact.note);
            pstmt.setInt(6, contact.contactId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Delete contact
    public static void deleteContacts(Contacts contact) {
        String sql = "DELETE FROM contacts WHERE contact_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contact.contactId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // --- DATA LOADING AND SORTING ---
    public static DefaultTableModel loadContacts() {
        String sql = "SELECT c.contact_id, c.name, c.phone_number, c.email_address, g.group_name, c.notes FROM contacts as c JOIN contact_groups as g ON c.group_id = g.group_id";
        return createTableModelFromSQL(sql);
    }


    public static DefaultTableModel sortByName() {
        String sql = "SELECT c.contact_id, c.name, c.phone_number, c.email_address, g.group_name, c.notes FROM contacts as c JOIN contact_groups as g ON c.group_id = g.group_id ORDER BY c.name ASC";
        return createTableModelFromSQL(sql);
    }

    public static DefaultTableModel sortByGroup() {
        String sql = "SELECT c.contact_id, c.name, c.phone_number, c.email_address, g.group_name, c.notes FROM contacts as c JOIN contact_groups as g ON c.group_id = g.group_id ORDER BY g.group_name ASC, c.name ASC";
        return createTableModelFromSQL(sql);
    }


    private static DefaultTableModel createTableModelFromSQL(String sql) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{ "Id","Name", "Phone", "Email", "Group", "Note"}, 0);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("contact_id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email_address"),
                        rs.getString("group_name"),
                        rs.getString("notes")
                });
            }
        } catch (SQLException e) {
          throw  new RuntimeException(e);
        }
        return model;
    }
}