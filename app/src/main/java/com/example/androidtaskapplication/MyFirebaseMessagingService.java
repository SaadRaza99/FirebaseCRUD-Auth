package com.example.androidtaskapplication;

import android.app.Service;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Remote message=","Remote message="+remoteMessage);
        // Handle incoming messages here
    }
}
