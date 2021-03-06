package com.example.e_carorder.chats;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.e_carorder.R;
import com.example.e_carorder.addChargePoint.connectorsRecyclerView.ConnectorAdapter;
import com.example.e_carorder.chats.usersRecyclerView.UserAdapter;
import com.example.e_carorder.chats.usersRecyclerView.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class AllUsersChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    private ArrayList<UserModel> mUsers;

    private FloatingActionButton backAllUsersChatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_chats);

        recyclerView = findViewById(R.id.rvAllUsers);
        backAllUsersChatBtn = findViewById(R.id.backAllUsersChatBtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();

        readUsers();

        backAllUsersChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllUsersChatsActivity.super.onBackPressed();
            }
        });
    }

    private void readUsers(){
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference reference = FirebaseFirestore.getInstance().collection("users");

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e == null){
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        if(documentSnapshot.exists()){
                            String name = documentSnapshot.getString("username");
                            String userId = documentSnapshot.getId();

                            if(!userId.equals(currentUser)){


                                mUsers.add(new UserModel(userId, name));
                            }

                            userAdapter = new UserAdapter(AllUsersChatsActivity.this, mUsers);
                            recyclerView.setAdapter(userAdapter);

                        }
                    }
                }
            }
        });

    }
}