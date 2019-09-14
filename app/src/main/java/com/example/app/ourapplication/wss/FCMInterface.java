package com.example.app.ourapplication.wss;


import com.google.firebase.messaging.RemoteMessage;

public interface FCMInterface {
    void onMessage(RemoteMessage remoteMessage);

}