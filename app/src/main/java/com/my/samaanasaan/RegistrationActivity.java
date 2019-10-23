package com.my.samaanasaan;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.my.samaanasaan.model.User;

import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseDatabase mFireBase;
    DatabaseReference mRefrence;
    FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private ImageView userImage;
    private  EditText etName;
    private EditText etUserName;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etCnic;
    private Button btnSignUp;
    private Button btnCancel;
    int Image_Request_Code = 7;////Image request code
    Uri FilePathUri;/// To store the path of image
/////////////////////////
     String Name;
    String userName;
    String userPwd;
    String userPhone;
    String userCnic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mFireBase=FirebaseDatabase.getInstance();
        mRefrence=mFireBase.getReference();
        mAuth=FirebaseAuth.getInstance();

        //userImage=(ImageView)findViewById(R.id.UserImage);
        etName=(EditText)findViewById(R.id.EtName);
        etUserName=(EditText)findViewById(R.id.EtUserName);
        etPassword=(EditText)findViewById(R.id.EtPassword);
        etPhone=(EditText)findViewById(R.id.EtPhone);
        etCnic=(EditText)findViewById(R.id.EtCnic);
        btnCancel=(Button)findViewById(R.id.BtnCancel);
        btnSignUp=(Button)findViewById(R.id.BtnSignup);
        loadingBar=new ProgressDialog(this);
        /////======================Create User=================///////////////
        this.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Creating Your Accont...");
                loadingBar.setMessage("Please wait for a mement this might take a few seconds");
                loadingBar.show();
                 userName = etUserName.getText().toString();
                 userPwd = etPassword.getText().toString();

                    mAuth.createUserWithEmailAndPassword(userName, userPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful())
                         {

                             uploadUserData();
                             Toast.makeText(RegistrationActivity.this, "Your Account is Created Succesfully", Toast.LENGTH_LONG).show();
                             Intent goTOLogin=new Intent(RegistrationActivity.this,LoginActivity.class);
                             startActivity(goTOLogin);
                             loadingBar.dismiss();
                         }
                         else
                         {
                             Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                         }
                        }
                    });
                    

            }
        });


        ////////===============Go Back to Login================/////////////

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToLogin=new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(goToLogin);
            }
        });

        /////===============Fetch Image from gallery============================/////
/*
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });

*/
    }

////////=================Set Image to Image View in Activity ==============//////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                userImage.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /////=============This method upload user profile details to database================////////
    private void uploadUserData()
    {

        Name = etName.getText().toString();
        userPhone = etPhone.getText().toString();
        userCnic = etCnic.getText().toString();

        User newUser=new User(Name,userPhone,userCnic);
        mRefrence.child("Customer").child(mAuth.getUid()).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

              //  Toast.makeText(RegistrationActivity.this, "Profile Data Uploaded Succesfully", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(RegistrationActivity.this, "Something went Wrong in Data Upload. Try Again ..", Toast.LENGTH_LONG).show();

            }
        });

    }
    void checkvalid(){

    }
}
