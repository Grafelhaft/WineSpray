package de.grafelhaft.winespray.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.grafelhaft.winespray.model.json.IJsonParsable;
import io.realm.RealmObject;

/**
 * Created by Markus on 09.10.2016.
 */

public class SensorData extends RealmObject implements IJsonParsable<SensorData> {

    private double value;

    private String unit;
    private Sensor sensor;


    public SensorData() {

    }

    public SensorData(double value, String unit, Sensor sensor) {
        this.value = value;
        this.unit = unit;
        this.sensor = sensor;
    }


    public double getValue() {
        return value;
    }

    public SensorData setValue(double value) {
        this.value = value;
        return this;
    }


    public String getUnit() {
        return unit;
    }

    public SensorData setUnit(String unit) {
        this.unit = unit;
        return this;
    }


    public Sensor getSensor() {
        return sensor;
    }

    public SensorData setSensor(Sensor sensor) {
        this.sensor = sensor;
        return this;
    }


    @Override
    public SensorData fromJSON(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("value", this.value);
        jsonObject.putOpt("unit", this.unit);
        jsonObject.putOpt("sensor", this.sensor.toJSON());
        return jsonObject;
    }
}
