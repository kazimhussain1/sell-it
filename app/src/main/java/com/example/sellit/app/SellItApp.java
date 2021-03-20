package com.example.sellit.app;

import android.app.Application;

import com.example.sellit.common.ContextService;

public class SellItApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextService contextService = ContextService.getInstance();
        contextService.setContext(this);
    }


}
