package com.example.w_people.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.w_people.R;
import com.example.w_people.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePreviewActivity extends AppCompatActivity {

    private ImageView imageProfile;
    private TextView textName, textEmail;
    private Button btnStartChat;

    private FirebaseFirestore db;
    private String userId; // The user being previewed
    private User selectedUser; // Will be passed to ChatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_preview);

        imageProfile = findViewById(R.id.imageProfile);
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        btnStartChat = findViewById(R.id.btnStartChat);

        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");

        if (userId == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserProfile();

        btnStartChat.setOnClickListener(v -> {
            if (selectedUser != null) {
                Intent intent = new Intent(ProfilePreviewActivity.this, ChatActivity.class);
                intent.putExtra("receiverId", selectedUser.getUid());
                intent.putExtra("receiverName", selectedUser.getFullName());
                intent.putExtra("receiverProfile", selectedUser.getProfileImage());
                startActivity(intent);
            }
        });
    }

    private void loadUserProfile() {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        selectedUser = documentSnapshot.toObject(User.class);
                        if (selectedUser != null) {
                            textName.setText(selectedUser.getFullName());
                            textEmail.setText(selectedUser.getEmail());
                            Glide.with(this)
                                    .load(selectedUser.getProfileImage())
                                    .placeholder(R.drawable.ic_user_avatar_placeholder)
                                    .into(imageProfile);
                        }
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading user", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
