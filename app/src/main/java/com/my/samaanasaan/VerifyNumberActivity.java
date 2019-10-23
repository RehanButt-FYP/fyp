package com.my.samaanasaan;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyNumberActivity extends AppCompatActivity {



    String codeSent ,codeSended="0";//////====== it saves that code which will send to user
    private ProgressDialog loadingBar;//========This loading bar is used when checking the entered code is correct or not
    Button signIn;//====== This Button is used to intiate the process of Number verification if code is correct, it takes user to next activity
    TextView changeNumber;//=================This Text view is clickable and used to change the Phone  Number
   // String Enteredcode;//This saves that code which is recieved via sms
    String phoneNumber;

    FirebaseAuth mAuth;  //////=======Firebase Authentication instance

    FirebaseDatabase mFireBase;
    DatabaseReference mRefrence;
    ///////////=============Edit Text That contain code===============///////////
     EditText c1;
    EditText c2;
    EditText c3;
    EditText c4;
    EditText c5;
    EditText c6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFireBase=FirebaseDatabase.getInstance();
        mRefrence=mFireBase.getReference("user");
        setContentView(R.layout.activity_verify_number);

       loadingBar=new ProgressDialog(this);

        viewIntialize();
         phoneNumber=getIntent().getStringExtra("PhoneNumber");
         //codeSent=getIntent().getStringExtra("Code");
        //Toast.makeText(VerifyNumberActivity.this, codeSent, Toast.LENGTH_LONG).show();
        sendVerificatonCode(phoneNumber);
        mAuth = FirebaseAuth.getInstance();




        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loadingBar.setTitle("Logging in...");
                loadingBar.setMessage("Please wait for a mement this might take a few seconds");
                loadingBar.show();
                verifyCode();
                String Key=mRefrence.push().getKey();
                Map<String,Object> newUser=new HashMap<>();
                newUser.put("phone", phoneNumber);
                newUser.put("uid", codeSended);
                 mRefrence.child(Key).setValue(newUser);
                Intent home=new Intent(VerifyNumberActivity.this,CustomerHomeNavActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);

            }
        });


        /////////////=====================On click Listener for Text view for changing the Number============/////////////

        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToCustomerLogin=new Intent(VerifyNumberActivity.this,CustomerLoginActivity.class);
                startActivity(goToCustomerLogin);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }



    private void verifyCode(){
      String  Enteredcode="0";
              Enteredcode=c1.getText().toString()+c2.getText().toString()+c3.getText().toString()+c4.getText().toString()+c5.getText().toString()+c6.getText().toString();
            if (Enteredcode.isEmpty())
            {
                Toast.makeText(this, "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                return;
            }
            else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSended, Enteredcode);
                signInWithCredential(credential);
            }
    }



    private void signInWithCredential(PhoneAuthCredential credential) {


        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Intent home=new Intent(VerifyNumberActivity.this,CustomerHomeNavActivity.class);
                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter Wrong Code" ,Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });


    }




    private void sendVerificatonCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack);
        // Toast.makeText(this, "Code Sent", Toast.LENGTH_SHORT).show();
    }

    ///////////

    /////////=====================For mCallBackMethod=========================////////////////////

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //codeSended = phoneAuthCredential.getSmsCode().trim().split(" ")[0];;
            if (codeSended != null) {
                //       c1.setText(code.charAt(0));
                //   c2.setText(code.charAt(1));
                //c3.setText(code.charAt(2));
                //c4.setText(code.charAt(3));
                //c5.setText(code.charAt(4));
                //c6.setText(code.charAt(5));
                //      verifyCode(code);
            }
        }

        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText( VerifyNumberActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
             codeSended=s;

            Toast.makeText(VerifyNumberActivity.this,"Code Sent :"+codeSended.toString(),Toast.LENGTH_SHORT).show();
        }
    };



///////======================Initialize data=================/////

    private void viewIntialize()
    {

        changeNumber = findViewById(R.id.ChangeNumber);

        signIn=findViewById(R.id.btnSignIn);
        ///////////////////

        c1=findViewById(R.id.c1);
        c2=findViewById(R.id.c2);
        c3=findViewById(R.id.c3);
        c4=findViewById(R.id.c4);
        c5=findViewById(R.id.c5);
        c6=findViewById(R.id.c6);


    }

}
