package com.example.w_people.models;

public class Message {
    private String senderId;
    private String receiverId;
    private String message;
    private long timestamp;
    private String senderUsername;

    // Default constructor required for Firebase
    public Message() {}

    public Message(String senderId, String receiverId, String message, long timestamp, String senderUsername) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
        this.senderUsername = senderUsername;
    }

    // Getter and Setter methods
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
}
