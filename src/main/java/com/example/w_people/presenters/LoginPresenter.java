package com.example.w_people.presenters;

import android.text.TextUtils;

import com.example.w_people.views.ILoginView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter {
    private ILoginView loginView;
    private FirebaseAuth auth;

    public LoginPresenter(ILoginView view) {
        this.loginView = view;
        this.auth = FirebaseAuth.getInstance();
    }

    public void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            loginView.onLoginFailure("Email and password must not be empty.");
            return;
        }

        loginView.showProgress();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loginView.hideProgress();
                    if (task.isSuccessful()) {
                        loginView.onLoginSuccess();
                    } else {
                        loginView.onLoginFailure(task.getException().getMessage());
                    }
                });
    }
}
