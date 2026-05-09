package com.example.fitrack.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitrack.interfaces.ActionCallback;
import com.example.fitrack.repositories.AuthRepository;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository = new AuthRepository();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void login(String email, String password) {
        loading.setValue(true);
        authRepository.signIn(email, password, new ActionCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
                message.setValue("Login successful");
            }

            @Override
            public void onError(String errorMessage) {
                loading.setValue(false);
                message.setValue(errorMessage);
            }
        });
    }

    public void signup(String email, String password) {
        loading.setValue(true);
        authRepository.signUp(email, password, new ActionCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
                message.setValue("Account created");
            }

            @Override
            public void onError(String errorMessage) {
                loading.setValue(false);
                message.setValue(errorMessage);
            }
        });
    }

    public void resetPassword(String email) {
        loading.setValue(true);
        authRepository.sendPasswordReset(email, new ActionCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
                message.setValue("Reset email sent");
            }

            @Override
            public void onError(String errorMessage) {
                loading.setValue(false);
                message.setValue(errorMessage);
            }
        });
    }

    public void logout() {
        authRepository.logout();
    }
}

