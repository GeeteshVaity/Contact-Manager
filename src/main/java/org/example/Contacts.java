package org.example;

public class Contacts {
    public int contactId;
    public String name;
    public String phone;
    public String email;
    public int group_id; // CHANGED: To store the foreign key
    public String groupName; // To store the display name
    public String note;

    // For creating a NEW contact before saving
    public Contacts(String name, String phone, String email, int group_id, String note) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.group_id = group_id;
        this.note = note;
    }

    // For representing an EXISTING contact loaded from the database
    public Contacts(int contactId, String name, String phone, String email, String groupName, String note) {
        this.contactId = contactId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.groupName = groupName;
        this.note = note;
    }

    public Contacts(int group_id, String groupName) {
        this.group_id = group_id;
        this.groupName = groupName;
    }
}