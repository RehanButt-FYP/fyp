package com.my.samaanasaan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class activity_vehicle_popup_detail extends AppCompatActivity {
    Dialog myDialog;
 TextView tvCatName;
 TextView tvCapacity;
 TextView tvVol;
 TextView tvBill;
 ConstraintLayout btnSelctVehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDialog=new Dialog(activity_vehicle_popup_detail.this);
        myDialog.setContentView(R.layout.activity_vehicle_popup_detail);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        intitialize();
        Intent getData=getIntent();
       tvCatName.setText(getData.getExtras().getString("CatName","Null"));
       tvVol.setText(getData.getExtras().getString("Vol", "0"));
       tvCapacity.setText(getData.getExtras().getString("Cap", "0"));
        tvBill.setText("Rs. "+getData.getExtras().getString("Bill", "0"));
        myDialog.show();
        myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
           @Override
           public void onCancel(DialogInterface dialog) {
               startActivity(new Intent(activity_vehicle_popup_detail.this,SelectVehicleActivity.class));
               overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
           }
       });

        btnSelctVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent goToConfirmBooking=new Intent(activity_vehicle_popup_detail.this,ConfirmBookingActivity.class);
               //goToConfirmBooking.
                startActivity(goToConfirmBooking);

            }
        });

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

        startActivity(new Intent(activity_vehicle_popup_detail.this,SelectVehicleActivity.class));
    }
    private void intitialize()
    {
        tvCatName=myDialog.findViewById(R.id.categoryName);
        tvCapacity=myDialog.findViewById(R.id.capacity);
        tvVol=myDialog.findViewById(R.id.volume);
        tvBill=myDialog.findViewById(R.id.bill);
        btnSelctVehicle=myDialog.findViewById(R.id.SelectVehicleButton);
    }
}
