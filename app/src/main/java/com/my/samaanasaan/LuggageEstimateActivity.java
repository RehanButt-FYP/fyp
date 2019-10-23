package com.my.samaanasaan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.core.GeoHashQuery;
import com.my.samaanasaan.model.DataAdapter;
import com.my.samaanasaan.model.VehicleCategory;

import static com.my.samaanasaan.CustomerMapsActivity.MY_PREFS_NAME;

public class LuggageEstimateActivity extends AppCompatActivity {

    Button btnNext;
    Button calcVol;
    EditText length;// user interface
    EditText width;// user interface
    EditText height;// user interface
    TextView Volume;// user interface
    EditText Wheight;// user interface

    float Lenth;// save lenth which user enter thron ui
    float Width;// save width which user enter thron ui
    float Height;// save height which user enter thron ui
    float volume;// save volume which calculated from
    Double weight;
    String Distance;
    String Duration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luggage_estimate);

       initialize();



        Distance= getIntent().getStringExtra("Distance");
        Duration=getIntent().getStringExtra("Duration");
calcVol.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (length.getText().toString().isEmpty())
      {
          return;
      }
      if (width.getText().toString().isEmpty())
      {
          return;
      }
      if (height.getText().toString().isEmpty())
      {
          return;
      }
        else {



           Lenth=Float.parseFloat(length.getText().toString());
           Width=Float.parseFloat(width.getText().toString());
           Height=Float.parseFloat(height.getText().toString());
          if (Lenth<0)
          {
              length.setError("Enter Length");
              return;
          }
          if (Width<0)
          {
              length.setError("Enter Wdith");
              return;
          }
          if (Height<0)
          {
              length.setError("Enter Height");
              return;
          }
          else {

              volume = Lenth * Width * Height;
              Volume.setText(String.valueOf(volume));
          }
      }
    }
});

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToSelctVehicle=new Intent(LuggageEstimateActivity.this,SelectVehicleActivity.class);
                if(TextUtils.isEmpty(Wheight.getText()))
                {
                   Wheight.setError("Please Enter Wieght");
                   return;
                }
                else
                {
                    weight=Double.parseDouble(Wheight.getText().toString());

                }

                /*Intent sendDatatoJavaClass=new Intent(LuggageEstimateActivity.this, DataAdapter.class);
                sendDatatoJavaClass.putExtra("Distance", Distance);
                */
                goToSelctVehicle.putExtra("Distance", Distance);
                Toast.makeText(LuggageEstimateActivity.this, String.valueOf(volume), Toast.LENGTH_SHORT).show();
                //goToSelctVehicle.putExtra();
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putFloat("Volume", volume);
                editor.apply();
                //goToSelctVehicle.putExtra("Volume", volume);
                goToSelctVehicle.putExtra("Weight", weight);
                startActivity(goToSelctVehicle);
            }
        });
    }

    private void initialize()
    {
        btnNext=(Button)findViewById(R.id.btnNext);
        calcVol=(Button)findViewById(R.id.btnCalcVol);
        length=(EditText)findViewById(R.id.lth);
        width=(EditText)findViewById(R.id.wth);
        height=(EditText)findViewById(R.id.height);
        Volume=(TextView)findViewById(R.id.Vol);
        Wheight=(EditText) findViewById(R.id.weight);
        }
}
