package de.grafelhaft.winespray.acrecontrol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author Markus Graf (Grafelhaft) on 11.10.2016.
 */

public class Polygon implements IPolygon<IPoint> {

    private IPoint[] points;
    private double[] polyX, polyY;
    private int polySides;

    public Polygon(IPoint[] points) {
        this.points = points;

        this.polyX = new double[points.length];
        this.polyY = new double[points.length];

        for (int i = 0; i < points.length; i++) {
            this.polyX[i] = points[i].x();
            this.polyY[i] = points[i].y();
        }

        this.polySides = points.length;
    }

    public Polygon(List<IPoint> points) {
        this(points.toArray(new Point[points.size()]));
    }

    public boolean contains(IPoint point) {
        return contains(point.x(), point.y());
    }

    /*public boolean contains(double x, double y) {
        boolean c = false;
        int i, j = 0;
        for (i = 0, j = this.polySides - 1; i < this.polySides; j = i++) {
            if (((this.polyY[i] > y) != (this.polyY[j] > y))
                    && (x < (this.polyX[j] - this.polyX[i]) * (y - this.polyY[i]) / (this.polyY[j] - this.polyY[i]) + this.polyX[i]))
                c = !c;
        }
        return c;
    }*/

    public boolean contains(double x, double y) {
        boolean c = false;
        int i, j = 0;
        for (i = 0, j = this.polySides - 1; i < this.polySides; j = i++) {
            if (((this.points[i].y() > y) != (this.points[j].y() > y))
                    && (x < (this.points[j].x() - this.points[i].x()) * (y - this.points[i].y()) / (this.points[j].y() - this.points[i].y()) + this.points[i].x()))
                c = !c;
        }
        return c;
    }

    /*public double area() {
        double area = 0;         // Accumulates area in the loop
        int j = this.points.length - 1;  // The last vertex is the 'previous' one to the first

        for (int i = 0; i < this.points.length; i++) {
            area = area + (this.polyX[j] + this.polyX[i]) * (this.polyY[j] - this.polyY[i]);
            j = i;  //j is previous vertex to i
        }
        return Math.abs(area / 2);
    }*/

    public double area() {
        double area = 0;         // Accumulates area in the loop
        int j = this.points.length - 1;  // The last vertex is the 'previous' one to the first

        for (int i = 0; i < this.points.length; i++) {
            area = area + (this.points[j].x() + this.points[i].x()) * (this.points[j].y() - this.points[i].y());
            j = i;  //j is previous vertex to i
        }
        return Math.abs(area / 2);
    }

    public IPoint[] getPoints() {
        return this.points;
    }

    public IPoint centroid() {
        double x = 0, y = 0;

        for (IPoint p : this.points) {
            x += p.x();
            y += p.y();
        }

        return new Point(x/this.points.length, y/this.points.length);
    }

    public void scale(double scale) {
        IPoint[] newPoints = new Point[this.points.length];
        for (int i = 0; i < newPoints.length; i++) {
            newPoints[i] = scale(centroid(), this.points[i], scale);
        }
        this.points = newPoints;
    }


    public static Point scale(IPoint start, IPoint point, double scale) {
        double x = start.x() + ((point.x() - start.x()) * scale);
        double y = start.y() + ((point.y() - start.y()) * scale);
        return new Point(x, y);
    }

    public static List<IPoint> scale(IPolygon polygon, double scale) {
        List<IPoint> newPoints = new ArrayList<>(polygon.getPoints().length);
        for (IPoint p : polygon.getPoints()) {
            newPoints.add(scale(polygon.centroid(), p, scale));
        }
        return newPoints;
    }

    public static boolean contains(IPolygon polygon, IPoint point) {
        boolean c = false;
        int i, j = 0;
        for (i = 0, j = polygon.getPoints().length - 1; i < polygon.getPoints().length; j = i++) {
            if (((polygon.getPoints()[i].y() > point.y()) != (polygon.getPoints()[j].y() > point.y()))
                    && (point.x() < (polygon.getPoints()[j].x() - polygon.getPoints()[i].x()) * (point.y() - polygon.getPoints()[i].y()) / (polygon.getPoints()[j].y() - polygon.getPoints()[i].y()) + polygon.getPoints()[i].x()))
                c = !c;
        }
        return c;
    }

    public static double area(IPolygon polygon) {
        double area = 0;         // Accumulates area in the loop
        int j = polygon.getPoints().length - 1;  // The last vertex is the 'previous' one to the first

        for (int i = 0; i < polygon.getPoints().length; i++) {
            area = area + (polygon.getPoints()[j].x() + polygon.getPoints()[i].x()) * (polygon.getPoints()[j].y() - polygon.getPoints()[i].y());
            j = i;  //j is previous vertex to i
        }
        return Math.abs(area / 2);
    }

    public static IPoint centroid(IPolygon polygon) {
        double x = 0, y = 0;

        for (IPoint p : polygon.getPoints()) {
            x += p.x();
            y += p.y();
        }

        return new Point(x/polygon.getPoints().length, y/polygon.getPoints().length);
    }
}
