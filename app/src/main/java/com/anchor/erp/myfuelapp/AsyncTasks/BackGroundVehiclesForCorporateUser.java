package com.anchor.erp.myfuelapp.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BackGroundVehiclesForCorporateUser extends AsyncTask<String,String,String> {

    private Context context;
    AppDB appDB;

    public BackGroundVehiclesForCorporateUser(Context context) {
        this.context = context;
        appDB = new AppDB(context);
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        int id = Integer.parseInt(strings[0]);
        Call<List<Vehicle>> call = Config.getNetClient().vehiclesForCorporate(id);
        try {
            Response<List<Vehicle>> response = call.execute();
            if (response.isSuccessful()){
                result = "Success";
                List<Vehicle> vehicles = response.body();
                if (vehicles != null && !vehicles.isEmpty()){
                    Vehicle vehicle = vehicles.get(0);
                    List<Vehicle> vehicles1 = appDB.getUserVehicles(id,false);
                    if (vehicles1 != null && !vehicles1.isEmpty()){
                        boolean exists = false;
                        for (Vehicle vehicle1 : vehicles1){
                            if (vehicle.getId() == vehicle1.getId()){
                                exists = true;
                                break;
                            } else {
                                continue;
                            }
                        }
                        if (!exists){
                            appDB.addVehicle(vehicle);
                        }
                    } else {
                        appDB.addVehicle(vehicle);
                    }
                }
            } else {
                result = "Error "+response.code()+" Occured (Corporate User Vehicles)";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null){
            if (!s.equals("Success")){
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
