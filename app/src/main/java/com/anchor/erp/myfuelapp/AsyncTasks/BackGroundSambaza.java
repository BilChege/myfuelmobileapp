package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Activities.HomePage;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class BackGroundSambaza extends AsyncTask<String,String,String> {

    private Context context;
    private SessionPreferences sessionPreferences;
    private ProgressDialog progressDialog;

    public BackGroundSambaza(Context context) {
        this.context = context;
        sessionPreferences = new SessionPreferences(context);
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Processing Transaction ... ");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String message = null;

        String sentfrom = strings[0];
        String recipient = strings[1];
        String amt = strings[2];
        NetClient netClient = Config.getNetClient();
        Call<Balances> call = netClient.sambazaPackege(sentfrom,recipient,amt);
        try {
            Response<Balances> response = call.execute();
            if (response.isSuccessful()){
                Balances balances = response.body();
                sessionPreferences.insertBalances(balances);
                message = "Success";
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
        progressDialog.dismiss();
        if (s != null){
            if (s.equals("Success")){
                new AlertDialog.Builder(context).setTitle("Success")
                        .setMessage("Transaction has been completed successfully")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, HomePage.class));
                            }
                        }).show();
            } else {
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Check your Network",Toast.LENGTH_SHORT).show();
        }
    }
}
