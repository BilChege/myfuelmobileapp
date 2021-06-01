package com.anchor.erp.myfuelapp.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GPSTracker extends Service implements LocationListener {

    private Context context;
    private FragmentActivity fragmentActivity;
    boolean gpsenabled = false;
    boolean networkenabled = false;

    Location location = null;
    private LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context = context;
        fragmentActivity = (FragmentActivity) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            gpsenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (location == null) {

                    if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.e(TAG, "getLocation:  is Location Enabled : "+location);
                }
        } catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
