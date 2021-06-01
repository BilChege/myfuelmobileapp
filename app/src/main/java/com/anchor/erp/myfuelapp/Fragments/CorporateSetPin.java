package com.anchor.erp.myfuelapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorporateSetPin extends BottomSheetDialogFragment {

    private Context context;
    private SessionPreferences sessionPreferences;
    private TextInputEditText setPin, rptPin;
    private Button savePin;
    private String pin1, pin2;

    public CorporateSetPin() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.corporateusersetpin,container,false);
        setPin = view.findViewById(R.id.set_pin);
        rptPin = view.findViewById(R.id.rpt_pin);
        savePin = view.findViewById(R.id.save_pin);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sessionPreferences = new SessionPreferences(context);
        final MobileUser loggedIn = sessionPreferences.getLoggedInUser();
        super.onViewCreated(view, savedInstanceState);
        savePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    loggedIn.setPin(Base64.encodeToString(pin1.getBytes(),Base64.NO_WRAP));
                    loggedIn.setAccountPassword(null);
                    Call<MobileUser> call = Config.getNetClient().updateuser(loggedIn);
                    final ProgressDialog dialog = new ProgressDialog(context);
                    dialog.setMessage("Saving data ... ");
                    dialog.show();
                    call.enqueue(new Callback<MobileUser>() {
                        @Override
                        public void onResponse(Call<MobileUser> call, Response<MobileUser> response) {
                            if (response.isSuccessful()){
                                dialog.dismiss();
                                MobileUser mobileUser = response.body();
                                sessionPreferences.insertLoggedInUser(mobileUser);
                                sessionPreferences.setCorporateFirstTime(false);
                                new AlertDialog.Builder(context).setMessage("Data saved successfully").setPositiveButton("Ok",null).show();
                                dismiss();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(context,"Error "+response.code()+" Occured",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MobileUser> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(context,"Server is unreachable. Check your network",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validate(){
        getValues();
        boolean valid = true;
        if (pin1.isEmpty()){
            setPin.setError("Enter a pin");
            valid = false;
        } else if (pin2.isEmpty()){
            rptPin.setError("Repeat pin");
            valid = false;
        } else if (!pin1.equals(pin2)){
            rptPin.setError("Values must match");
            valid = false;
        }
        return valid;
    }

    private void getValues(){
        pin1 = setPin.getText().toString();
        pin2 = rptPin.getText().toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
