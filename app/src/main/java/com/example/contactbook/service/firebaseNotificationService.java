package com.example.contactbook.service;

import androidx.annotation.NonNull;

import com.example.contactbook.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class firebaseNotificationService extends FirebaseMessagingService {

    private User user = new User();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        updateToken(s);
        super.onNewToken(s);
    }

    private void updateToken(String token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUserCode());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        databaseReference.updateChildren(map);
    }
}
