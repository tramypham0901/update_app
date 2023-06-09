package com.example.contactbook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactbook.model.ChatMessage;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    TextView txtUserInfo;
    private FirebaseListAdapter<ChatMessage> adapter;
    private String messageReceiverId, messageReceiverName, messageSenderId;
    private String saveCurrentTime, saveCurrentDate;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    public static final String NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance("https://contact-book-chat-57229-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        init();
        displayChatMessages();
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            DatabaseReference userMessageKeyRef = rootRef.child("Messages")
                    .child(messageSenderId).child(messageReceiverId).push();
            String messageSenderRef = "Messages/" + messageSenderId + "/" + messageReceiverId;
            String messageReceiverRef = "Messages/" + messageReceiverId + "/" + messageSenderId;
            String messagePushId = userMessageKeyRef.getKey();
            EditText input = (EditText) findViewById(R.id.input);
            Map messageBodyDetails = new HashMap();
            if(input.getText().toString().trim().length() > 0) {
                ChatMessage message = new ChatMessage(messageSenderId, input.getText().toString(), "text", messageReceiverId, messagePushId, saveCurrentTime, saveCurrentDate, messageReceiverName);
                messageBodyDetails.put(messageSenderRef + "/" + messagePushId, message);
                messageBodyDetails.put(messageReceiverRef + "/" + messagePushId, message);
            }
            // Read the input field and push a new instance
            // of ChatMessage to the Firebase database
            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    // Clear the input
                    input.setText("");
                }
            });
        });
    }

    private void init() {
        messageSenderId = getIntent().getExtras().get("userCode").toString();
        if (getIntent().getExtras().get("visit_user_id") != null) {
            messageReceiverId = getIntent().getExtras().get("visit_user_id").toString();
            messageReceiverName = getIntent().getExtras().get("visit_user_name").toString();
        } else {
            messageReceiverId = "HS0001";
        }
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        txtUserInfo = (TextView) findViewById(R.id.txt_user_info);
        txtUserInfo.setTextColor(Color.BLUE);
        txtUserInfo.setText(messageReceiverName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private void displayChatMessages() {
        DatabaseReference roofRef = FirebaseDatabase.getInstance("https://contact-book-chat-57229-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        Query query = roofRef.child("Messages").child(messageSenderId).child(messageReceiverId);
        FirebaseListOptions<ChatMessage> options =
                new FirebaseListOptions.Builder<ChatMessage>()
                        .setQuery(query, ChatMessage.class)
                        .setLayout(R.layout.message)
                        .build();
        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageReceiver = (TextView) v.findViewById(R.id.receiver_message_text);

                if (messageSenderId.equals(model.getFrom())) {
                    messageReceiver.setVisibility(View.GONE);
                    messageUser.setVisibility(View.GONE);
                    messageText.setText(model.getMessage());

                } else {
                    messageText.setVisibility(View.GONE);
                    messageUser.setText(messageReceiverName);
                    messageReceiver.setText(model.getMessage());
                    messageTime.setText(model.getDate() + "-" + model.getTime());
                }
            }
        };
        listOfMessages.setAdapter(adapter);
    }

  /*  private  void getToken(String messageReceiverName, String messageReceiverId, String messageSenderId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(messageReceiverId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.child("token").getValue().toString();
                String name = snapshot.child("name").getValue().toString();

                JSONObject to = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    data.put("title", name);
                    data.put("message", messageReceiverName);
                    data.put("receiverID", messageReceiverId);

                    to.put("to", token);
                    to.put("data", data);

                    sendNotification(to);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, NOTIFICATION_URL, to, response -> {

            Log.d("TAG", "sendNotification: " + response);
        }, error -> {
            Log.d("TAG", "sendNotification: " + error);

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
    }*/
}