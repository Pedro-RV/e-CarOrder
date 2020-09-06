package com.example.e_carorder.chargePointInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.e_carorder.R;
import com.example.e_carorder.chats.MessageActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class userInfoActivity extends AppCompatActivity {

    private ImageView userSelectedImage;
    private TextView userSelectedUsername;
    private Button openChatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent data = getIntent();
        final String userUsingConnectorId = data.getStringExtra("userUsingConnectorId");
        String usernameUsingConnector = data.getStringExtra("usernameUsingConnector");

        userSelectedImage = findViewById(R.id.userSelectedImage);
        userSelectedUsername = findViewById(R.id.userSelectedUsername);
        openChatBtn = findViewById(R.id.openChatBtn);

        userSelectedUsername.setText(usernameUsingConnector);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+userUsingConnectorId+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(userSelectedImage);
            }
        });

        openChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MessageActivity.class);
                i.putExtra("userId", userUsingConnectorId);
                startActivity(i);
            }
        });

    }
}