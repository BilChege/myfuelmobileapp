package com.anchor.erp.myfuelapp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.anchor.erp.myfuelapp.AsyncTasks.BackgroundRedeemPoints;
import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileDealer;
import com.anchor.erp.myfuelapp.Models.MobileRedemptions;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.OffersForMobile;
import com.example.nbs.myfuelapp.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class RedeemOffer extends Fragment {

    private Context context;
    private AppDB appDB;
    private SessionPreferences sessionPreferences;
    private EditText promoname, promocode, promodesc, promopoints;
    private SearchableSpinner stationId;
    private Button confirm;
    private ArrayAdapter<MobileDealer> mobileDealerArrayAdapter;

    public RedeemOffer() {
        //default empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDB = new AppDB(context);
        sessionPreferences = new SessionPreferences(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.redeemoffer,container,false);
        promoname = view.findViewById(R.id.etpromoname);
        promocode = view.findViewById(R.id.etpromocode);
        promodesc = view.findViewById(R.id.promodesc);
        promopoints = view.findViewById(R.id.etnumpoints);
        stationId = view.findViewById(R.id.stationnumber);
        confirm = view.findViewById(R.id.confirm);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OffersForMobile selected = null;
        try {
            selected = sessionPreferences.getSelectedPromotion();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        promoname.setText(selected.getPromoname());
        promoname.setEnabled(false);
        promocode.setText(selected.getPromocode());
        promocode.setEnabled(false);
        promodesc.setText(selected.getPromodesc());
        promodesc.setEnabled(false);
        promopoints.setText(String.valueOf(selected.getPoints()));
        promopoints.setEnabled(false);
        List<MobileDealer> dealers = appDB.getDealers();
        mobileDealerArrayAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,dealers);
        mobileDealerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stationId.setAdapter(mobileDealerArrayAdapter);
        final OffersForMobile finalSelected = selected;
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    new AlertDialog.Builder(context).setTitle("Redeem Points?")
                            .setMessage("Do you want to Redeem "+ finalSelected.getPoints()+" Points for Service: "+finalSelected.getPromoname())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (sessionPreferences.getBalances().getPoints() >= finalSelected.getPoints()){
                                        MobileRedemptions redemptions = new MobileRedemptions();
                                        MobileUser user = new MobileUser();
                                        user.setId(sessionPreferences.getLoggedInUser().getId());
                                        redemptions.setMobileUser(user);
                                        redemptions.setOffers(finalSelected);
                                        redemptions.setDateredeemed(new Date());
                                        redemptions.setStationId(((MobileDealer) stationId.getSelectedItem()).getStationId());
                                        new BackgroundRedeemPoints(redemptions,context).execute();
                                    } else {
                                        new AlertDialog.Builder(context).setTitle("Insufficient Points")
                                                .setMessage("Sorry. You do not have enough points to redeem for this offer")
                                                .setPositiveButton("Ok",null)
                                                .show();
                                    }
                                }
                            }).setNegativeButton("No",null).show();
                }
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private boolean validate(){
        boolean result = true;
        if (stationId.getSelectedItem() == null){
            TextView textView = (TextView) stationId.getSelectedView();
            Toast.makeText(context,"Specify the station Id Number",Toast.LENGTH_SHORT).show();
            textView.setError("Please Specify the station");
            result = false;
        }
        return result;
    }
}
