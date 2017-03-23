package de.grafelhaft.winespray.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.grafelhaft.grafellib.async.AAsyncTask;
import de.grafelhaft.grafellib.async.OnTaskOutputListener;
import de.grafelhaft.winespray.acrecontrol.Constants;
import de.grafelhaft.winespray.acrecontrol.IPoint;
import de.grafelhaft.winespray.acrecontrol.IPolygon;
import de.grafelhaft.winespray.acrecontrol.Point;
import de.grafelhaft.winespray.acrecontrol.Polygon;
import de.grafelhaft.winespray.acrecontrol.PolygonBuilder;
import de.grafelhaft.winespray.acrecontrol.RecordTask;
import de.grafelhaft.winespray.acrecontrol.algorithm.QuickHull;
import de.grafelhaft.winespray.acrecontrol.ScannerTask;
import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.State;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by @author Markus Graf (Grafelhaft) on 14.10.2016.
 */

public class AcreControlService {

    private static AcreControlService _instance;

    public static AcreControlService getInstance() {
        if (_instance == null) {
            _instance = new AcreControlService();
        }
        return _instance;
    }

    private AcreControlService() {

    }

    public static void scan(List<? extends IPolygon> polygons, IPoint point, ScannerTask.OnPolygonContainsPointsListener listener) {
        ScannerTask task = new ScannerTask(checkPolygon(polygons), point);
        task.addOnPolygonContainsPointsListener(listener);
        task.execute();
    }

    private static List<? extends IPolygon> checkPolygon(List<? extends IPolygon> polygons) {
        List<IPolygon> unmanagedList = new ArrayList<>(polygons.size());
        for (IPolygon p : polygons) {
            if (p instanceof RealmObject && ((RealmObject) p).isManaged()) {
                unmanagedList.add((IPolygon) Realm.getDefaultInstance().copyFromRealm((RealmObject) p));
            } else {
                unmanagedList.add(p);
            }
        }
        return unmanagedList;
    }

    public static List<Location> calcQuickHullPolygonLocations(List<Location> locations) {
        Map<Point, Location> pointLocationMap = new HashMap<>();
        List<Point> points = new ArrayList<>();
        for (Location l : locations) {
            if (l != null) {
                Point point = new Point(l.getLatitude(), l.getLongitude());
                points.add(point);
                pointLocationMap.put(point, l);
            }
        }

        List<? extends IPoint> polyPoints = QuickHull.scan(points);
        List<Location> polyLocations = new ArrayList<>(polyPoints.size());
        for (IPoint p : polyPoints) {
            polyLocations.add(pointLocationMap.get(p));
        }

        return polyLocations;
    }

    public static Polygon createPolygon(Run run) {
        final List<Location> dataLocations = new ArrayList<>();
        for (DataPoint d : run.getDataPoints()) {
            if (d.getLocation() != null) {
                if (d.isManaged()) {
                    dataLocations.add(Realm.getDefaultInstance().copyFromRealm(d.getLocation()));
                } else {
                    dataLocations.add(d.getLocation());
                }
            }
        }

        return new PolygonBuilder(dataLocations, Constants.UNIT_DEGREES).setOutput(Constants.UNIT_METER).build();
    }


    private RecordAcreAsyncTask _recordAcreAsyncTask;
    private RecordAsyncTask _recordAsyncTask;

    public void startRecording(OnTaskOutputListener<List<Location>> outputListener, OnPointAddedListener pointListener) {
        _recordAcreAsyncTask = new RecordAcreAsyncTask();
        _recordAcreAsyncTask.addOnTaskOutputListener(outputListener);
        _recordAcreAsyncTask.setOnPointAddedListener(pointListener);
        _recordAcreAsyncTask.execute();
    }

    public void stopRecording() {
        _recordAcreAsyncTask.onStateChanged(State.STOPPED);
    }

    public void record(RecordTask.OnResultListener resultListener, RecordTask.OnPointRecordedListener pointListener) {
        _recordAsyncTask = new RecordAsyncTask();
        _recordAsyncTask.addOnResultListener(resultListener);
        _recordAsyncTask.addOnPointRecordedListener(pointListener);
        _recordAsyncTask.execute();
    }

    public void stop() {
        if (_recordAsyncTask != null) {
            _recordAsyncTask.stop();
        }
    }

    private class RecordAcreAsyncTask extends AAsyncTask<Void, Location, List<Location>, List<Location>> implements State.OnStateChangedListener {

        private int __state;
        private OnPointAddedListener _pointAddedListener;

        public RecordAcreAsyncTask() {
            this.__state = State.ACTIVE;
        }

        @Override
        protected List<Location> doInBackground(Void... voids) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            List<Location> locations = new ArrayList<>();

            while (__state == State.ACTIVE) {
                try {
                    android.location.Location location = GpsLocationService.getInstance().getCurrentLocation();
                    Location l = new Location(location);
                    locations.add(l);
                    publishProgress(l);

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return locations;
        }

        @Override
        protected void onProgressUpdate(Location... values) {
            super.onProgressUpdate(values);
            if (_pointAddedListener != null) {
                _pointAddedListener.onPointAdded(values[0]);
            }
        }

        @Override
        protected List<Location> convertResultToOutput(List<Location> points) {
            return points;
        }

        @Override
        public void onStateChanged(int state) {
            this.__state = state;
        }

        public void setOnPointAddedListener(OnPointAddedListener l) {
            this._pointAddedListener = l;
        }
    }

    public interface OnPointAddedListener {
        void onPointAdded(Location point);
    }

    private class RecordAsyncTask extends RecordTask {

        @Override
        protected List<? extends IPoint> doInBackground(Void... voids) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            List<Location> locations = new ArrayList<>();

            while (this.isRecording()) {
                try {
                    android.location.Location location = GpsLocationService.getInstance().getCurrentLocation();
                    Location l = new Location(location);
                    locations.add(l);
                    publishProgress(l);

                    Thread.sleep(this.getSleepTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return locations;
        }
    }
}
