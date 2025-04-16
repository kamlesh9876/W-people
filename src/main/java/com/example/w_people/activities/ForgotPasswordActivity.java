package com.example.w_people.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.w_people.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnReset.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                binding.etEmail.setError("Email required");
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);

            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(unused -> {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}
