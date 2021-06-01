package com.anchor.erp.myfuelapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Adapters.OffersAdapter;
import com.anchor.erp.myfuelapp.Models.OffersForMobile;
import com.example.nbs.myfuelapp.R;

import java.text.ParseException;
import java.util.List;

public class AllOffers extends Fragment {

    private Context context;
    private SessionPreferences sessionPreferences;
    private RecyclerView offers;
    private OffersAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public AllOffers() {
        //default empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionPreferences = new SessionPreferences(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.promosview,container,false);
        offers = view.findViewById(R.id.offers);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<OffersForMobile> offersForMobiles = null;
        try {
            offersForMobiles = sessionPreferences.getoffers(sessionPreferences.getNumOffers());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter = new OffersAdapter(context,offersForMobiles);
        layoutManager = new LinearLayoutManager(context);
        offers.setLayoutManager(layoutManager);
        offers.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
