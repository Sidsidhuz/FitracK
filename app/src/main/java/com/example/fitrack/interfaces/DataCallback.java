package com.example.fitrack.interfaces;

public interface DataCallback<T> {
    void onSuccess(T data);

    void onError(String message);
}

