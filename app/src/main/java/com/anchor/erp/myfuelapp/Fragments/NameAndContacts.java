package com.anchor.erp.myfuelapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.anchor.erp.myfuelapp.Activities.Login;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NameAndContacts extends Fragment {

    private NamesPass listener;
    private TextInputEditText firstname,lastname,email,phone;
    private String s_firstname,s_lastname,s_email,s_phone;
    private Button next,back;
    String test = null;
    private Context context;
    private AppCompatActivity appCompatActivity;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public NameAndContacts() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        appCompatActivity = (AppCompatActivity) context;
        listener = (NamesPass) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_and_contacts,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firstname = view.findViewById(R.id.first_name);
        lastname = view.findViewById(R.id.last_name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email_address);
        next = view.findViewById(R.id.cntx_next);
        back = view.findViewById(R.id.cntx_back);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    email.setHint("(e.g myName@domain.com)");
                } else {
                    email.setHint("");
                }
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    phone.setHint("(e.g 07xxxxxxxxx)");
                } else {
                    phone.setHint("");
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (validate()){
                    final String phonefinal = removeLeadingZeroes(s_phone);
                    Call<MobileUser> call = Config.getNetClient().checkUser(s_email);
                    final ProgressDialog dialog = new ProgressDialog(context);
                    dialog.setMessage("Please wait ... ");
                    dialog.show();
                    call.enqueue(new Callback<MobileUser>() {
                        @Override
                        public void onResponse(Call<MobileUser> call, Response<MobileUser> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()){
                                MobileUser user = response.body();
                                if (user.getId() > 0){
                                    email.setError("There is an account registered with this email");
                                } else {
                                    listener.namesPass(s_firstname,s_lastname,s_email,phonefinal);
                                }
                            } else {
                                Toast.makeText(context,"Error "+response.code()+" Occured while verifying user",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MobileUser> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(context,"Server is unreachable. Check network",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(context,"Please Enter All the information required",Toast.LENGTH_LONG).show();
                }
            }
            public String removeLeadingZeroes(String value) {
                return new Integer(value).toString();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,Login.class));
                appCompatActivity.finish();
            }
        });
    }

    private boolean validate(){
        boolean result = true;
        getStrings();
        if (s_firstname.isEmpty()){
            firstname.setError("Enter first name");
            result = false;
        } else if (s_lastname.isEmpty()){
            lastname.setError("Enter last name");
            result = false;
        } else if (s_phone.isEmpty()){
            phone.setError("Enter phone number");
            result = false;
        } else if (s_email.isEmpty()){
            email.setError("Enter email address");
            result = false;
        } else if (!s_phone.startsWith("07")){
            phone.setError("Invalid phone number format. Should start with 07...");
            result = false;
        } else if (!VALID_EMAIL_ADDRESS_REGEX.matcher(s_email).find()){
            email.setError("invalid email input format");
            result = false;
        }
        return result;
    }

    private void getStrings(){
        s_firstname = firstname.getText().toString();
        s_lastname = lastname.getText().toString();
        s_phone = phone.getText().toString();
        s_email = email.getText().toString();
    }

    public interface NamesPass{
        void namesPass(String fname,String lname,String email,String phone);
    }

}
