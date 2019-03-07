package com.floorists.grannyspillbox.classes;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private User emergencyContact;

    public User(){

    }
    public User(int id, String firstName, String lastName, String phone, String email, User emergencyContact) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.emergencyContact = emergencyContact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(User emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}
