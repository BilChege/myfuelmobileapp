package com.anchor.erp.myfuelapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.LocObj;
import com.anchor.erp.myfuelapp.Models.MobileDealer;
import com.anchor.erp.myfuelapp.Services.GPSTracker;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {

    private static final float DEFAULT_ZOOM = 11;
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    Location mlastLocation;
    LocationManager locationManager;
    private MarkerOptions markerOptions;
    private Marker marker;
    private Button nearestStation;
    private Polyline polyline;
    Marker currLocationMarker;
    GoogleApiClient googleApiClient;
    SessionPreferences sessionPreferences;
    private LocObj locObj;
    private final double defaultlat = -1.28333;
    private final double defaultlon = 36.81667;
    private AppDB appDB;
    GPSTracker gpsTracker;
    FloatingActionButton floatingActionButton;
    LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 1;

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        nearestStation = findViewById(R.id.nearestStation);
        sharedPreferences = getSharedPreferences(Config.lastLocation, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        floatingActionButton = findViewById(R.id.btmmyloc);
        mlastLocation = new Location(LocationManager.GPS_PROVIDER);
        mlastLocation.setLatitude(defaultlat);
        mlastLocation.setLongitude(defaultlon);
        sessionPreferences = new SessionPreferences(MapsActivity.this);
        appDB = new AppDB(MapsActivity.this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        mapFragment.getMapAsync(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            buildGoogleApiClient();
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
//            onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

            final List<MobileDealer> mobileDealers = appDB.getDealers();
            int setdealers = 0;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            if (!mobileDealers.isEmpty()) {
                for (MobileDealer mobileDealer : mobileDealers) {
                    if (mobileDealer.getLatitude() != 0 && mobileDealer.getLongitude() != 0) {
                        LatLng latLng = new LatLng(mobileDealer.getLatitude(), mobileDealer.getLongitude());
                        builder.include(latLng);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(mobileDealer.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        setdealers += 1;
                    }
                }
                nearestStation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MapsActivity.this,"Your location not yet found",Toast.LENGTH_SHORT).show();
                    }
                });
                LatLngBounds bounds = builder.build();
//                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,50));
                if (setdealers == 0) {
                    new AlertDialog.Builder(MapsActivity.this).setMessage("No stations have been registered yet. Service will be available soon")
                            .setPositiveButton("Ok", null).show();
                }
            } else {
                new AlertDialog.Builder(MapsActivity.this).setMessage("No stations have been registered yet. Service will be available soon")
                        .setPositiveButton("Ok", null).show();
            }
        }

        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                onLocationChanged(location);
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return true;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlastLocation != null && mlastLocation.getLatitude() != defaultlat && mlastLocation.getLongitude() != defaultlon) {
                    LatLng latLng = new LatLng(mlastLocation.getLatitude(), mlastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    mMap.animateCamera(cameraUpdate);
                    if (marker != null) {
                        marker.remove();
                    }
                    markerOptions = new MarkerOptions().position(latLng);
                    markerOptions.title(sessionPreferences.getLoggedInUser().getFirstName() + " " + sessionPreferences.getLoggedInUser().getLastName());
                    marker = mMap.addMarker(markerOptions);
                    marker.setPosition(latLng);
                } else {
                    Toast.makeText(MapsActivity.this, "Location not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Polyline polyline = mMap.addPolyline(new PolylineOptions().clickable(true).add(new LatLng(-35.016, 143.321),
//                new LatLng(-34.747, 145.592),
//                new LatLng(-34.364, 147.891),
//                new LatLng(-33.501, 150.217),
//                new LatLng(-32.306, 149.248),
//                new LatLng(-32.491, 147.309)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));
//        polyline.setStartCap(new RoundCap());
//        polyline.setEndCap(new RoundCap());
//        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
//        polyline.setColor(R.color.colorPrimary);
//        polyline.setJointType(JointType.ROUND);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private class BackGroundNearestStation extends AsyncTask<String,String,String>{

        private ProgressDialog dialog;
        private List<LatLng> destinations;

        public BackGroundNearestStation(List<LatLng> destinations) {
            this.destinations = destinations;
            dialog = new ProgressDialog(MapsActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Routing to nearest station ... ");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            double myLat = mlastLocation.getLatitude();
            double myLon = mlastLocation.getLongitude();
            OkHttpClient okHttpClient = new OkHttpClient();
            String urlDestinations = "";
            int counter = 0;
            for (LatLng latLng: destinations){
                double lat = latLng.latitude;
                double lon = latLng.longitude;
                urlDestinations += lat+","+lon;
                if (counter < destinations.size() - 1){
                    urlDestinations += "|";
                }
                counter += 1;
            }
            Request request = new Request.Builder().url(Config.distanceMatrixApiUrl+"?units=imperial&origins="+myLat+","+myLon+"&destinations="+urlDestinations+"&key="+getResources().getString(R.string.google_maps_api_key))
                    .get().build();
            Log.i(TAG, "doInBackground: REQUEST URL -> "+request.url());
            try {
                Response response = okHttpClient.newCall(request).execute();
                LatLng nearestLatLng = null;
                if (response.isSuccessful()){
                    String json = response.body().string();
                    Log.i(TAG, "doInBackground: DISTANCE MATRIX JSON RESPONSE -> "+json);
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("OK")){
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject row = jsonArray.getJSONObject(i);
                            JSONArray elements = row.getJSONArray("elements");
                            int nearest = 0;
                            if (elements.length() > 0){
                                JSONObject element = elements.getJSONObject(0);
                                String elementStatus = element.getString("status");
                                if (elementStatus.equals("OK")){
                                    JSONObject distance = element.getJSONObject("distance");
                                    nearest = distance.getInt("value");
                                    nearestLatLng = destinations.get(0);
                                }
                            }
                            for (int j = 0;j < elements.length();j++){
                                JSONObject element = elements.getJSONObject(j);
                                String elementStatus = element.getString("status");
                                if (elementStatus.equals("OK")){
                                    JSONObject distance = element.getJSONObject("distance");
                                    int distanceValue = distance.getInt("value");
                                    if (distanceValue < nearest){
                                        nearestLatLng = destinations.get(j);
                                    }
                                }
                            }
                        }
                        double currLat = mlastLocation.getLatitude();
                        double currLong = mlastLocation.getLongitude();
                        double destLat = nearestLatLng.latitude;
                        double destLon = nearestLatLng.longitude;
                        OkHttpClient client = new OkHttpClient();
                        Request request1 = new Request.Builder().url(Config.directionsApiUrl+"?origin="+currLat+","+currLong+"&destination="+destLat+","+destLon+"&key="+getResources().getString(R.string.google_maps_api_key))
                                .get().build();
                        Response response1 = client.newCall(request1).execute();
                        Log.i(TAG, "doInBackground: REQUEST_URL-> "+request1.url());
                        if (response1.isSuccessful()){
                            result = response1.body().string();
                            Log.i(TAG, "doInBackground: DIRECTIONS API JSON RESPONSE -> "+result);
                        } else {
                            result = "Error "+response1.code()+" Occurred";
                        }
                    } else {
                        result = "Error: -> "+status;
                    }
                } else {
                    result = "Error "+response.code()+" Occurred";
                    Log.e(TAG, "doInBackground: (Google Distance Matrix API) ERROR MESSAGE: "+response.message()+" ERROR BODY: "+response.body() );
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s != null){
                if (!s.startsWith("Error")){
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String statusCode = jsonObject.getString("status");
                        if (statusCode.equals("OK")){
                            JSONArray jsonArray = jsonObject.getJSONArray("routes");
                            if (jsonArray.length() > 0){
                                JSONObject route = jsonArray.getJSONObject(0);
                                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                                String points = overviewPolyline.getString("points");
                                List<LatLng> routePoints = PolyUtil.decode(points);
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (int i = 0;i < routePoints.size() - 1;i++){
                                    LatLng src = routePoints.get(i);
                                    LatLng dest = routePoints.get(i+1);
                                    builder.include(src);
                                    builder.include(dest);
                                    polyline = mMap.addPolyline(new PolylineOptions().add(new LatLng(src.latitude,src.longitude),new LatLng(dest.latitude,dest.longitude)).width(5).color(Color.BLUE).geodesic(true));
                                }
                                LatLngBounds bounds = builder.build();
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,50));
                            }
                        } else {
                            Toast.makeText(MapsActivity.this,"Error ->"+statusCode,Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MapsActivity.this,s,Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MapsActivity.this,"Service is currently unreachable",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private synchronized void buildGoogleApiClient() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();
        }
        Log.e(TAG, "buildGoogleApiClient: @@@@@@@@@@       $$$$$$$$$$$  PERMISSION GRANTED :  "+ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) );

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected: CONNECT METHOD CALLED @@@@@@@@@@@@@@@@@@@  " );
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "onConnected: @@@@@@@@@@@@@@@@@@@     ##############    IF EXECUTED" );
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                }
            }
        }
    }

    boolean firstTime = true;

    @Override
    public void onLocationChanged(Location location) {
        nearestStation = findViewById(R.id.nearestStation);
        if (firstTime){
            if (location != null){
                mlastLocation = location;
                mlastLocation.setLatitude(location.getLatitude());
                mlastLocation.setLongitude(location.getLongitude());
                if (mlastLocation != null && mlastLocation.getLatitude() != defaultlat && mlastLocation.getLongitude() != defaultlon) {
                    LatLng latLng = new LatLng(mlastLocation.getLatitude(), mlastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
                    mMap.animateCamera(cameraUpdate);
                    if (marker != null){
                        marker.remove();
                    }
                    markerOptions = new MarkerOptions().position(latLng);
                    markerOptions.title(sessionPreferences.getLoggedInUser().getFirstName() + " " + sessionPreferences.getLoggedInUser().getLastName());
                    marker = mMap.addMarker(markerOptions);
                    marker.setPosition(latLng);
                } else {
                    Toast.makeText(MapsActivity.this, "Location not Available", Toast.LENGTH_SHORT).show();
                }
                firstTime = false;
                Log.e(TAG, "onLocationChanged: "+location.getLatitude()+"     @@@@@@@@@@@@@@@@@@      "+location.getLongitude() );
            } else {
                Toast.makeText(MapsActivity.this,"Location not available.",Toast.LENGTH_SHORT).show();
            }
            final List<MobileDealer> mobileDealers = appDB.getDealers();
            final List<LatLng> latLngs = new ArrayList<>();
            if (!mobileDealers.isEmpty()){
                nearestStation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mlastLocation != null){
                            for (MobileDealer mobileDealer : mobileDealers) {
                                if (mobileDealer.getLatitude() != 0 && mobileDealer.getLongitude() != 0) {
                                    LatLng latLng = new LatLng(mobileDealer.getLatitude(), mobileDealer.getLongitude());
                                    latLngs.add(latLng);
                                }
                            }
                            new BackGroundNearestStation(latLngs).execute();
                        } else {
                            Toast.makeText(MapsActivity.this,"Location is unavailable",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

}
