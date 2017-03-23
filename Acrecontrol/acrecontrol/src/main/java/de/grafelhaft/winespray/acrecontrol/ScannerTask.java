package de.grafelhaft.winespray.acrecontrol;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author Markus Graf (Grafelhaft) on 20.10.2016.
 */

public class ScannerTask extends AsyncTask<Void, Void, List<IPolygon>> {

    private List<? extends IPolygon> polygons;
    private IPoint point;

    private List<OnPolygonContainsPointsListener> listeners = new ArrayList<>();

    public ScannerTask(List<? extends IPolygon> polygons, IPoint point) {
        this.polygons = polygons;
        this.point = point;
    }

    @Override
    protected List<IPolygon> doInBackground(Void... params) {
        List<IPolygon> list = new ArrayList<>();
        for (IPolygon p : polygons) {
            if (p.contains(point)) {
                list.add(p);
            }
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<IPolygon> polygons) {
        for (OnPolygonContainsPointsListener l : listeners) {
            if (polygons.size() > 0) {
                l.onHit(polygons);
            } else {
                l.onNothing();
            }
        }
    }


    public interface OnPolygonContainsPointsListener {
        void onHit(List<IPolygon> polygon);
        void onNothing();
    }

    public void addOnPolygonContainsPointsListener(OnPolygonContainsPointsListener l) {
        this.listeners.add(l);
    }

    public void removeOnPolygonContainsPointsListener(OnPolygonContainsPointsListener l) {
        this.listeners.remove(l);
    }
}
