package com.example.w_people.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.w_people.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private Uri imageUri;
    private StorageReference storageRef;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageRef = FirebaseStorage.getInstance().getReference();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        Glide.with(this).load(imageUri).circleCrop().into(binding.ivProfile);
                    }
                });

        binding.ivProfile.setOnClickListener(v -> pickImageFromGallery());
        binding.btnSave.setOnClickListener(v -> saveProfile());
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void saveProfile() {
        String fullName = binding.etName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String bio = binding.etBio.getText().toString().trim();

        if (fullName.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            StorageReference imgRef = storageRef.child("profiles/" + user.getUid() + ".jpg");
            imgRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        saveUserData(user.getUid(), fullName, username, bio, uri.toString());
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    });
        } else {
            saveUserData(user.getUid(), fullName, username, bio, null);
        }
    }

    private void saveUserData(String uid, String fullName, String username, String bio, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> profile = new HashMap<>();
        profile.put("fullName", fullName);
        profile.put("username", username);
        profile.put("bio", bio);
        if (imageUrl != null) profile.put("profileImage", imageUrl);

        db.collection("users").document(uid).set(profile)
                .addOnSuccessListener(unused -> {
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    prefs.edit().putBoolean("isProfileComplete", true).apply();

                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error saving: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
