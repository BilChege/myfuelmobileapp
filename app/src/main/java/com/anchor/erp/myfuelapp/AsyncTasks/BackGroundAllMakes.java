package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.VehicleMake;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BackGroundAllMakes extends AsyncTask<String,String,String> {

    private Context context;
    private ProgressDialog dialog;
    private SessionPreferences sessionPreferences;

    public BackGroundAllMakes(Context context) {
        this.context = context;
        dialog = new ProgressDialog(this.context);
        sessionPreferences = new SessionPreferences(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Please wait ... ");
        dialog.setCancelable(false);
//        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        String message = null;
        NetClient client = Config.getNetClient();
        Call<List<VehicleMake>> call = client.allmakes();
        try {
            Response<List<VehicleMake>> response = call.execute();
            if (response.isSuccessful()){
                message = "Success";
                List<VehicleMake> makes = response.body();
                sessionPreferences.setnumvmakes(sessionPreferences.addVehicleMakes(makes));
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
            }
        } else {
            Toast.makeText(context,"Server currently unreachable. Check network",Toast.LENGTH_SHORT).show();
        }
    }
}
