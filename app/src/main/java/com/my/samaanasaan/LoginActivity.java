package com.my.samaanasaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView creatNewAccont;
    Button signIn;
    EditText userName;
    EditText Password;

    FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        creatNewAccont=(TextView) findViewById(R.id.createAccount);
        userName=findViewById(R.id.userName);
        Password=findViewById(R.id.pass);
        signIn=findViewById(R.id.SignIn);
        loadingBar=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        creatNewAccont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToReg=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(goToReg);
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"Enter User Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (Password.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    loadingBar.setTitle("Logging in...");
                    loadingBar.setMessage("Please wait for a mement this might take a few seconds");
                    loadingBar.show();
                    String user=userName.getText().toString();
                    String pwd=Password.getText().toString();

                    mAuth.signInWithEmailAndPassword(user, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           Intent home=new Intent(LoginActivity.this,CustomerHomeNavActivity.class);
                           home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(home);
                           loadingBar.dismiss();
                       }
                       else
                       {
                           Toast.makeText(LoginActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                           return;

                       }
                        }
                    });
                }
            }
        });
    }

    public void onBackPressed(){

        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
    }
}
