package com.example.e_carorder.chats;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_carorder.R;
import com.example.e_carorder.chats.messagesRecyclerView.ChatModel;
import com.example.e_carorder.chats.messagesRecyclerView.MessageAdapter;
import com.example.e_carorder.chats.usersRecyclerView.UserAdapter;
import com.example.e_carorder.chats.usersRecyclerView.UserModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatsFragment extends Fragment {

    private Button allUsersBtn;

    private UserAdapter userAdapter;
    private ArrayList<UserModel> mUsers;

    private FirebaseUser fUser;
    private DatabaseReference databaseReference;

    private ArrayList<String> usersIdList;

    private RecyclerView recyclerViewUserLastChats;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        allUsersBtn = view.findViewById(R.id.allUsersBtn);

        recyclerViewUserLastChats = view.findViewById(R.id.rvUserLastChats);

        allUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AllUsersChatsActivity.class);
                startActivity(i);
            }
        });

        recyclerViewUserLastChats.setLayoutManager(new LinearLayoutManager(getContext()));

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        usersIdList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersIdList.clear();

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String userId = snapshot.child("id").getValue().toString();

                        usersIdList.add(userId);
                    }

                    chatList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void chatList(){

        // Getting all recent chats
        mUsers = new ArrayList<>();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("users");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mUsers.clear();

                if(e == null){
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        if(documentSnapshot.exists()){
                            String userId = documentSnapshot.getId();

                            UserModel user = new UserModel(documentSnapshot.getId(), documentSnapshot.getString("username"), R.mipmap.ic_launcher);

                            for(String id: usersIdList){
                                if(userId.equals(id)){
                                    mUsers.add(user);
                                }
                            }
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers);
                    recyclerViewUserLastChats.setAdapter(userAdapter);
                }
            }
        });


    }

    // Check user status to see if he is receiving chat messages
    private void CheckStatus(String status){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference documentReference = FirebaseFirestore.getInstance()
                    .collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            Map<String, Object> mapStatus = new HashMap<>();
            mapStatus.put("status", status);

            documentReference.update(mapStatus);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        CheckStatus("offline");
    }
}