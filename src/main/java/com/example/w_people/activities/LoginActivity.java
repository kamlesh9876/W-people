package com.example.w_people.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.w_people.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Login button click
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            checkProfileCompletion(user.getUid());
                        }
                    })
                    .addOnFailureListener(e -> {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // Navigate to Register screen
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Navigate to Forgot Password screen
        binding.tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
    }

    private void checkProfileCompletion(String uid) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(document -> {
                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    boolean isComplete = document.exists() && document.getString("fullName") != null;
                    prefs.edit().putBoolean("isProfileComplete", isComplete).apply();

                    Intent intent = new Intent(this, isComplete ? HomeActivity.class : ProfileActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking profile", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                });
    }
}
