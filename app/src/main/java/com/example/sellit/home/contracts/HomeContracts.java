package com.example.sellit.home.contracts;

import com.example.sellit.home.api.response.ClassifiedResponse;

import org.jetbrains.annotations.NotNull;

public interface HomeContracts {

    interface ViewModel{
        void onClassifiedSuccess(@NotNull ClassifiedResponse response);
        void onClassifiedError(@NotNull String error);

        void onClassifiedPaginatedSuccess(@NotNull ClassifiedResponse response);
        void onClassifiedPaginatedError(@NotNull String error);
    }
}
