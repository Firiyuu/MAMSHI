package com.example.espina.momshie;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ESPINA on 9/30/2017.
 */

public class FireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        if (FirebaseApp.getApps(this).isEmpty()){

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        }
    }
}
