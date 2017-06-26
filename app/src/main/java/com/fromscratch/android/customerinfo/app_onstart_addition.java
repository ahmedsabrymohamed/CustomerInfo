package com.fromscratch.android.customerinfo;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by moon on 6/25/2017.
 */

public class app_onstart_addition extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
