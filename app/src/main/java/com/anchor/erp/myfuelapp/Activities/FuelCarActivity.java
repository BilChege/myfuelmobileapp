package com.anchor.erp.myfuelapp.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundFuelCar;
import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Models.MobileDealer;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "FUELCARACTIVITY";
    private TextInputEditText amountcash,pin;
    private Button ok;
    private MaterialSpinner vehicle;
    private SearchableSpinner stationId;
    private ArrayAdapter<String> registrations;
    private AppDB appDB;
    private RelativeLayout container;
    private TextView tv;
    private SimpleDateFormat simpleDateFormat;
    private SessionPreferences sessionPreferences;
    private List<Vehicle> vehicles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_car);

        appDB = new AppDB(FuelCarActivity.this);
        vehicle = findViewById(R.id.vehicle);
        stationId = findViewById(R.id.stationnumber);
        container = findViewById(R.id.container);
        sessionPreferences = new SessionPreferences(FuelCarActivity.this);
        tv = findViewById(R.id.balanceval);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final MobileUser loggedIn = sessionPreferences.getLoggedInUser();
        vehicles = appDB.getUserVehicles(loggedIn.getId(),true);
        String display = String.format("%,.2f",sessionPreferences.getBalances().getAccount());
        tv.setText(display);

        List<Vehicle> vehicles = appDB.getUserVehicles(loggedIn.getId(),true);
        ArrayAdapter<Vehicle> vehicleArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,vehicles);
        vehicleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicle.setAdapter(vehicleArrayAdapter);

        List<MobileDealer> mobileDealers = appDB.getDealers();
        ArrayAdapter<MobileDealer> mobileDealerArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,mobileDealers);
        mobileDealerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stationId.setAdapter(mobileDealerArrayAdapter);

        amountcash = findViewById(R.id.amountoffuel);

        pin = findViewById(R.id.pin);
        ok = findViewById(R.id.btnfuelcar);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (validate()){
                        final FuelCar fuelCar = new FuelCar();
                        fuelCar.setVehicle((Vehicle) vehicle.getSelectedItem());
                        fuelCar.setStationid(((MobileDealer)stationId.getSelectedItem()).getStationId());
                        fuelCar.setDateFueled(new Date());
                        fuelCar.setAmount(Double.parseDouble(amountcash.getText().toString()));
                        fuelCar.setUser(loggedIn);
                        double amount = Double.parseDouble(amountcash.getText().toString());
                        new AlertDialog.Builder(FuelCarActivity.this)
                                .setTitle("Confirm Transaction")
                                .setMessage("Confirm that you want to purchase fuel worth "+String.format("%,.2f",amount)+"Ksh" +
                                        " for "+((Vehicle) vehicle.getSelectedItem()).getRegno())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String pinValue = pin.getText().toString();
                                        String encoded = Base64.encodeToString(pinValue.getBytes(),Base64.NO_WRAP);
                                        Call<Boolean> call = Config.getNetClient().verifyPin(loggedIn.getId(),encoded);
                                        final ProgressDialog dialog1 = new ProgressDialog(FuelCarActivity.this);
                                        dialog1.setMessage("Verifying Pin ... ");
                                        dialog1.show();
                                        call.enqueue(new Callback<Boolean>() {
                                            @Override
                                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                                dialog1.dismiss();
                                                if (response.isSuccessful()){
                                                    Boolean valid = response.body();
                                                    if (valid){
                                                        new BackGroundFuelCar(FuelCarActivity.this,fuelCar).execute();
                                                    } else {
                                                        Toast.makeText(FuelCarActivity.this,"The pin you entered is incorrect",Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(FuelCarActivity.this,"Error "+response.code()+" Occured",Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Boolean> call, Throwable t) {
                                                dialog1.dismiss();
                                                Toast.makeText(FuelCarActivity.this,"Service is currently unreachable",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("No",null).show();
                    }
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validate() throws InvalidKeySpecException, NoSuchAlgorithmException {
        boolean result = true;
        Balances balances = sessionPreferences.getBalances();
        MobileUser loggedIn = sessionPreferences.getLoggedInUser();
        if (vehicle.getSelectedItem().equals("Select The Car To Fuel")){
            TextView textView = (TextView) vehicle.getSelectedView();
            textView.setError("Cannot Be empty");
            Snackbar.make(container,"Select The car To Fuel",Snackbar.LENGTH_SHORT).show();
            result = false;
        } else if (stationId.getSelectedItem()== null){
            TextView textView = (TextView) stationId.getSelectedView();
            textView.setError("Cannot Be Empty");
            Snackbar.make(container,"Enter the Station Id Number",Snackbar.LENGTH_SHORT).show();
            result = false;
        } else if (TextUtils.isEmpty(amountcash.getText().toString())){
            amountcash.setError("Specify the amount to Fuel");
            Snackbar.make(container,"Specify the amount to Fuel",Snackbar.LENGTH_SHORT).show();
            result = false;
        } else if (TextUtils.isEmpty(pin.getText().toString())){
            pin.setError("Enter Your Pin");
            Snackbar.make(container,"Enter Your Pin",Snackbar.LENGTH_SHORT).show();
            result = false;
        } else if (balances.getAccount() < Double.parseDouble(amountcash.getText().toString())){
            amountcash.setError("The amount entered exceeds your Balance");
            Snackbar.make(container,"The amount entered exceeds your Balance",Snackbar.LENGTH_SHORT).show();
            result = false;
        } else if (Double.parseDouble(amountcash.getText().toString()) <= 0){
            amountcash.setError("Invalid amount. Enter an amount greater than zero");
            result = false;
        }
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
