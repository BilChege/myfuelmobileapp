package com.anchor.erp.myfuelapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nbs.myfuelapp.R;

public class ConfirmPurchase extends DialogFragment {

    private TextView amountofBundle,priceofBundle,dateofPurchase,expirydate,validitydays;
    private String strbundleamt,strprice,strpurchasedate,strexpdate,strvalidity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirm_purchase,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        amountofBundle = view.findViewById(R.id.txtamtbundle);
        priceofBundle = view.findViewById(R.id.priceofbundle);
        dateofPurchase = view.findViewById(R.id.prchsdt);
        expirydate = view.findViewById(R.id.expdt);
        validitydays = view.findViewById(R.id.validitydays);

//        strbundleamt = getArguments().getString("strbundleamt");
//        strpurchasedate = getArguments().getString("strpurchasedate");
//        strprice = getArguments().getString("strprice");
//        strexpdate = getArguments().getString("strexpdate");
//        strvalidity = getArguments().getString("strvalidity");
//
//        amountofBundle.setText(strbundleamt);
//        priceofBundle.setText(strpurchasedate);
//        dateofPurchase.setText(strpurchasedate);
//        expirydate.setText(strexpdate);
//        validitydays.setText(strvalidity);
    }
}
