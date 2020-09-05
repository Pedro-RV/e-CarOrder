package com.example.e_carorder.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_carorder.R;
import com.example.e_carorder.profile.EditProfileActivity;
import com.example.e_carorder.signInSignUp.SignInSignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {
    public static final String TAG = "TAG";
    private TextView username, email;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    private Button resetPasswordLocal, changeProfile;
    private FirebaseUser user;
    private ImageView profileImage;
    private StorageReference storageReference;

    // Estos atributos para detectar la no verificacion del email
    private Button resendCode;
    private TextView verifyMsg;
    //

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        resetPasswordLocal = view.findViewById(R.id.resetPasswordLocal);

        profileImage = view.findViewById(R.id.profileImage);
        changeProfile = view.findViewById(R.id.changeProfile_btn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(profileImage);
            }
        });

        // Esto para detectar la no verificacion del email
        resendCode = view.findViewById(R.id.resendCode_btn);
        verifyMsg = view.findViewById(R.id.verifyMsg);
        //

        userID = fAuth.getCurrentUser().getUid();

        // Esto para detectar la no verificacion del email
        user = fAuth.getCurrentUser();

        if(!user.isEmailVerified()){
            resendCode.setVisibility(view.VISIBLE);
            verifyMsg.setVisibility(view.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }
        //



        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(e == null){
                    if(documentSnapshot.exists()){
                        username.setText(documentSnapshot.getString("username"));
                        email.setText(documentSnapshot.getString("email"));
                    }else {
                        Log.d(TAG, "onEvent: Document do not exists");
                    }
                }
            }
        });


        resetPasswordLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());
                resetPassword.setHint("Password");
                resetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                resetPassword.setBackgroundColor(Color.rgb(247,242,255));
                resetPassword.setTextSize(18);

                final EditText repeatResetPassword = new EditText(v.getContext());
                repeatResetPassword.setHint("Repeat password");
                repeatResetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                repeatResetPassword.setBackgroundColor(Color.rgb(247,242,255));
                repeatResetPassword.setTextSize(18);

                LinearLayout.LayoutParams params_resetPassword = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params_resetPassword.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params_resetPassword.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params_resetPassword.height = 112;

                resetPassword.setLayoutParams(params_resetPassword);

                LinearLayout.LayoutParams params_repeatResetPassword = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params_repeatResetPassword.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params_repeatResetPassword.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params_repeatResetPassword.height = 112;
                params_repeatResetPassword.topMargin = 50;

                repeatResetPassword.setLayoutParams(params_repeatResetPassword);

                LinearLayout layout = new LinearLayout(v.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(resetPassword);
                layout.addView(repeatResetPassword);

                final AlertDialog passwordChangeDialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Change Password ?")
                        .setMessage("Enter new password.")
                        .setView(layout)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Accept", null)
                        .create();


                passwordChangeDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(final DialogInterface dialog) {

                        Button positive = passwordChangeDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negative = passwordChangeDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                        positive.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // change password
                                String newPassword = resetPassword.getText().toString();
                                String newPasswordRepeated = repeatResetPassword.getText().toString();

                                if(newPassword.isEmpty() || newPasswordRepeated.isEmpty()){
                                    resetPassword.setError("Both fields must be filled.");

                                } else if(newPassword.equals(newPasswordRepeated)) {
                                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Password reset successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Password reset failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    dialog.dismiss();

                                } else{
                                        resetPassword.setError("Passwords do not match.");

                                }

                            }
                        });

                        negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                });

                passwordChangeDialog.show();

            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(), EditProfileActivity.class);
                i.putExtra("username", username.getText().toString());
                i.putExtra("email", email.getText().toString());
                startActivity(i);

                // Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // startActivityForResult(openGalleryIntent, 1000);
            }
        });
    }
}