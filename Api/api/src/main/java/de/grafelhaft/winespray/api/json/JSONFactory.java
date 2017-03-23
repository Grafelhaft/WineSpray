package de.grafelhaft.winespray.api.json;

import org.json.JSONObject;

import de.grafelhaft.winespray.api.Api;
import de.grafelhaft.winespray.api.attributes.HsWormsAttributes;
import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Sensor;
import de.grafelhaft.winespray.model.SensorData;
import de.grafelhaft.winespray.model.SensorPurpose;
import de.grafelhaft.winespray.model.Unit;

/**
 * Created by Markus on 11.09.2016.
 */
public class JSONFactory {

    private Api _api;

    public JSONFactory(Api api) {
        this._api = api;
    }

    public DataPoint build(JSONObject jsonObject) {
        switch (_api) {
            case HsWormsSS16:
                return convertJsonFromV1(jsonObject);
            case HsWormsWS1617:
                return convertJsonFromV2(jsonObject);
            default:
                return null;
        }
    }

    private DataPoint convertJsonFromV1(JSONObject jsonObject) {

        double sensor1 = jsonObject.optDouble(HsWormsAttributes.V1.SENSOR_1);
        double sensor2 = jsonObject.optDouble(HsWormsAttributes.V1.SENSOR_2);
        double sensor3 = jsonObject.optDouble(HsWormsAttributes.V1.SENSOR_3);

        long timeStamp = jsonObject.optLong(HsWormsAttributes.V1.TIMESTAMP);
        String hwId = jsonObject.optString(HsWormsAttributes.V1.HARDWARE_ID);

        Sensor ejectionSensor = new Sensor(SensorPurpose.EJECTION, Unit.LITER_PER_SECOND);
        SensorData sensorData1 = new SensorData(sensor1, ejectionSensor.getUnit(), ejectionSensor);

        Sensor injectionSensor = new Sensor(SensorPurpose.INJECTION, Unit.LITER_PER_SECOND);
        SensorData sensorData2 = new SensorData(sensor2, injectionSensor.getUnit(), injectionSensor);

        Sensor returnSensor = new Sensor(SensorPurpose.RETURN, Unit.LITER_PER_SECOND);
        SensorData sensorData3 = new SensorData(sensor3, returnSensor.getUnit(), returnSensor);

        DataPoint dataPoint = new DataPoint(timeStamp)
                .addData(sensorData1, sensorData2, sensorData3)
                .setHardWareId(hwId);

        return dataPoint;
    }

    private DataPoint convertJsonFromV2(JSONObject jsonObject) {
        DataPoint dataPoint = convertJsonFromV1(jsonObject);

        double latitude = jsonObject.optDouble(HsWormsAttributes.V2.LATITUDE);
        double longitude = jsonObject.optDouble(HsWormsAttributes.V2.LONGITUDE);
        Location location = new Location(longitude, latitude);

        dataPoint.setLocation(location);

        return dataPoint;
    }

}
