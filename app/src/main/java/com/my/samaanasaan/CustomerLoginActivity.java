package com.my.samaanasaan;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class CustomerLoginActivity extends AppCompatActivity {

  String codeSended; //////====== it saves that code which will send to user
  EditText etPhone; //////=== Edit Text in which user enter mobile number
  Button signIn; //////======== Button which is used to intiat verification process
  TextView signUp;//////=========== This TextView act as Button and take user to registration actiivty

  CountryCodePicker codePicker;





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_login);





    signIn=findViewById(R.id.SignIn);
    codePicker=findViewById(R.id.ccp);
    etPhone=findViewById(R.id.phone_number_edt);
    signUp=findViewById(R.id.signUp);

    signUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent goToRegistration=new Intent(CustomerLoginActivity.this,RegistrationActivity.class);
        startActivity(goToRegistration);
      }
    });

    signIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        String code = codePicker.getSelectedCountryCode().toString();


        String number=etPhone.getText().toString();

        String phoneNumber;

        if (number!=null) {
          phoneNumber = "+"+code + number;
        }else
        {
          etPhone.setError("Please Enter Number");
          etPhone.requestFocus();
          return;
        }
        Toast.makeText(CustomerLoginActivity.this, phoneNumber, Toast.LENGTH_LONG).show();


        //sendVerificatonCode(phoneNumber);
        Intent goToVarifyCode = new Intent(CustomerLoginActivity.this,VerifyNumberActivity.class);
       goToVarifyCode.putExtra("PhoneNumber", phoneNumber);
        startActivity(goToVarifyCode);

        //goToVarifyCode.putExtra("Code", codeSended);
       // startActivity(goToVarifyCode);


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
      Toast.makeText( CustomerLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
      super.onCodeSent(s, forceResendingToken);
      codeSended=s;
      Toast.makeText(CustomerLoginActivity.this,"Code Sent :"+codeSended.toString(),Toast.LENGTH_SHORT).show();
    }
  };

////////
}
