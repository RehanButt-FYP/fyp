package com.my.samaanasaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.my.samaanasaan.model.DataAdapter;
import com.my.samaanasaan.model.VehicleCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.my.samaanasaan.CustomerMapsActivity.MY_PREFS_NAME;

public class SelectVehicleActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private RecyclerView seggestedVehicleRecycler;
    private DataAdapter mVehicleAdapter;
    private List<VehicleCategory> mDataList;
    float Distance;
    float wieght;
    float volum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);
        Intent getData=getIntent();
     //   volum=getIntent().getFloatExtra("Volume",0);
        Distance= getData.getFloatExtra("Distance", 0);
        wieght=getData.getFloatExtra("Weight",0);
        SharedPreferences prefs = SelectVehicleActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        volum =prefs.getFloat("Volume", 0);

        // volum=getData.getFloatExtra("Volume",0);
        mDataList = new ArrayList<>();
        seggestedVehicleRecycler=findViewById(R.id.suggested_vehicle);

        seggestedVehicleRecycler.setLayoutManager(new LinearLayoutManager(this));

        mVehicleAdapter=new DataAdapter(this,mDataList);
        seggestedVehicleRecycler.setAdapter(mVehicleAdapter);
        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("VehicleCategory");

        readData();


    }

    public void readData()
    {

Toast.makeText(SelectVehicleActivity.this, String.valueOf(volum), Toast.LENGTH_SHORT).show();
        Query query1=mRef.orderByChild("Volume").startAt(volum-5).endAt(volum+5);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Map<String,Object> data=(Map<String, Object>) snapshot.getValue();
                    VehicleCategory vehicleCategory = new VehicleCategory(Float.parseFloat(data.get("Capacity").toString())
                            ,Double.parseDouble(data.get("PerKMCharge").toString())
                            ,Double.parseDouble(data.get("BaseFair").toString())
                            ,data.get("CategoryName").toString()
                            , Double.parseDouble(data.get("PerMinuteCharge").toString())
                            ,Double.parseDouble(data.get("Volume").toString()));
                    vehicleCategory.setCategoryId(dataSnapshot.getKey());
                    mDataList.add(vehicleCategory);
                    mVehicleAdapter.notifyDataSetChanged();
//                    Toast.makeText(SelectVehicleActivity.this, "Data is"+data.get("vehicleName").toString(),Toast.LENGTH_SHORT ).show();
                    //Log.d("What is this",data.get("Name").toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent goBack =new Intent(SelectVehicleActivity.this,LuggageEstimateActivity.class);
        goBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goBack);

    }
}
