package com.my.samaanasaan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

  FirebaseAuth mAuth;
  FirebaseUser firbaseUser;
  static public int delay = 3000;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mAuth=FirebaseAuth.getInstance();
    firbaseUser= mAuth.getCurrentUser();


    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if(firbaseUser!=null)
        {

          if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
          {

            statusCheck();

          }
          else
          {


            startActivity(new Intent(MainActivity.this,CustomerHomeNavActivity.class));
            finish();
          }


        }
        else
        {
          Intent homeIntent = new Intent(MainActivity.this, LoginActivity.class);
          startActivity(homeIntent);
        }

      }
    }, delay);

  }



  public void statusCheck() {
    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      startActivity(new Intent(MainActivity.this,CustomerHomeNavActivity.class));

    }
    else
    {
      startActivity(new Intent(MainActivity.this,CustomerMapsActivity.class));

    }
  }
}
