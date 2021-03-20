package com.example.sellit.common;


import android.content.Context;

public class ContextService {

    private static final ContextService ourInstance = new ContextService();
    private Context context;

    public static ContextService getInstance() {

        return ourInstance;
    }

    private ContextService() {
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
