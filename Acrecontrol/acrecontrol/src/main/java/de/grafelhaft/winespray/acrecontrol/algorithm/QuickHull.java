package de.grafelhaft.winespray.acrecontrol.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.grafelhaft.winespray.acrecontrol.IPoint;

/**
 * Created by @author Markus Graf (Grafelhaft) on 11.10.2016.
 * Source: http://www.ahristov.com/tutorial/geometry-games/convex-hull.html
 */

public class QuickHull {

    public static IPoint[] scan(IPoint... points) {
        List<? extends IPoint> help = scan(new ArrayList<IPoint>(Arrays.asList(points)));
        return help.toArray(new IPoint[help.size()]);
    }

    public static List<? extends IPoint> scan(List<? extends IPoint> points) {
        ArrayList<IPoint> convexHull = new ArrayList<>();

        //It's maybe not even a triangle
        if (points.size() <= 3)
            return points;

        int minPoint = -1, maxPoint = -1;
        double minX = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).x() < minX) {
                minX = points.get(i).x();
                minPoint = i;
            }
            if (points.get(i).x() > maxX) {
                maxX = points.get(i).x();
                maxPoint = i;
            }
        }

        IPoint A = points.get(minPoint);
        IPoint B = points.get(maxPoint);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);

        ArrayList<IPoint> leftSet = new ArrayList<>();
        ArrayList<IPoint> rightSet = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            IPoint p = points.get(i);
            if (pointLocation(A, B, p) == -1)
                leftSet.add(p);
            else if (pointLocation(A, B, p) == 1)
                rightSet.add(p);
        }
        hullSet(A, B, rightSet, convexHull);
        hullSet(B, A, leftSet, convexHull);

        return convexHull;
    }

    private static double distance(IPoint A, IPoint B, IPoint C) {
        double ABx = B.x() - A.x();
        double ABy = B.y() - A.y();
        double num = ABx * (A.y() - C.y()) - ABy * (A.x() - C.x());
        if (num < 0)
            num = -num;
        return num;
    }

    private static void hullSet(IPoint A, IPoint B, ArrayList<IPoint> set,
                               ArrayList<IPoint> hull) {
        int insertPosition = hull.indexOf(B);
        if (set.size() == 0)
            return;
        if (set.size() == 1) {
            IPoint p = set.get(0);
            set.remove(p);
            hull.add(insertPosition, p);
            return;
        }
        double dist = Integer.MIN_VALUE;
        int furthestPoint = -1;
        for (int i = 0; i < set.size(); i++) {
            IPoint p = set.get(i);
            double distance = distance(A, B, p);
            if (distance > dist) {
                dist = distance;
                furthestPoint = i;
            }
        }
        IPoint P = set.get(furthestPoint);
        set.remove(furthestPoint);
        hull.add(insertPosition, P);

        // Determine who's to the left of AP
        ArrayList<IPoint> leftSetAP = new ArrayList<>();
        for (int i = 0; i < set.size(); i++) {
            IPoint M = set.get(i);
            if (pointLocation(A, P, M) == 1) {
                leftSetAP.add(M);
            }
        }

        // Determine who's to the left of PB
        ArrayList<IPoint> leftSetPB = new ArrayList<>();
        for (int i = 0; i < set.size(); i++) {
            IPoint M = set.get(i);
            if (pointLocation(P, B, M) == 1) {
                leftSetPB.add(M);
            }
        }
        hullSet(A, P, leftSetAP, hull);
        hullSet(P, B, leftSetPB, hull);

    }

    private static int pointLocation(IPoint A, IPoint B, IPoint P) {
        double cp1 = (B.x() - A.x()) * (P.y() - A.y()) - (B.y() - A.y()) * (P.x() - A.x());
        if (cp1 > 0)
            return 1;
        else if (cp1 == 0)
            return 0;
        else
            return -1;
    }
}
