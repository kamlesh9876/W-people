package com.example.w_people.views;

public interface IRegisterView {
    void onRegisterSuccess();
    void onRegisterFailure(String message);
    void showProgress();
    void hideProgress();
}
