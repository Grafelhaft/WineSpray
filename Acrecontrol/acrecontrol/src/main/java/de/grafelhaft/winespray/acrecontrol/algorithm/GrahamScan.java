package de.grafelhaft.winespray.acrecontrol.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import de.grafelhaft.winespray.acrecontrol.IPoint;
import de.grafelhaft.winespray.acrecontrol.Point;

/**
 * Created by @author Markus Graf (Grafelhaft) on 22.10.2016.
 * Source: https://github.com/bkiers/GrahamScan/blob/master/src/main/cg/GrahamScan.java
 */

public class GrahamScan {

    /**
     * An enum denoting a directional-turn between 3 points (vectors).
     */
    protected static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }

    /**
     * Returns true iff all points in <code>points</code> are collinear.
     *
     * @param points the list of points.
     * @return       true iff all points in <code>points</code> are collinear.
     */
    protected static boolean areAllCollinear(List<? extends IPoint> points) {

        if(points.size() < 2) {
            return true;
        }

        final IPoint a = points.get(0);
        final IPoint b = points.get(1);

        for(int i = 2; i < points.size(); i++) {

            IPoint c = points.get(i);

            if(getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the convex hull of the points created from <code>xs</code>
     * and <code>ys</code>. Note that the first and last point in the returned
     * <code>List&lt;java.awt.Point&gt;</code> are the same point.
     *
     * @param xs the x coordinates.
     * @param ys the y coordinates.
     * @return   the convex hull of the points created from <code>xs</code>
     *           and <code>ys</code>.
     * @throws IllegalArgumentException if <code>xs</code> and <code>ys</code>
     *                                  don't have the same size, if all points
     *                                  are collinear or if there are less than
     *                                  3 unique points present.
     */
    public static List<? extends IPoint> getConvexHull(int[] xs, int[] ys) throws IllegalArgumentException {

        if(xs.length != ys.length) {
            throw new IllegalArgumentException("xs and ys don't have the same size");
        }

        List<IPoint> points = new ArrayList<IPoint>();

        for(int i = 0; i < xs.length; i++) {
            points.add(new Point(xs[i], ys[i]));
        }

        return getConvexHull(points);
    }

    /**
     * Returns the convex hull of the points created from <code>xs</code>
     * and <code>ys</code>. Note that the first and last point in the returned
     * <code>List&lt;java.awt.Point&gt;</code> are the same point.
     *
     * @param xs the x coordinates.
     * @param ys the y coordinates.
     * @return   the convex hull of the points created from <code>xs</code>
     *           and <code>ys</code>.
     * @throws IllegalArgumentException if <code>xs</code> and <code>ys</code>
     *                                  don't have the same size, if all points
     *                                  are collinear or if there are less than
     *                                  3 unique points present.
     */
    public static IPoint[] getConvexHull(IPoint[] points) throws IllegalArgumentException {
        List<? extends IPoint> help = getConvexHull(new ArrayList<IPoint>(Arrays.asList(points)));
        return help.toArray(new IPoint[help.size()]);
    }

    /**
     * Returns the convex hull of the points created from the list
     * <code>points</code>. Note that the first and last point in the
     * returned <code>List&lt;java.awt.Point&gt;</code> are the same
     * point.
     *
     * @param points the list of points.
     * @return       the convex hull of the points created from the list
     *               <code>points</code>.
     * @throws IllegalArgumentException if all points are collinear or if there
     *                                  are less than 3 unique points present.
     */
    public static List<? extends IPoint> getConvexHull(List<? extends IPoint> points) throws IllegalArgumentException {

        List<IPoint> sorted = new ArrayList<IPoint>(getSortedPointSet(points));

        if(sorted.size() < 3) {
            throw new IllegalArgumentException("can only create a convex hull of 3 or more unique points");
        }

        if(areAllCollinear(sorted)) {
            throw new IllegalArgumentException("cannot create a convex hull from collinear points");
        }

        Stack<IPoint> stack = new Stack<IPoint>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {

            IPoint head = sorted.get(i);
            IPoint middle = stack.pop();
            IPoint tail = stack.peek();

            Turn turn = getTurn(tail, middle, head);

            switch(turn) {
                case COUNTER_CLOCKWISE:
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(head);
                    break;
            }
        }

        // close the hull
        stack.push(sorted.get(0));

        return new ArrayList<IPoint>(stack);
    }

    /**
     * Returns the points with the lowest y coordinate. In case more than 1 such
     * point exists, the one with the lowest x coordinate is returned.
     *
     * @param points the list of points to return the lowest point from.
     * @return       the points with the lowest y coordinate. In case more than
     *               1 such point exists, the one with the lowest x coordinate
     *               is returned.
     */
    protected static IPoint getLowestPoint(List<? extends IPoint> points) {

        IPoint lowest = points.get(0);

        for(int i = 1; i < points.size(); i++) {

            IPoint temp = points.get(i);

            if(temp.y() < lowest.y() || (temp.x() == lowest.x() && temp.x() < lowest.x())) {
                lowest = temp;
            }
        }

        return lowest;
    }

    /**
     * Returns a sorted set of points from the list <code>points</code>. The
     * set of points are sorted in increasing order of the angle they and the
     * lowest point <tt>P</tt> make with the x-axis. If tow (or more) points
     * form the same angle towards <tt>P</tt>, the one closest to <tt>P</tt>
     * comes first.
     *
     * @param points the list of points to sort.
     * @return       a sorted set of points from the list <code>points</code>.
     * @see GrahamScan#getLowestPoint(java.util.List)
     */
    protected static Set<? extends IPoint> getSortedPointSet(List<? extends IPoint> points) {

        final IPoint lowest = getLowestPoint(points);

        TreeSet<IPoint> set = new TreeSet<IPoint>(new Comparator<IPoint>() {
            @Override
            public int compare(IPoint a, IPoint b) {

                if(a == b || a.equals(b)) {
                    return 0;
                }

                // use longs to guard against int-underflow
                double thetaA = Math.atan2((long)a.y() - lowest.y(), (long)a.x() - lowest.x());
                double thetaB = Math.atan2((long)b.y() - lowest.y(), (long)b.x() - lowest.x());

                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    // collinear with the 'lowest' point, let the point closest to it come first

                    // use longs to guard against int-over/underflow
                    double distanceA = Math.sqrt((((long)lowest.x() - a.x()) * ((long)lowest.x() - a.x())) +
                            (((long)lowest.y() - a.y()) * ((long)lowest.y() - a.y())));
                    double distanceB = Math.sqrt((((long)lowest.x() - b.x()) * ((long)lowest.x() - b.x())) +
                            (((long)lowest.y() - b.y()) * ((long)lowest.y() - b.y())));

                    if(distanceA < distanceB) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(points);

        return set;
    }

    /**
     * Returns the GrahamScan#Turn formed by traversing through the
     * ordered points <code>a</code>, <code>b</code> and <code>c</code>.
     * More specifically, the cross product <tt>C</tt> between the
     * 3 points (vectors) is calculated:
     *
     * <tt>(b.x-a.x * c.y-a.y) - (b.y-a.y * c.x-a.x)</tt>
     *
     * and if <tt>C</tt> is less than 0, the turn is CLOCKWISE, if
     * <tt>C</tt> is more than 0, the turn is COUNTER_CLOCKWISE, else
     * the three points are COLLINEAR.
     *
     * @param a the starting point.
     * @param b the second point.
     * @param c the end point.
     * @return the GrahamScan#Turn formed by traversing through the
     *         ordered points <code>a</code>, <code>b</code> and
     *         <code>c</code>.
     */
    protected static Turn getTurn(IPoint a, IPoint b, IPoint c) {

        // use longs to guard against int-over/underflow
        double crossProduct = ((b.x() - a.x()) * (c.y() - a.y())) -
                ((b.y() - a.y()) * (c.x() - a.x()));

        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }
}