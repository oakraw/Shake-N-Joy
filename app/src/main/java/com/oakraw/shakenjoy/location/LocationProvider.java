package com.oakraw.shakenjoy.location;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.DetectedActivity;

import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by Rawipol on 11/26/14 AD.
 */
public class LocationProvider {

    private Context mContext;
    private double latitude;
    private double longitude;
    private boolean isCapturingLocation;

    public LocationProvider(Context mContext) {
        this.mContext = mContext;
    }

    public void startLocation() {

        // Init the location with custom options
        SmartLocation.getInstance().start(mContext, new SmartLocation.OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location, DetectedActivity detectedActivity) {
                listener.onGPSReady();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }


        });

        Location tryLastLocation = SmartLocation.getInstance().getLastKnownLocation(mContext);
        DetectedActivity tryLastActivity = SmartLocation.getInstance().getLastKnownActivity(mContext);

        isCapturingLocation = true;
        if (tryLastLocation != null && tryLastActivity != null) {
            latitude = tryLastLocation.getLatitude();
            longitude = tryLastLocation.getLongitude();
        } else {
        }

    }

    public void stopLocation() {
        // Stop the location capture
        SmartLocation.getInstance().stop(mContext);

        // Cleanup so we know we don't want extra activation/deactivation of the locator for the time being.
        SmartLocation.getInstance().cleanup(mContext);

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Lat:"+latitude+" Lng:"+longitude;
    }


    public interface OnGPSReadyListener
    {
        public void onGPSReady();
    }

    private OnGPSReadyListener listener;

    private int value;

    public void setOnGPSReady(OnGPSReadyListener listener)
    {
        this.listener = listener;
    }


}
