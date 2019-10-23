package com.my.samaanasaan.model;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.my.samaanasaan.LuggageEstimateActivity;
import com.my.samaanasaan.R;
import com.my.samaanasaan.SelectVehicleActivity;
import com.my.samaanasaan.activity_vehicle_popup_detail;

import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.my.samaanasaan.CustomerMapsActivity.MY_PREFS_NAME;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    public DataAdapter(Context mContext, List<VehicleCategory> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    private Context mContext;
    private List<VehicleCategory> mDataList;

    int itemIndex=-1;
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(mContext).inflate(R.layout.list_items, parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final VehicleCategory vehicleCategory =mDataList.get(position);

        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        float distance =(Float.parseFloat(prefs.getString("Distance", "0")))/1000;
        float time = Float.parseFloat(prefs.getString("Duration", "0"))/60;
        final Double totalBill=vehicleCategory.getBaseFair()+(vehicleCategory.getPerKMCharge()*distance)+(vehicleCategory.getPerMinutCharge()*time);
        holder.tvVehicleName.setText(vehicleCategory.getCategoryName());
        holder.tvVehicleCapicity.setText(Float.toString(vehicleCategory.getCapacity()));
        holder.tvVehiclePrice.setText(Double.toString(totalBill.intValue()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.itemLayout.setBackgroundColor(Color.GRAY);
                Intent goToDetailPopUp=new Intent(mContext,activity_vehicle_popup_detail.class);

                goToDetailPopUp.putExtra("CatName", vehicleCategory.getCategoryName());
                goToDetailPopUp.putExtra("Cap", Float.toString(vehicleCategory.getCapacity()));
                goToDetailPopUp.putExtra("Vol", Double.toString(vehicleCategory.getVolume()));
                goToDetailPopUp.putExtra("Bill", Double.toString(totalBill.intValue()));
                mContext.startActivity(goToDetailPopUp);

                // myDialog=new Dialog(mContext);
                //myDialog.setContentView(R.layout.activity_vehicle_popup_detail);
                //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //myDialog.show();
            }
        });

        holder.tvVehicleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////==============On click lister for recycler view item



            }
        });

       // Log.e("This is my Error",n.getExtras().getString("Distance", "0"));
       // float distance= goTO.getFloatExtra("Distance", 0);
     // );
         //
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvVehicleName;
        TextView tvVehicleCapicity;
        TextView tvVehiclePrice;
        CardView itemLayout;
        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVehicleName=itemView.findViewById(R.id.vehicle_name);
            tvVehicleCapicity=itemView.findViewById(R.id.vehicle_capacity);
            itemLayout=itemView.findViewById(R.id.WholeItemLayout);
            tvVehiclePrice=itemView.findViewById(R.id.vehicle_price);
            mView=itemView;

        }
    }
}
