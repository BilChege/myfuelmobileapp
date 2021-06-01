package com.anchor.erp.myfuelapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeactivateCars extends DialogFragment {

    private Context context;
    private AppDB appDB;
    private ListView listView;
    private SessionPreferences sessionPreferences;
    private List<Vehicle> vehicles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activatecars,container,false);
        appDB = new AppDB(context);
        sessionPreferences = new SessionPreferences(context);
        listView = view.findViewById(R.id.carslist);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MobileUser loggedIn = sessionPreferences.getLoggedInUser();
        vehicles = appDB.getUserVehicles(loggedIn.getId(),false);
        if (vehicles != null){
            listView = view.findViewById(R.id.carslist);
            ActivateCarsAdapter activateCarsAdapter = new ActivateCarsAdapter(vehicles);
            listView.setAdapter(activateCarsAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private class ActivateCarsAdapter extends BaseAdapter{

        private List<Vehicle> vehicles;
        private LayoutInflater inflater;

        public ActivateCarsAdapter(List<Vehicle> vehicles) {
            this.vehicles = vehicles;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        public View getView(int position, View convertView, final ViewGroup parent) {
            convertView = inflater.inflate(R.layout.carelementactivate,parent,false);
            TextView regno = convertView.findViewById(R.id.regno);
            final Switch activate = convertView.findViewById(R.id.activate);
            final Vehicle vehicle = vehicles.get(position);
            activate.setChecked(vehicle.isActive());
            activate.setText(activate.isChecked()?"Deactivate":"Activate");
            activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked){
                        activate.setText("Activate");
                        vehicle.setActive(false);
                        new AlertDialog.Builder(context).setTitle("Deactivate?")
                                .setMessage("Are you Sure You want to Deactivate this vehicle\n Registration: "+vehicle.getRegno())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        upDateVehicle(vehicle,activate);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activate.setText("Deactivate");
                                        activate.setOnCheckedChangeListener(null);
                                        activate.setChecked(true);
                                        resetListener(activate);
                                    }
                                })
                                .show();
                    } else {
                        activate.setText("Deactivate");
                        vehicle.setActive(true);
                        new AlertDialog.Builder(context).setTitle("Activate?")
                                .setMessage("Are you sure you want to activate this car\n Registration: "+vehicle.getRegno())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        upDateVehicle(vehicle,activate);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activate.setText("Activate");
                                        activate.setOnCheckedChangeListener(null);
                                        activate.setChecked(false);
                                        resetListener(activate);
                                    }
                                })
                                .show();
                    }
                }

                private void resetListener(Switch activate) {
                    activate.setOnCheckedChangeListener(this);
                }
            });
            regno.setText(vehicle.getRegno());
            return convertView;
        }
    }
    private void upDateVehicle(final Vehicle vehicle, final Switch s){
        NetClient netClient = Config.getNetClient();
        Call<Vehicle> call = netClient.updateCar(vehicle);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait... ");
        progressDialog.show();
        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    Vehicle vehicle1 = response.body();
                    int update = 0;
                    update = appDB.updateVehicle(vehicle1);
                    Toast.makeText(context,"Updated Successfully",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"Error "+response.code()+" Occured",Toast.LENGTH_SHORT).show();
                    if (s.isChecked()){
                        s.setText("Activate");
                        s.setOnCheckedChangeListener(null);
                        s.setChecked(false);
                        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            }
                        });
                    } else {
                        s.setText("Deactivate");
                        s.setOnCheckedChangeListener(null);
                        s.setChecked(true);
                        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                progressDialog.dismiss();
                s.setOnCheckedChangeListener(null);
                Toast.makeText(context,"Check your connection",Toast.LENGTH_SHORT).show();
                if (s.isChecked()){
                    s.setText("Activate");
                    s.setChecked(false);
                } else {
                    s.setText("Deactivate");
                    s.setChecked(true);
                }
                s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked){
                            vehicle.setActive(false);
                            new AlertDialog.Builder(context).setTitle("Deactivate?")
                                    .setMessage("Are you Sure You want to Deactivate this vehicle\n Registration: "+vehicle.getRegno())
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            upDateVehicle(vehicle,s);
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    s.setOnCheckedChangeListener(null);
                                    s.setChecked(true);
                                }
                            })
                                    .show();
                            s.setOnCheckedChangeListener(this);
                            resetListener(s);
                        } else {
                            vehicle.setActive(true);
                            new AlertDialog.Builder(context).setTitle("Activate?")
                                    .setMessage("Are you sure you want to activate this car\n Registration: "+vehicle.getRegno())
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            upDateVehicle(vehicle,s);
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    s.setOnCheckedChangeListener(null);
                                    s.setChecked(false);
                                }
                            })
                                    .show();
                            s.setOnCheckedChangeListener(this);
                            resetListener(s);
                        }
                    }
                    private void resetListener(Switch activate) {
                        activate.setOnCheckedChangeListener(this);
                    }
                });
            }
        });
    }
}
