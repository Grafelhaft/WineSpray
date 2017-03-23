package de.grafelhaft.winespray.acrecontrol;

/**
 * Created by @author Markus Graf (Grafelhaft) on 21.10.2016.
 */

public interface IPolygon<T extends IPoint> {

    T[] getPoints();

    double area();

    boolean contains(IPoint point);

    T centroid();

    void scale(double scale);

}
