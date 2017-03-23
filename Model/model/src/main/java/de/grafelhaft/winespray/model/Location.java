package de.grafelhaft.winespray.model;

import android.location.LocationManager;

import org.json.JSONException;
import org.json.JSONObject;

import de.grafelhaft.winespray.acrecontrol.IPoint;
import de.grafelhaft.winespray.model.json.IJsonParsable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Location extends RealmObject implements IModel<Location>, IJsonParsable<Location>, IPoint {


    private Long id;

    private double latitude;
    private double longitude;
    private double altitude;

    private float accuracy;
    private float speed;


    public Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Location(double latitude, double longitude, double altitude, float speed, float accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.speed = speed;
    }

    public Location(android.location.Location location) {
        this(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getSpeed(), location.getAccuracy());
    }


    public Long getId() {
        return id;
    }

    public Location setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public double getLatitude() {
        return latitude;
    }

    public Location setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }


    public double getLongitude() {
        return longitude;
    }

    public Location setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }


    public double getAltitude() {
        return altitude;
    }

    public Location setAltitude(double altitude) {
        this.altitude = altitude;
        return this;
    }


    public float getAccuracy() {
        return accuracy;
    }

    public Location setAccuracy(float accuracy) {
        this.accuracy = accuracy;
        return this;
    }


    public float getSpeed() {
        return speed;
    }

    public Location setSpeed(float speed) {
        this.speed = speed;
        return this;
    }


    public static android.location.Location getAndroidLocation(Location location) {
        android.location.Location androidLocation = new android.location.Location(LocationManager.GPS_PROVIDER);
        androidLocation.setLongitude(location.getLongitude());
        androidLocation.setLatitude(location.getLatitude());
        androidLocation.setAltitude(location.getAltitude());
        androidLocation.setAccuracy(location.getAccuracy());
        androidLocation.setSpeed(location.getSpeed());

        return androidLocation;
    }


    @Override
    public Location fromJSON(JSONObject jsonObject) {
        double lat = jsonObject.optDouble("latitude");
        double lng = jsonObject.optDouble("longitude");
        double alt = jsonObject.optDouble("altitude");
        float speed = (float) jsonObject.optDouble("speed");
        float acc = (float) jsonObject.optDouble("accuracy");
        return new Location()
                .setLatitude(lat)
                .setLongitude(lng)
                .setAltitude(alt)
                .setSpeed(speed)
                .setAccuracy(acc);
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("latitude", this.latitude);
        jsonObject.putOpt("longitude", this.longitude);
        jsonObject.putOpt("altitude", this.altitude);
        jsonObject.putOpt("speed", this.speed);
        jsonObject.putOpt("accuracy", this.accuracy);
        return jsonObject;
    }

    @Override
    public double x() {
        return this.latitude;
    }

    @Override
    public double y() {
        return this.longitude;
    }
}
