package com.example.w_people.models;

public class User {
    private String uid; // Firebase user ID
    private String username;
    private String fullName;
    private String bio;
    private String email; // Assuming you have an email
    private String profileImage; // Assuming this is a URL to the profile image

    // Default constructor
    public User() {
        // Empty constructor needed for Firestore
    }

    // Constructor with all fields
    public User(String uid, String username, String fullName, String bio, String email, String profileImage) {
        this.uid = uid;
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.email = email;
        this.profileImage = profileImage;
    }

    // Getter methods
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    // Setter methods (if needed)
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
