package com.example.cake;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseMessagingService  extends FirebaseInstanceIdService {

    String refreshedToken = FirebaseInstanceId.getInstance().getToken();

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // Instance ID token to your app server.
    //sendRegistrationToServer(refreshedToken);
}
