package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Activities.HomePage;
import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class BackGroundFuelCar extends AsyncTask<Void,String,String> {

    private Context context;
    private AppCompatActivity appCompatActivity;
    private FuelCar fuelCar;
    private AppDB appDB;
    private SessionPreferences sessionPreferences;
    private ProgressDialog dialog;

    public BackGroundFuelCar(Context context, FuelCar fuelCar) {
        this.context = context;
        appCompatActivity = (AppCompatActivity) context;
        this.fuelCar = fuelCar;
        this.dialog = new ProgressDialog(this.context);
        sessionPreferences = new SessionPreferences(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Processing request. Please wait ... ");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {

        String message = null;

        NetClient client = Config.getNetClient();
        Call<FuelCar> call = client.fuelmycar(fuelCar);
        try {
            Response<FuelCar> response = call.execute();
            if (response.isSuccessful() && response.body() instanceof FuelCar){
                message = "Success";
                Balances balances = response.body().getBalances();
                sessionPreferences.insertBalances(balances);
            } else {
                message = "Error "+response.code();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if (aVoid != null){
            if (aVoid.equals("Success")){
                context.startActivity(new Intent(context,HomePage.class));
                Toast.makeText(context,aVoid,Toast.LENGTH_SHORT).show();
                appCompatActivity.finish();
            } else {
                Toast.makeText(context,aVoid,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Server currently Unreachable",Toast.LENGTH_SHORT).show();
        }
    }
}
