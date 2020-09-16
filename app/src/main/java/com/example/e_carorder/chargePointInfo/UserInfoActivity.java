package com.example.e_carorder.chargePointInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_carorder.R;
import com.example.e_carorder.chats.MessageActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserInfoActivity extends AppCompatActivity {

    private ImageView userSelectedImage;
    private TextView userSelectedUsername, userInfo_carModel, userInfo_description;
    private Button openChatBtn;
    private FloatingActionButton backUserInfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent data = getIntent();
        final String userId = data.getStringExtra("userId");
        String username = data.getStringExtra("username");
        String carModel = data.getStringExtra("carModel");
        String description = data.getStringExtra("description");

        userSelectedImage = findViewById(R.id.userSelectedImage);
        userSelectedUsername = findViewById(R.id.userSelectedUsername);
        userInfo_carModel = findViewById(R.id.userInfo_carModel);
        userInfo_description = findViewById(R.id.userInfo_description);
        openChatBtn = findViewById(R.id.openChatBtn);
        backUserInfoBtn = findViewById(R.id.backUserInfoBtn);

        userSelectedUsername.setText(username);
        userInfo_carModel.setText(carModel);
        userInfo_description.setText(description);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+userId+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(userSelectedImage);
            }
        });

        if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            openChatBtn.setVisibility(View.GONE);

        }

        openChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MessageActivity.class);
                i.putExtra("userId", userId);
                startActivity(i);
            }
        });

        backUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.super.onBackPressed();
            }
        });

    }
}