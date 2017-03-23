package de.grafelhaft.winespray.acrecontrol;

/**
 * Created by @author Markus Graf (Grafelhaft) on 11.10.2016.
 */

public class Point implements IPoint {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "|" + this.y + ")";
    }
}
