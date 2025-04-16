package com.example.w_people.views;

public interface ILoginView {
    void onLoginSuccess();
    void onLoginFailure(String message);
    void showProgress();
    void hideProgress();
}
