package com.anchor.erp.myfuelapp.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileDealer;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BackGroundAllDealers extends AsyncTask<String,String,String> {

    private Context context;
    private AppDB appDB;
    private SessionPreferences sessionPreferences;

    public BackGroundAllDealers(Context context) {
        this.context = context;
        sessionPreferences = new SessionPreferences(this.context);
        appDB = new AppDB(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {

        String message = null;
        int id = Integer.parseInt(strings[0]);
        NetClient client = Config.getNetClient();
        Call<List<MobileDealer>> call = client.alldealers(id);
        try {
            Response<List<MobileDealer>> response = call.execute();
            if (response.isSuccessful()){
                List<MobileDealer> mobileDealers = response.body();
                if (!mobileDealers.isEmpty() && !appDB.getDealers().isEmpty() && appDB.getDealers() != null){
                    int [] ids = new int[appDB.getDealers().size()];
                    int counter = 0;
                    List<MobileDealer> dealers = appDB.getDealers();
                    for (MobileDealer mobileDealer:dealers){
                        ids[counter] = mobileDealer.getId();
                        counter += 1;
                    }
                    for (MobileDealer mobileDealer:mobileDealers){
                        if (contains(ids,mobileDealer.getId())){
                            MobileDealer dealer = appDB.getDealer(mobileDealer.getId());
                            if (dealer.equals(mobileDealer)){
                                continue;
                            } else {
                                appDB.updateDealer(mobileDealer);
                            }
                        } else {
                            appDB.addDealer(mobileDealer);
                        }
                    }
                } else if(!mobileDealers.isEmpty()){
                    for (MobileDealer dealer:mobileDealers){
                        appDB.addDealer(dealer);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean contains(final int[] array, final int key) {
        for (final int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }
}
