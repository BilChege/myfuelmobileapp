package com.anchor.erp.myfuelapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Adapters.TransactionsAdapter;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Transactions extends Fragment {

    private RelativeLayout rlcontainer;
    private RecyclerView transactionslist;
    private Context context;
    private SessionPreferences sessionPreferences;

    public Transactions() {
        //default empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactions,container,false);
        rlcontainer = view.findViewById(R.id.transactions_container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionPreferences = new SessionPreferences(context);
        showList();
    }

    private void showList() {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait ... ");
        progressDialog.show();
        NetClient netClient = Config.getNetClient();
        MobileUser loggedIn = sessionPreferences.getLoggedInUser();
        Call<List<FuelCar>> call = netClient.usagesforuser(loggedIn.getId());
        Log.e(TAG, "showList: "+call.request().url() );
        call.enqueue(new Callback<List<FuelCar>>() {
            @Override
            public void onResponse(Call<List<FuelCar>> call, Response<List<FuelCar>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    List<FuelCar> fuelCars = response.body();
                    if (fuelCars.size() > 0){
                        transactionslist = new RecyclerView(context);
                        TransactionsAdapter adapter = new TransactionsAdapter(context,fuelCars);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                        transactionslist.setLayoutManager(layoutManager);
                        transactionslist.setAdapter(adapter);
                        if (rlcontainer.getChildCount() > 0){
                            for (int i = 0; i < rlcontainer.getChildCount();i++){
                                View view = rlcontainer.getChildAt(i);
                                rlcontainer.removeView(view);
                            }
                        }
                        rlcontainer.addView(transactionslist);
                    } else {
                        TextView textView = new TextView(context);
                        textView.setText("There were no Transactions Found");
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                        textView.setLayoutParams(layoutParams);
                        if (rlcontainer.getChildCount() > 0){
                            for (int i = 0; i < rlcontainer.getChildCount();i++){
                                View view = rlcontainer.getChildAt(i);
                                rlcontainer.removeView(view);
                            }
                        }
                        rlcontainer.addView(textView);
                    }
                } else {
                    TextView textView = new TextView(context);
                    textView.setText("There was an Error Retrieving the List");
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                    textView.setLayoutParams(layoutParams);
                    if (rlcontainer.getChildCount() > 0){
                        for (int i = 0; i < rlcontainer.getChildCount();i++){
                            View view = rlcontainer.getChildAt(i);
                            rlcontainer.removeView(view);
                        }
                    }
                    rlcontainer.addView(textView);
                    Toast.makeText(context,"Error "+response.code()+" Encountered",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FuelCar>> call, Throwable t) {
                progressDialog.dismiss();
                TextView textView = new TextView(context);
                textView.setText("There was an Error Retrieving the List");
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                textView.setLayoutParams(layoutParams);
                if (rlcontainer.getChildCount() > 0){
                    for (int i = 0; i < rlcontainer.getChildCount();i++){
                        View view = rlcontainer.getChildAt(i);
                        rlcontainer.removeView(view);
                    }
                }
                rlcontainer.addView(textView);
                Toast.makeText(context,"Server Error Encountered",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

}
