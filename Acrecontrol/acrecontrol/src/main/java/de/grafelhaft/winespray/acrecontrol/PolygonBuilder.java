package de.grafelhaft.winespray.acrecontrol;

import android.location.Location;

import java.util.List;

import de.grafelhaft.winespray.acrecontrol.algorithm.GrahamScan;
import de.grafelhaft.winespray.acrecontrol.algorithm.QuickHull;
import de.grafelhaft.winespray.acrecontrol.builder.IBuilder;

import static de.grafelhaft.winespray.acrecontrol.Constants.UNIT_DEGREES;
import static de.grafelhaft.winespray.acrecontrol.Constants.UNIT_METER;
import static de.grafelhaft.winespray.acrecontrol.LocationConverter.approxDegree;
import static de.grafelhaft.winespray.acrecontrol.LocationConverter.approxDegreeSphere;
import static de.grafelhaft.winespray.acrecontrol.LocationConverter.approxMeterSphere;
import static de.grafelhaft.winespray.acrecontrol.LocationConverter.location2Point;

/**
 * Created by @author Markus Graf (Grafelhaft) on 20.10.2016.
 */

public class PolygonBuilder implements IBuilder<Polygon> {

    private IPoint[] points;
    private int unitIn = UNIT_DEGREES, unitOut;

    public PolygonBuilder(Location[] locations) {
        this.points = QuickHull.scan(location2Point(locations));
        this.unitIn = UNIT_DEGREES;
        this.unitOut = UNIT_DEGREES;
    }

    public PolygonBuilder(IPoint[] points, int unitIn) {
        this.points = QuickHull.scan(points);
        this.unitIn = unitIn;
        this.unitOut = unitIn;
    }

    public PolygonBuilder(List<? extends IPoint> points, int unitIn) {
        this(points.toArray(new IPoint[points.size()]), unitIn);
    }

    public PolygonBuilder(Location[] locations, boolean gs) {
        this.points = GrahamScan.getConvexHull(location2Point(locations));
        this.unitIn = UNIT_DEGREES;
        this.unitOut = UNIT_DEGREES;
    }

    public PolygonBuilder(IPoint[] points, int unitIn, boolean gs) {
        this.points = GrahamScan.getConvexHull(points);
        this.unitIn = unitIn;
        this.unitOut = unitIn;
    }

    public PolygonBuilder(List<? extends IPoint> points, int unitIn, boolean gs) {
        this(points.toArray(new IPoint[points.size()]), unitIn);
    }

    public PolygonBuilder setOutput(int unit) {
        this.unitOut = unit;
        return this;
    }

    @Override
    public Polygon build() {

        if (unitIn == UNIT_DEGREES & unitOut == UNIT_DEGREES) {
            return new Polygon(points);
        } else
        if (unitIn == UNIT_METER & unitOut == UNIT_METER) {
            return new Polygon(points);
        } else
        if (unitIn == UNIT_DEGREES & unitOut == UNIT_METER) {
            return new Polygon(approxMeterSphere(points));
        } else
        if (unitIn == UNIT_METER & unitOut == UNIT_DEGREES) {
            return new Polygon(approxDegreeSphere(points));
        }

        return null;
    }

}
