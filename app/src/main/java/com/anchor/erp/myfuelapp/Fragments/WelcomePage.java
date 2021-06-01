package com.anchor.erp.myfuelapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nbs.myfuelapp.R;

public class WelcomePage extends Fragment {

    private Button skip,next;
    private NextFromWelcome nxt;

    public WelcomePage() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        next = view.findViewById(R.id.wlcm_next);
        skip = view.findViewById(R.id.wlcm_skip);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nxt.nxtFromWelcomeClicked();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nxt.welcomeSkipClicked();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        nxt = (NextFromWelcome) context;
    }

    public interface NextFromWelcome {
        void nxtFromWelcomeClicked();
        void welcomeSkipClicked();
    }

}
