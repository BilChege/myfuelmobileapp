package com.anchor.erp.myfuelapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.anchor.erp.myfuelapp.AsyncTasks.BackgroundBuyPackage;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Activities.BuyBundles;
import com.anchor.erp.myfuelapp.Models.Purchase;
import com.anchor.erp.myfuelapp.MpesaModels.StkPushQuery;
import com.anchor.erp.myfuelapp.MpesaModels.StkPushQuerySuccess;
import com.anchor.erp.myfuelapp.MpesaModels.StkRequest;
import com.anchor.erp.myfuelapp.MpesaModels.StkRequestSuccess;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.example.nbs.myfuelapp.R;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MpesaPayment {

    private Context context;
    private ProgressDialog progressDialog;
    private SessionPreferences sessionPreferences;

    public MpesaPayment(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        sessionPreferences = new SessionPreferences(context);
    }

    public String requestPin(StkRequest stkRequest){
        final String[] checkoutrequestid = {null};
        final BuyBundles categoryBundles = (BuyBundles) context;
        categoryBundles.dialog.setMessage("Processing Request. Please wait... ");
        categoryBundles.dialog.setCancelable(false);
        categoryBundles.dialog.show();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor(){
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String token = null;
                        try {
                            token = Config.authenticate();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Request.Builder builder = request.newBuilder().header("Authorization","Bearer "+token);
                        Request request1 = builder.build();
                        return chain.proceed(request1);
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.safaricomstkprocessrequest)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetClient netClient = retrofit.create(NetClient.class);
        Call<StkRequestSuccess> call = netClient.promptUser(stkRequest);
        call.enqueue(new Callback<StkRequestSuccess>() {
            @Override
            public void onResponse(Call<StkRequestSuccess> call, Response<StkRequestSuccess> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(context,"Wait for mpesa pin prompt or sms notification",Toast.LENGTH_SHORT).show();
                    StkRequestSuccess stkRequestSuccess = response.body();
                    checkoutrequestid[0] = stkRequestSuccess.getCheckoutRequestID();
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Config.key_checkoutRequestId,Context.MODE_PRIVATE);
                    SharedPreferences.Editor  editor = sharedPreferences.edit();
                    editor.putString(Config.key_checkoutRequestId,stkRequestSuccess.getCheckoutRequestID());
                    editor.commit();
                } else {
                    if (categoryBundles.dialog.isShowing()){
                        categoryBundles.dialog.dismiss();
                    }
                    Toast.makeText(context,"Error "+response.code()+" Occured while Processing Mpesa Request",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StkRequestSuccess> call, Throwable t) {
                if (categoryBundles.dialog.isShowing()){
                    categoryBundles.dialog.dismiss();
                }
                new AlertDialog.Builder(context).setTitle(R.string.purchasefailed)
                        .setMessage("There was no response from Mpesa. Your Balance could be insufficient")
                        .setPositiveButton("Ok",null).show();
            }
        });
        return checkoutrequestid[0];
    }

    public boolean confirmPayment(StkPushQuery stkPushQuery){
        final boolean[] confirmed = {false};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        String token = null;
                        try {
                            token = Config.authenticate();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Request.Builder builder = chain.request().newBuilder().header("Authorization","Bearer "+token);
                        Request request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.safaricomstkpushquery)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetClient netClient = retrofit.create(NetClient.class);

        Call<StkPushQuerySuccess> call = netClient.confirmPayment(stkPushQuery);
        progressDialog.setMessage("Confirming Payment ... ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        call.enqueue(new Callback<StkPushQuerySuccess>() {
            @Override
            public void onResponse(Call<StkPushQuerySuccess> call, Response<StkPushQuerySuccess> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    StkPushQuerySuccess success = response.body();
                    int resultcode = Integer.parseInt(success.getResultCode());
                    switch (resultcode){
                        case 0 : {
                            Purchase purchase = null;
                            try {
                                purchase = sessionPreferences.getPurchaseToBeMade();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            new BackgroundBuyPackage(purchase, context).execute();
                            break;
                        }
                        case 1 : {
                            new AlertDialog.Builder(context).setTitle(R.string.purchasefailed)
                                    .setMessage("You Do not have enough money in your Mpesa to make this purchase")
                                    .setPositiveButton("Ok",null)
                                    .show();
                            break;
                        }
                        case 2001 : {
                            new AlertDialog.Builder(context).setTitle(R.string.purchasefailed)
                                    .setMessage("You entered the wrong Mpesa Pin")
                                    .setPositiveButton("Ok",null)
                                    .show();
                            break;
                        }
                        case 1032 : {
                            new AlertDialog.Builder(context).setTitle(R.string.purchasefailed)
                                    .setMessage("You Cancelled the mpesa Transaction")
                                    .setPositiveButton("Ok",null)
                                    .show();
                            break;
                        }
                        default: {
                            new AlertDialog.Builder(context).setTitle(R.string.purchasefailed)
                                    .setMessage("An Error Occured during mpesa transaction. Please Contact Support.\n Issue number "+resultcode)
                                    .setPositiveButton("Ok",null)
                                    .show();
                        }
                    }
                } else {
                    Toast.makeText(context,"Error "+response.code()+" Occured while contacting Mpesa Services",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StkPushQuerySuccess> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context,"Error Reaching mpesa services",Toast.LENGTH_SHORT).show();
            }
        });
        return confirmed[0];
    }
}
