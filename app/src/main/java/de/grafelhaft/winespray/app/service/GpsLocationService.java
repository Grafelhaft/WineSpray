package de.grafelhaft.winespray.app.service;

import android.content.ContentResolver;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import de.grafelhaft.grafellib.util.PrefUtils;
import de.grafelhaft.winespray.app.R;

/**
 * Created by @author Markus Graf.
 */
public class GpsLocationService implements LocationListener {

    private static final String LOG_TAG = GpsLocationService.class.getName();

    private static GpsLocationService _instance;
    private static int _count = 0;

    public static GpsLocationService getInstance() {
        if (_instance == null) {
            _instance = new GpsLocationService();
        }
        return _instance;
    }

    private GpsLocationService() {
        _currentLocation.setLatitude(0.0d);
        _currentLocation.setLongitude(0.0d);
    }

    private Location _currentLocation = new Location(LocationManager.GPS_PROVIDER);
    private LocationManager _locationManager;

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG,
                "Longitude: " + location.getLongitude() +
                        " Latitude: " + location.getLatitude() +
                        " Altitude: " + location.getAltitude() +
                        " Accuracy: " + location.getAccuracy() +
                        " Speed: " + location.getSpeed()
        );
        this._currentLocation = location;
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

    public Location getCurrentLocation() {
        return this._currentLocation;
    }

    private boolean isGpsEnabled(Context context) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        return Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
    }

    public void start(Context context) {
        if (isGpsEnabled(context)) {
            _count++;
            int interval = Integer.parseInt(PrefUtils.readStringPref(context, R.string.pref_key_update_interval, "1000"));

            _locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = _locationManager.getBestProvider(criteria, true);
            Location location = _locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }
            _locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    interval, //minimum time interval between location updates, in milliseconds
                    0.05f, //minimum distance between location updates, in meters
                    this
            );
        }
    }

    public void stop() {
        _count = _count > 0 ? _count-- : 0;
        if (_locationManager != null && _count <= 0) {
            _locationManager.removeUpdates(this);
        }
    }

    public void stop(boolean force) {
        if (force) {
            if (_locationManager != null) {
                _locationManager.removeUpdates(this);
            }
        } else {
            stop();
        }
    }
}
