package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class Backgroundfetchbalances extends AsyncTask<String,String,String> {

    private ProgressDialog dialog;
    private Context context;

    public Backgroundfetchbalances(Context context) {
        this.context = context;
        dialog = new ProgressDialog(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Fetching your Balances");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String message = null;

        NetClient client = Config.getNetClient();
        Call<Balances> call = client.getbalances(new SessionPreferences(context).getLoggedInUser().getId());
        try {
            Response<Balances> response = call.execute();
            if (response.isSuccessful() && response.body() instanceof Balances){
                new SessionPreferences(context).insertBalances(response.body());
                message = "Balances Fetched";
            } else {
                message = "No Transaction";
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
            Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Server currently Unreachable",Toast.LENGTH_SHORT).show();
        }
    }
}
