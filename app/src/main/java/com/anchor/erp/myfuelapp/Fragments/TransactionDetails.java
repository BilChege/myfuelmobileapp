package com.anchor.erp.myfuelapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetails extends Fragment {

    private EditText amount,date,car,station,feedback;
    private Button sendfeedback;
    private Context context;
    private RelativeLayout rlcontainer;
    private SimpleDateFormat simpleDateFormat;
    private SessionPreferences sessionPreferences;

    public TransactionDetails() {
        //default empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_details,container,false);
        amount = view.findViewById(R.id.etamt);
        date = view.findViewById(R.id.etdateoftransaction);
        car = view.findViewById(R.id.etcarfueled);
        station = view.findViewById(R.id.etStation);
        rlcontainer = view.findViewById(R.id.container);
        feedback = view.findViewById(R.id.etfeedback);
        sendfeedback = view.findViewById(R.id.btnsend);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionPreferences = new SessionPreferences(context);
        FuelCar fuelCar = null;
        try {
            fuelCar = sessionPreferences.getSelectedTransaction();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        amount.setText(String.valueOf(fuelCar.getAmount()));
        amount.setEnabled(false);
        date.setText(simpleDateFormat.format(fuelCar.getDateFueled()));
        date.setEnabled(false);
        car.setText(fuelCar.getVehicle().getRegno());
        car.setEnabled(false);
        station.setText(fuelCar.getStationid());
        station.setEnabled(false);
        final FuelCar finalFuelCar = fuelCar;
        sendfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuelCar fuelCar1 = new FuelCar();
                fuelCar1.setId(finalFuelCar.getId());
                fuelCar1.setFeedBack(feedback.getText().toString());
                sendUserFeedBack(fuelCar1);
            }
        });
    }

    private void sendUserFeedBack(FuelCar fuelCar1) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sending Feedback ... ");
        progressDialog.show();
        Call<FuelCar> call = Config.getNetClient().giveUserFeedback(fuelCar1);
        call.enqueue(new Callback<FuelCar>() {
            @Override
            public void onResponse(Call<FuelCar> call, Response<FuelCar> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    new AlertDialog.Builder(context).setTitle("Success ... ")
                            .setMessage("Your Feedback has been saved successfully.")
                            .setPositiveButton("ok", null)
                            .show();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(context,"Error "+response.code()+" Encountered",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FuelCar> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context,"Server is Unreachale",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}

