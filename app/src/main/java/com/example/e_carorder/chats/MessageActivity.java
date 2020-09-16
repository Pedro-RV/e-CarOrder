package com.example.e_carorder.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_carorder.R;
import com.example.e_carorder.chats.messagesRecyclerView.ChatModel;
import com.example.e_carorder.chats.messagesRecyclerView.MessageAdapter;
import com.example.e_carorder.chats.usersRecyclerView.UserAdapter;
import com.example.e_carorder.chats.usersRecyclerView.UserModel;
import com.example.e_carorder.profile.EditProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private TextView nameUserMessage, onlineOffline;
    private ImageView imageUser;

    private RecyclerView recyclerViewMessage;
    private MessageAdapter messageAdapter;
    private ArrayList<ChatModel> mChats;
    private EditText etMessage;
    private ImageView sendMessageBtn;
    private FloatingActionButton backChatBtn;

    private FirebaseUser fUser;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private Intent intent;

    private String userToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        imageUser = findViewById(R.id.imageUser);
        nameUserMessage = findViewById(R.id.nameUserMessage);
        onlineOffline = findViewById(R.id.onlineOffline);

        sendMessageBtn = findViewById(R.id.sendMessage);
        backChatBtn = findViewById(R.id.backChatBtn);
        etMessage = findViewById(R.id.etMessage);

        recyclerViewMessage = findViewById(R.id.rvMessage);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewMessage.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        userToSend = intent.getStringExtra("userId");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+userToSend+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(imageUser);
            }
        });


        fUser = FirebaseAuth.getInstance().getCurrentUser();
        documentReference = FirebaseFirestore.getInstance().collection("users").document(userToSend);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null){
                    if(documentSnapshot.exists()){
                        UserModel user = new UserModel(userToSend, documentSnapshot.getString("username"));
                        nameUserMessage.setText(user.getName());

                        // Update user image to see in layout

                        readMessages(fUser.getUid(), userToSend, "image");

                        if(documentSnapshot.getString("status").equals("online")){
                            onlineOffline.setText("Online");
                        }else{
                            onlineOffline.setText("Offline");
                        }
                    }
                }
            }
        });


        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etMessage.getText().toString();

                if(!msg.equals("")){
                    sendMessage(fUser.getUid(), userToSend, msg);

                }else{
                    Toast.makeText(MessageActivity.this, "Please, send a message not empty.", Toast.LENGTH_SHORT).show();
                }

                etMessage.setText("");
            }
        });

        backChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.super.onBackPressed();
            }
        });
    }

    private  void sendMessage(String sender, String receiver, String message){
        DocumentReference reference = FirebaseFirestore.getInstance().collection("chats")
                .document(Long.toString(System.currentTimeMillis()) + sender);

        Map<String, Object> mapMessage = new HashMap<>();
        mapMessage.put("sender", sender);
        mapMessage.put("receiver", receiver);
        mapMessage.put("message", message);
        mapMessage.put("status", false);

        reference.set(mapMessage);

        // Adding User to chat fragment: Latest Chats with contacts

        // This one to display chats for sender
        final DatabaseReference databaseReferenceSender = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fUser.getUid())
                .child(userToSend);

        databaseReferenceSender.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    databaseReferenceSender.child("id").setValue(userToSend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // This one to display chats for receiver
        final DatabaseReference databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(userToSend)
                .child(fUser.getUid());

        databaseReferenceReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    databaseReferenceReceiver.child("id").setValue(fUser.getUid());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessages(final String myId, final String userId, String imageUrl){
        mChats = new ArrayList<>();

        collectionReference = FirebaseFirestore.getInstance().collection("chats");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mChats.clear();
                if(e == null){
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        if(documentSnapshot.exists()){
                            ChatModel chat = new ChatModel(documentSnapshot.getString("sender"),
                                    documentSnapshot.getString("receiver"), documentSnapshot.getString("message"), documentSnapshot.getBoolean("status"));

                            if(!documentSnapshot.getBoolean("status") && chat.getReceiver().equals(myId) && chat.getSender().equals(userId)){
                                String chatId = documentSnapshot.getId();
                                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("chats").document(chatId);

                                Map<String, Object> mapStatus = new HashMap<>();
                                mapStatus.put("status", true);

                                documentReference.update(mapStatus);

                                chat.setStatus(true);
                            }

                            if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                                    chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){

                                mChats.add(chat);

                            }

                            messageAdapter = new MessageAdapter(MessageActivity.this, mChats, Integer.toString(R.drawable.default_profile));
                            recyclerViewMessage.setAdapter(messageAdapter);
                        }
                    }
                }
            }
        });
    }



}