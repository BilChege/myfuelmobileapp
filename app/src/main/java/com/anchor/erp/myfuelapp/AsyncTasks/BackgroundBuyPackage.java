package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Activities.HomePage;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Models.Purchase;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BackgroundBuyPackage extends AsyncTask<String,String,String> {

    private Purchase purchase;
    private ProgressDialog dialog;
    private Context context;
    private AppCompatActivity context1;
    private AppDB appDB;
    private SessionPreferences sessionPreferences;

    public BackgroundBuyPackage(Purchase purchase, Context context) {
        this.purchase = purchase;
        this.context = context;
        this.context1 = (AppCompatActivity) context;
        dialog = new ProgressDialog(this.context);
        sessionPreferences = new SessionPreferences(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Making Purchase ... ");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... voids) {

        String message = null;
        NetClient client = Config.getNetClient();
        Call<Purchase> call = client.buybundle(purchase);
        Log.e(TAG, "doInBackground: PACKAGE ID : "+purchase.getaPackage().getId() );
        RequestBody r = call.request().body();
        Log.e(TAG, "doInBackground: "+r.contentType());
        try {
            Response<Purchase> response = call.execute();
            Log.e(TAG, "doInBackground: "+response.body() );
            if (response.isSuccessful() && response.body()instanceof Purchase){
                message = "Success";
                Balances b = response.body().getBalances();
                sessionPreferences.insertBalances(b);
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
            if (aVoid == "Success"){
                new AlertDialog.Builder(context).setTitle("Purchase Successful")
                        .setMessage("You Have SuccessFully Purchased a prepay package for fuel worth "+String.format("%,.2f",purchase.getaPackage().getPriceOfPackage())+" Ksh.\n Expiry date: "+new SimpleDateFormat("dd/MM/yyyy").format(purchase.getExpiryDate())+" \n Points Awarded: "+purchase.getaPackage().getPoints())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, HomePage.class));
                            }
                        }).show();
            } else {
                Toast.makeText(context,aVoid,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Server currently Unreachable",Toast.LENGTH_SHORT).show();
        }
    }
}
