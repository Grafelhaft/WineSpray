package de.grafelhaft.winespray.acrecontrol;

import org.junit.Before;
import org.junit.Test;

import static de.grafelhaft.winespray.acrecontrol.Constants.EARTH_RADIUS;
import static org.junit.Assert.*;

/**
 * Created by @author Markus Graf (Grafelhaft) on 03.02.2017.
 */
public class LocationConverterTest {

    private static final double EARTH_RADIUS = 6378.388;

    @Test
    public void convertGps() throws Exception {
        final IPoint point = new Point(90, 90);

        IPoint apoint = approxMeterSphere(point);
        IPoint bpoint = LocationConverter.approxDegreeSphere(apoint);
        IPoint cpoint = new Point(EARTH_RADIUS * (Math.PI/180) * point.x(), EARTH_RADIUS * (Math.PI/180) * Math.cos(Math.toRadians(point.x())) * point.y());

        assertEquals(point.x(), bpoint.x(), 0);
        assertEquals(point.y(), bpoint.y(), 0);
    }

    public static Point approxMeterSphere(IPoint point) {
        double lat = point.x();
        double lng = point.y();
        double distLat = EARTH_RADIUS * (Math.PI/180) * lat;
        double distLng = EARTH_RADIUS * /*(Math.PI/180) **/ Math.acos(Math.pow(Math.sin(Math.toRadians(lat)), 2) + Math.pow(Math.cos(Math.toRadians(lat)), 2) * Math.cos(Math.toRadians(lng)));
        //double distLng = EARTH_RADIUS * /*(Math.PI/180)* */ Math.acos(Math.sin(Math.toRadians(lat)) * Math.sin(Math.toRadians(lat)) + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lng)));

        return new Point(distLat, distLng);
    }

}