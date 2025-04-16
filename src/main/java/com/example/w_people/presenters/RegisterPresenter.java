package com.example.w_people.presenters;

import androidx.annotation.NonNull;

import com.example.w_people.models.User;
import com.example.w_people.views.IRegisterView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPresenter {

    private final IRegisterView view;
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public RegisterPresenter(IRegisterView view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void registerUser(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            view.onRegisterFailure("Please fill in all fields");
            return;
        }

        view.showProgress();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideProgress();
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();
                            saveUserToFirestore(uid, name, email);
                        }
                    } else {
                        view.onRegisterFailure("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    private void saveUserToFirestore(String uid, String name, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("imageUrl", ""); // empty for now
        user.put("timestamp", System.currentTimeMillis());

        firestore.collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener(unused -> view.onRegisterSuccess())
                .addOnFailureListener(e -> view.onRegisterFailure("Failed to save user: " + e.getMessage()));
    }
}
