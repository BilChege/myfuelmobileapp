package com.anchor.erp.myfuelapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Activities.CreateVehicle;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.example.nbs.myfuelapp.R;

import java.util.List;

public class MycarsAdapter extends BaseAdapter {

    private List<Vehicle> vehicles;
    private Context context;
    private MobileUser loggedIn;
    private AppCompatActivity context1;
    private LayoutInflater inflater;
    private SessionPreferences sessionPreferences;

    public MycarsAdapter(List<Vehicle> vehicles, AppCompatActivity context1) {
        this.vehicles = vehicles;
        this.context1 = context1;
        sessionPreferences = new SessionPreferences(context1);
        this.context = context1;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loggedIn = sessionPreferences.getLoggedInUser();
    }

    @Override
    public int getCount() {
        return vehicles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Vehicle selectedVehicle = vehicles.get(position);
        convertView = inflater.inflate(R.layout.carelement,parent,false);
        TextView make = convertView.findViewById(R.id.carmake);
        TextView regno = convertView.findViewById(R.id.mycarregno);
        make.setText(selectedVehicle.getMake());
        regno.setText(selectedVehicle.getRegno());
        if (!selectedVehicle.isActive()){
            regno.setText(selectedVehicle.getRegno()+" (Car Deactivated)");
            convertView.setEnabled(false);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionPreferences.setSelectedVehicle(selectedVehicle);
                context.startActivity(new Intent(context, CreateVehicle.class).putExtra("function","edit"));
            }
        });
        if (loggedIn.getRole().equals("Corp-User")){
            convertView.setOnClickListener(null);
        }
        return convertView;

    }
}
