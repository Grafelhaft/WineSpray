package de.grafelhaft.winespray.api.attributes;

/**
 * Created by @author Markus Graf (Grafelhaft) on 13.10.2016.
 */

public interface HsWormsAttributes {

    interface V1 {

        String SENSOR_1 = "applicationQuantity";
        String SENSOR_2 = "injectionVolume";
        String SENSOR_3 = "returnFlowVolume";

        String TIMESTAMP = "timestamp";

        String HARDWARE_ID = "hwid";

    }

    interface V2 extends V1 {
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }

    String ALTITUDE = "altitude";
    String ACCURACY = "accuracy";
    String SPEED = "speed";
}
