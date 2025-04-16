package com.example.w_people.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_people.R;
import com.example.w_people.adapters.MessageAdapter;
import com.example.w_people.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageView sendButton;

    private DatabaseReference databaseReference;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    private String senderId;
    private String receiverId;
    private String receiverUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize the UI elements
        recyclerView = findViewById(R.id.recyclerViewMessages);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Get receiver details from the Intent
        receiverId = getIntent().getStringExtra("receiverId");
        receiverUsername = getIntent().getStringExtra("receiverUsername");

        // Log to confirm we received the values correctly
        Log.d("ChatActivity", "Receiver ID: " + receiverId);
        Log.d("ChatActivity", "Receiver Username: " + receiverUsername);

        // Check if receiverId is null
        if (receiverId == null) {
            Toast.makeText(this, "Receiver ID is missing!", Toast.LENGTH_SHORT).show();
            finish();  // Exit if receiverId is missing
            return;
        }

        // Initialize the message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);

        // Set the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Get current user ID (FirebaseAuth)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            senderId = currentUser.getUid();  // Use Firebase user ID as the senderId
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();  // Exit if user is not logged in
            return;
        }

        // Set Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");

        // Load previous messages between sender and receiver
        loadMessages();

        // Send message button action
        sendButton.setOnClickListener(v -> sendMessage());
    }

    // Load previous messages from Firebase Realtime Database
    private void loadMessages() {
        databaseReference.child(senderId).child(receiverId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            if (message != null) {
                                messageList.add(message);
                            }
                        }
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Send a message to Firebase Realtime Database
    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();

        if (messageText.isEmpty()) {
            return;  // Don't send empty messages
        }

        // Create a new message object
        Message message = new Message(senderId, receiverId, messageText, System.currentTimeMillis(), receiverUsername);

        // Save the message to the Firebase database (for both sender and receiver)
        databaseReference.child(senderId).child(receiverId).push().setValue(message);
        databaseReference.child(receiverId).child(senderId).push().setValue(message);

        // Clear the message input field
        messageEditText.setText("");
    }
}
