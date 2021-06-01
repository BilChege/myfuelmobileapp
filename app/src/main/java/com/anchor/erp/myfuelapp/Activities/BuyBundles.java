package com.anchor.erp.myfuelapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Adapters.NewBundlesAdapter;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Fragments.ConfirmPurchase;
import com.anchor.erp.myfuelapp.MpesaModels.StkPushQuery;
import com.anchor.erp.myfuelapp.Utils.MpesaPayment;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

public class BuyBundles extends AppCompatActivity implements Config.BundleSelection{

    private static final String TAG = "BuyBundles";
    private ExpandableListView bundlesview;
    private RecyclerView.LayoutManager layoutManager;
    private FuelPackage selected;
    private int status;
    private SessionPreferences sessionPreferences;
    public ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_bundles);
        dialog = new ProgressDialog(this);
        sessionPreferences = new SessionPreferences(this);
        bundlesview = findViewById(R.id.bundlecategory);
        Intent i = getIntent();
        status = i.getIntExtra("status",0);
        layoutManager = new LinearLayoutManager(BuyBundles.this);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new BackgroundShowBundles(bundlesview).execute();
    }

    @Override
    public void bundleselected(FuelPackage fuelPackage) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ConfirmPurchase cp = new ConfirmPurchase();
        Bundle data = new Bundle();
        data.putString("strbundleamt", String.valueOf(fuelPackage.getAmountOffuel())+" Bundles");
        data.putString("strprice", String.valueOf(fuelPackage.getPriceOfPackage())+" Shillings");
        data.putString("strpurchasedate", "Bought on "+String.valueOf(new Date()));
        data.putString("strexpdate", String.valueOf(new Date()));
        data.putString("strvalidity","30");
//        cp.setArguments(data);
//        cp.show(ft,null);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.key_purchase_status, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        super.onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sessionPreferences = new SessionPreferences(this);
        SharedPreferences sharedPreferences1 = getSharedPreferences(Config.key_purchase_status, Context.MODE_PRIVATE);
        status = sharedPreferences1.getInt(Config.key_purchase_status,0);
        if (status > 0) {
            SharedPreferences sharedPreferences = getSharedPreferences(Config.key_checkoutRequestId, Context.MODE_PRIVATE);
            StkPushQuery stkPushQuery = new StkPushQuery();
            stkPushQuery.setTimestamp(Config.generateTimeStamp());
            stkPushQuery.setCheckoutRequestID(sharedPreferences.getString(Config.key_checkoutRequestId, null));
            stkPushQuery.setPassword(Config.generateMpesaPassword());
            stkPushQuery.setBusinessShortCode(Config.BUSINESS_SHORT_CODE);
            MpesaPayment mpesaPayment = new MpesaPayment(this);
            Log.e(TAG, "onResume: "+new Gson().toJson(stkPushQuery));
            if (mpesaPayment.confirmPayment(stkPushQuery)) {

            }
        }
    }

    public class BackgroundShowBundles extends AsyncTask<Void,String,String>{

        private ExpandableListView view;
        private ProgressDialog dialog;

        public BackgroundShowBundles(ExpandableListView view) {
            this.view = view;
            dialog = new ProgressDialog(BuyBundles.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Fetching Packages ... ");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String message = null;
            NetClient client = Config.getNetClient();
            Call<List<FuelPackage>> call = client.allpackages();
            try {
                Response<List<FuelPackage>> response = call.execute();
                if (response.isSuccessful() && response.body() instanceof List){
                    message = "Success";
                    List<FuelPackage> fuelPackages = response.body();
                    Set<String> types = new HashSet<>();
                    List<String> titles = new ArrayList<>();
                    List<RadioGroup> radioGroups = new ArrayList<>();
                    for (FuelPackage f: fuelPackages){
                        types.add(f.getTypeOfPackage());
                    }
                    HashMap<String,List<FuelPackage>> details = new HashMap<>();
                    for (String s:types){
                        titles.add(s);
                        List<FuelPackage> fuelPackages1 = new ArrayList<>();
                        RadioGroup r = new RadioGroup(BuyBundles.this);
                        r.setOrientation(LinearLayout.VERTICAL);
                        for (FuelPackage f:fuelPackages){
                            if (f.getTypeOfPackage().equals(s)){
                                fuelPackages1.add(f);
                            }
                        }
                        details.put(s,fuelPackages1);
                    }
                    final NewBundlesAdapter adapter = new NewBundlesAdapter(titles,details,BuyBundles.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setAdapter(adapter);
                        }
                    });
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
                if (!aVoid.equals("Success")){
                    Toast.makeText(BuyBundles.this,aVoid,Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(BuyBundles.this,"Server Currently Unreachable",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
