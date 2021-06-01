package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class BackGroundUpdateVehicle extends AsyncTask<String,String,String> {

    private Context context;
    private Vehicle vehicle;
    private AppDB appDB;
    private ProgressDialog dialog;

    public BackGroundUpdateVehicle(Context context, Vehicle vehicle) {
        this.context = context;
        appDB = new AppDB(context);
        this.vehicle = vehicle;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Please wait ... ");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String message = null;

        NetClient netClient = Config.getNetClient();
        Call<Vehicle> call = netClient.updateCar(vehicle);
        try {
            Response<Vehicle> response = call.execute();
            if (response.isSuccessful()){
                Vehicle vehicle = response.body();
                int update = appDB.updateVehicle(vehicle);
                if (update > 0){
                    message = "Success";
                } else {
                    message = "Mobile update error";
                }
            } else {
                message = "Error "+response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        if (s != null){
            if (!s.equals("Success")){
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"Vehicle details Updated Successfully",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Server not reachable",Toast.LENGTH_SHORT).show();
        }
    }
}
