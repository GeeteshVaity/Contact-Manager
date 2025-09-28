package org.example;

public class Contacts {
    public int contactId; // for edit/delete
    public String name;
    public String phone;
    public String email;
    public String group;
    public String note;

    // Constructor for new contact (no ID)
    public Contacts(String name, String phone, String email, String group, String note) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.group = group;
        this.note = note;
    }

    // Constructor for existing contact (with ID)
    public Contacts(int contactId, String name, String phone, String email, String group, String note) {
        this.contactId = contactId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.group = group;
        this.note = note;
    }
}
