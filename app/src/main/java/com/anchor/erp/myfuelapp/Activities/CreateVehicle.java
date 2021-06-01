package com.anchor.erp.myfuelapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundUpdateVehicle;
import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Models.VehicleMake;
import com.anchor.erp.myfuelapp.Models.VehicleModel;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CreateVehicle extends AppCompatActivity {

    private static final String TAG = "CreateVehicle";
    private TextInputEditText regno,engtp;
    private ArrayAdapter<String> makesadapter,modelsadapter,ftypeadapter;
    private Spinner fueltype;
    private SearchableSpinner make,model;
    private Button btnadd;
    private ToggleButton deactivatecar;
    private AppDB appDB;
    private CardView container;
    private String function;
    private RelativeLayout rlcontainer;
    private SessionPreferences sessionPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle);
        regno = findViewById(R.id.tvregno);
        sessionPreferences = new SessionPreferences(CreateVehicle.this);
        engtp = findViewById(R.id.enginetype);
        make = findViewById(R.id.spnmake);
        model = findViewById(R.id.spnmodel);
        Intent intent = getIntent();
        if (intent != null){
            function = intent.getStringExtra("function");
        }
        fueltype = findViewById(R.id.spnfueltype);
        btnadd = findViewById(R.id.btnaddcar);
        rlcontainer = findViewById(R.id.container);
        container = findViewById(R.id.containertvaddcar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appDB = new AppDB(CreateVehicle.this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final List<VehicleMake> makes = sessionPreferences.makes(sessionPreferences.getnumvmakes());
        List<String> makeslist = new ArrayList<>();
        for (VehicleMake make:makes){
            makeslist.add(make.getVehicleMake());
            Log.e(TAG, "onCreate: "+make.getVehicleMake() );
        }
        makesadapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,makeslist);
        makesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        make.setAdapter(makesadapter);
        if (function.equals("create")){
            make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    model.setEnabled(true);
                    for (VehicleMake vmake:makes){
                        if (vmake.getVehicleMake().equals(make.getSelectedItem())){
                            List<VehicleModel> map = vmake.getModels();
                            List<String> models = new ArrayList<>();
                            for (VehicleModel vehicleModel : map){
                                models.add(vehicleModel.getModel());
                            }
                            modelsadapter = new ArrayAdapter<String>(CreateVehicle.this,android.R.layout.simple_spinner_item,models);
                            modelsadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            model.setAdapter(modelsadapter);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    model.setEnabled(false);
                }
            });
        }
        String [] ftypes = new String[]{"Petrol","Diesel"};
        ftypeadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ftypes);
        ftypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fueltype.setAdapter(ftypeadapter);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (function.equals("create")){
                    if (validate()){
                        Vehicle vehicle = new Vehicle();
                        vehicle.setRegno(regno.getText().toString());
                        vehicle.setMake(model.getSelectedItem() != null ? make.getSelectedItem().toString()+" "+model.getSelectedItem().toString() : make.getSelectedItem().toString());
                        vehicle.setCCs(engtp.getText().toString());
                        for (VehicleMake m:makes){
                            if (m.getVehicleMake().equals(make.getSelectedItem())){
                                vehicle.setMakeid(m.getId());
                                List<VehicleModel> models = m.getModels();
                                if (models != null && !models.isEmpty()){
                                    for (VehicleModel vmodel : models){
                                        if (vmodel.getModel().equals(model.getSelectedItem())){
                                            vehicle.setModelid(vmodel.getId());
                                        }
                                    }
                                }
                            }
                        }
                        vehicle.setOwner(new SessionPreferences(CreateVehicle.this).getLoggedInUser());
                        vehicle.setEnginetype(fueltype.getSelectedItem().toString());
                        new  Backgroundaddcar(vehicle).execute();
                    }
                } else if (function.equals("edit")){
                    if (validate()){
                        Vehicle vehicle = sessionPreferences.getSelectedVehicle();
                        vehicle.setRegno(regno.getText().toString());
                        vehicle.setMake(make.getSelectedItem().toString()+" "+model.getSelectedItem().toString());
                        vehicle.setCCs(engtp.getText().toString());
                        for (VehicleMake m:makes){
                            if (m.getVehicleMake().equals(make.getSelectedItem())){
                                vehicle.setMakeid(m.getId());
                                List<VehicleModel> models = m.getModels();
                                for (VehicleModel vmodel : models){
                                    if (vmodel.getModel().equals(model.getSelectedItem())){
                                        vehicle.setModelid(vmodel.getId());
                                    }
                                }
                            }
                        }
                        vehicle.setOwner(new SessionPreferences(CreateVehicle.this).getLoggedInUser());
                        vehicle.setEnginetype(fueltype.getSelectedItem().toString());
                        new BackGroundUpdateVehicle(CreateVehicle.this,vehicle).execute();
                    }
                }
            }
        });
        if (function.equals("edit")){
            Vehicle selected = sessionPreferences.getSelectedVehicle();
            regno.setText(selected.getRegno());
            for (VehicleMake vehicleMake: makes){
                if (selected.getMakeid() == vehicleMake.getId()){
//                    make.setSelection(makesadapter.getPosition(vehicleMake.getVehicleMake()));
//                    HashMap <Integer,String> map = vehicleMake.getModels();
//                    List<String> models = new ArrayList<>();
//                    for (Integer in: map.keySet()){
//                        models.add(map.get(in));
//                    }
//                    modelsadapter = new ArrayAdapter<>(CreateVehicle.this,android.R.layout.simple_spinner_item,models);
//                    modelsadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    model.setAdapter(modelsadapter);
//                    for (Integer i: map.keySet()){
//                        if (selected.getModelid() == i){
//                            model.setSelection(modelsadapter.getPosition(map.get(i)));
//                        }
//                    }
                }
            }
            engtp.setText(selected.getCCs());
            fueltype.setSelection(ftypeadapter.getPosition(selected.getEnginetype()));
            btnadd.setText(R.string.updatevehicle);
        }
    }

    private boolean validate() {
        boolean response = true;
        if (TextUtils.isEmpty(regno.getText().toString())){
            regno.setError("Specify the Registration Number of the Car");
            Snackbar.make(rlcontainer,R.string.validatereg,Snackbar.LENGTH_SHORT).show();
            response = false;
        } else
        if (make.getSelectedItem() == null){
            TextView textView = (TextView) make.getSelectedView();
            textView.setError("Select The make");
            Snackbar.make(rlcontainer,R.string.validatemake,Snackbar.LENGTH_SHORT).show();
            response = false;
        } else
        if (TextUtils.isEmpty(engtp.getText().toString())){
            engtp.setError("Specify the number of CCs");
            Snackbar.make(rlcontainer,R.string.validatecc,Snackbar.LENGTH_SHORT).show();
            response = false;
        } else
        if (fueltype.getSelectedItem().equals("Select The Fuel Type")){
            TextView textView = (TextView) fueltype.getSelectedView();
            textView.setError("Select Fuel Type");
            Snackbar.make(rlcontainer,R.string.validateenginetype,Snackbar.LENGTH_SHORT).show();
            response = false;
        }
        return response;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeAsUp :{
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class Backgroundaddcar extends AsyncTask<String,String,String>{

        private Vehicle vehicle;
        private ProgressDialog dialog;
        private String TAG = "ADD CAR";

        public Backgroundaddcar(Vehicle vehicle) {
            this.vehicle = vehicle;
            dialog = new ProgressDialog(CreateVehicle.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Adding Car ... ");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

             String [] message = new String[1];
             NetClient client = Config.getNetClient();
            Call<Vehicle> call = client.addvehicle(vehicle);
            Log.e(TAG, "onClick: Cubic Centimetres : "+vehicle.getCCs() );
            try {
                Response<Vehicle> response = call.execute();
                if (response.isSuccessful()&&response.body() instanceof Vehicle){
                    Vehicle v = response.body();
                    v.setOwner(new SessionPreferences(CreateVehicle.this).getLoggedInUser());
                    appDB.addVehicle(v);
                    Log.e(TAG, "doInBackground: @@@@@@@@@@@@@@@@@@@@ if executed "+response.body() );
                    message[0] = "success";
                } else {
                    if (!response.message().equals(null)){
                        Log.e(TAG, "doInBackground: @@@@@@@@@@@@@@@@@@@@ else 1 executed" );
                        message[0] = "Error "+String.valueOf(response.code());
                    } else {
                        Log.e(TAG, "doInBackground: @@@@@@@@@@@@@@@@@@@@ else 2 executed" );
                        message[0] = "unknown error";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return message[0];
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "onPostExecute: "+s );
            super.onPostExecute(s);
            dialog.dismiss();
            if (s != null){
                if (s.equals("success")){
                    Toast.makeText(CreateVehicle.this,s,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateVehicle.this,MyCars.class));
                    finish();
                } else {
                    Toast.makeText(CreateVehicle.this,s,Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CreateVehicle.this,"Server Currently Unreachable",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
