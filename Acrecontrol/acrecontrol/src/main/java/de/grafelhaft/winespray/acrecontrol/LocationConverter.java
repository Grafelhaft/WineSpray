package de.grafelhaft.winespray.acrecontrol;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import static de.grafelhaft.winespray.acrecontrol.Constants.DISTANCE_LATITUDE_METER;
import static de.grafelhaft.winespray.acrecontrol.Constants.DISTANCE_LONGITUDE_METER;
import static de.grafelhaft.winespray.acrecontrol.Constants.EARTH_RADIUS;

/**
 * Created by @author Markus Graf (Grafelhaft) on 21.10.2016.
 */

public class LocationConverter {

    public static IPoint[] location2Point(Location[] locations) {
        IPoint[] points = new Point[locations.length];
        for (int i = 0; i < points.length; i++) {
            points[i] = location2Point(locations[i]);
        }
        return points;
    }

    public static IPoint location2Point(Location location) {
        return new Point(location.getLatitude(), location.getLongitude());
    }


    //Approximate GPS coord to distances in meter
    public static Point approxMeterSphere(Location location) {
        return approxMeterSphere(new Point(location.getLatitude(), location.getLongitude()));
    }

    public static Point approxMeterSphere(IPoint point) {
        double lat = point.x();
        double lng = point.y();
        double distLat = EARTH_RADIUS * (Math.PI/180) * lat;
        double distLng = EARTH_RADIUS * (Math.PI/180) * lng * Math.cos(Math.toRadians(lat));

        return new Point(distLat, distLng);
    }

    public static IPoint[] approxMeterSphere(IPoint[] points) {
        IPoint[] help = new Point[points.length];
        for (int i = 0; i < help.length; i++) {
            help[i] = approxMeterSphere(points[i]);
        }
        return help;
    }

    //Approximate distances in meter to GPS coords
    public static IPoint approxDegreeSphere(IPoint point) {
        double lat = point.x();
        double lng = point.y();
        double distLat = lat / (EARTH_RADIUS * (Math.PI/180));
        double distLng = lng / (EARTH_RADIUS * (Math.PI/180) * Math.cos(Math.toRadians(lat)));

        return new Point(distLat, distLng);
    }

    public static IPoint[] approxDegreeSphere(IPoint[] points) {
        IPoint[] help = new Point[points.length];
        for (int i = 0; i < help.length; i++) {
            help[i] = approxDegreeSphere(points[i]);
        }
        return help;
    }


    public static double calcAreaInSquareMeter(List<? extends IPoint> points) {
        List<IPoint> help = new ArrayList<>();
        for (IPoint p : points) {
            help.add(approxMeterSphere(p));
        }
        return new Polygon(help).area();
    }


    @Deprecated
    public static IPoint approxMeter(Location location) {
        return approxMeter(new Point(location.getLatitude(), location.getLongitude()));
    }

    @Deprecated
    public static IPoint approxMeter(IPoint point) {
        return new Point(DISTANCE_LATITUDE_METER * Math.cos(Math.toRadians(point.x() / 2)), point.y() * DISTANCE_LONGITUDE_METER);
    }

    @Deprecated
    public static IPoint[] approxMeter(Location[] locations) {
        return approxMeter(location2Point(locations));
    }

    @Deprecated
    public static IPoint[] approxMeter(IPoint[] points) {
        IPoint[] help = new Point[points.length];
        for (int i = 0; i < help.length; i++) {
            help[i] = approxMeter(points[i]);
        }
        return help;
    }


    @Deprecated
    public static IPoint approxDegree(IPoint point) {
        return new Point( (Math.toDegrees(Math.acos(point.x() / DISTANCE_LATITUDE_METER)) * 2), point.y() / DISTANCE_LONGITUDE_METER);
    }

    @Deprecated
    public static IPoint[] approxDegree(IPoint[] points) {
        IPoint[] help = new Point[points.length];
        for (int i = 0; i < help.length; i++) {
            help[i] = approxDegree(points[i]);
        }
        return help;
    }

}
