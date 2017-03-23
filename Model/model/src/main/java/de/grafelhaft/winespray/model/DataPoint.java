package de.grafelhaft.winespray.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.grafelhaft.winespray.model.json.IJsonParsable;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class DataPoint extends RealmObject implements IModel<DataPoint>, IJsonParsable<DataPoint> {

    @PrimaryKey
    private Long id;

    private long timeStamp;
    private String hwId;

    private Location location;

    private RealmList<SensorData> data = new RealmList<>();


    public DataPoint() {
        this.timeStamp = System.currentTimeMillis();
    }

    public DataPoint(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    public Long getId() {
        return id;
    }

    public DataPoint setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public Location getLocation() {
        return location;
    }

    public DataPoint setLocation(Location location) {
        this.location = location;
        return this;
    }


    public List<SensorData> getData() {
        return data;
    }

    public SensorData getDataByPurpose(int purpose) {
        for (SensorData d : data) {
            if (d.getSensor().getPurpose() == purpose) {
                return d;
            }
        }
        return null;
    }

    public DataPoint setData(List<SensorData> data) {
        this.data.addAll(data);
        return this;
    }

    public DataPoint addData(SensorData... sensorData) {
        this.data.addAll(Arrays.asList(sensorData));
        return this;
    }

    public DataPoint addSensorData(List<SensorData> sensorData) {
        this.data.addAll(sensorData);
        return this;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    private DataPoint setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }


    public String getHardWareId() {
        return hwId;
    }

    public DataPoint setHardWareId(String hwId) {
        this.hwId = hwId;
        return this;
    }


    @Override
    public DataPoint fromJSON(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("id", this.id);
        jsonObject.putOpt("timestamp", this.timeStamp);
        jsonObject.putOpt("hwid", this.hwId);
        jsonObject.putOpt("location", location.toJSON());

        JSONArray jsonSensor = new JSONArray();
        for (SensorData s : data) {
            jsonSensor.put(s.toJSON());
        }
        jsonObject.putOpt("sensors", jsonSensor);

        return jsonObject;
    }


    public interface OnDataPointAddedListener {
        void onDataPointAdded(long runId, DataPoint dataPoint);
    }


    public static class Dummy {

        public static DataPoint create() {
            //int randomNum = rand.nextInt((max - min) + 1) + min;

            Random random = new Random();
            //double a = random.nextInt((100) + 1); //ejection volume
            double a = random.nextDouble(); //ejection volume
            double b = a * 0.25d; //injection volume
            //double c = random.nextInt((int) (a - b) + 1) + b; //returned volume
            double c = b + a * 0.33d; //returned volume

            Sensor sensor1 = new Sensor(SensorPurpose.EJECTION, Unit.LITER_PER_SECOND);
            sensor1.setId(0L);

            Sensor sensor2 = new Sensor(SensorPurpose.INJECTION, Unit.LITER_PER_SECOND);
            sensor2.setId(1L);

            Sensor sensor3 = new Sensor(SensorPurpose.RETURN, Unit.LITER_PER_SECOND);
            sensor3.setId(2L);

            DataPoint dp = new DataPoint()
                    .setTimeStamp(System.currentTimeMillis())
                    .addData(
                            new SensorData()
                                    .setSensor(sensor1)
                                    .setValue(a),
                            new SensorData()
                                    .setSensor(sensor2)
                                    .setValue(b),
                            new SensorData()
                                    .setSensor(sensor3)
                                    .setValue(c)
                    );
            RealmHelper.setAutoId(dp, DataPoint.class);

            return dp;
        }

    }
}
