package de.grafelhaft.winespray.app.controller;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.common.collect.EvictingQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.grafelhaft.grafellib.async.OnTaskOutputListener;
import de.grafelhaft.grafellib.async.http.HttpResponse;
import de.grafelhaft.grafellib.async.http.OnConnectionResponseListener;
import de.grafelhaft.grafellib.util.PrefUtils;
import de.grafelhaft.winespray.acrecontrol.Constants;
import de.grafelhaft.winespray.acrecontrol.IPolygon;
import de.grafelhaft.winespray.acrecontrol.LocationConverter;
import de.grafelhaft.winespray.acrecontrol.Polygon;
import de.grafelhaft.winespray.acrecontrol.PolygonBuilder;
import de.grafelhaft.winespray.acrecontrol.ScannerTask;
import de.grafelhaft.winespray.api.Api;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.service.AcreControlService;
import de.grafelhaft.winespray.app.service.DataCrawlerService;
import de.grafelhaft.winespray.app.service.ServiceConstants;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.app.util.MathUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Area;
import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.RunConfig;
import de.grafelhaft.winespray.model.SensorPurpose;
import de.grafelhaft.winespray.model.State;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by @author Markus Graf on 12.10.2016.
 */

public class RunController implements State.OnStateChangedListener, OnConnectionResponseListener {

    private static final String LOG_TAG = RunController.class.getName();

    private static RunController _instance;

    public static synchronized RunController getInstance() {
        if (_instance == null) {
            _instance = new RunController();
        }
        return _instance;
    }

    private RunController() {

    }

    //Logic/////////////////////////////////////////////////////////////////////////////////////////

    private long _activeSessionId = -1;
    private RunConfig _activeRunConfig;

    public boolean start(Context context, long sessionId, RunConfig config) {
        if (!isServiceRunning(context)) {
            this._activeSessionId = sessionId;
            this._activeRunConfig = config;

            int smoothing = Integer.parseInt(PrefUtils.readStringPref(context, R.string.pref_key_smoothing, context.getString(R.string.pref_default_smoothing)));
            this._dataPointsQueue = EvictingQueue.create(smoothing);

            startService(context, sessionId);

            //notifySessionStarted(); -> DataCrawlerService

            Log.d(LOG_TAG, "Run started");
            return true;
        }
        return false;
    }

    public void pause(Context context) {
        Intent pauseIntent = new Intent(context, DataCrawlerService.class);
        pauseIntent.setAction(ServiceConstants.ACTION.PAUSE_FOREGROUND_ACTION);
        context.startService(pauseIntent);

        //notifySessionPaused(); -> DataCrawlerService

        Log.d(LOG_TAG, "Run paused");
    }

    public void stop(Context context) {
        Intent stopIntent = new Intent(context, DataCrawlerService.class);
        stopIntent.setAction(ServiceConstants.ACTION.STOP_FOREGROUND_ACTION);
        context.startService(stopIntent);

        //notifySessionStopped(); -> DataCrawlerService

        //stopService(context);

        Log.d(LOG_TAG, "Run stopped");
    }

    private void startService(Context context, long sessionId) {
        Intent startIntent = new Intent(context.getApplicationContext(), DataCrawlerService.class);
        startIntent.setAction(ServiceConstants.ACTION.START_FOREGROUND_ACTION);
        startIntent.putExtra(IntentUtils.EXTRA_SESSION_ID, sessionId);
        context.getApplicationContext().startService(startIntent);
    }

    private boolean stopService(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DataCrawlerService.class.getName().equals(service.service.getClassName())) {
                context.stopService(new Intent(context, DataCrawlerService.class));
                return true;
            }
        }
        return false;
    }

    public boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DataCrawlerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /*SETTER AND GETTER*/

    public long getActiveSessionId() {
        return this._activeSessionId;
    }

    public RunConfig getRunConfig() {
        return this._activeRunConfig;
    }

    public int getUpdateInterval() {
        return _activeRunConfig.getUpdateInterval();
    }

    public int getNozzleCount() {
        return _activeRunConfig.getNozzleCount();
    }

    public double getWorkingWidth() {
        return _activeRunConfig.getWorkingWidth();
    }


    public boolean isPhoneGps(Context context) {
        return PrefUtils.readBooleanPref(context, R.string.pref_key_phone_gps, true);
    }

    public boolean isDebugSession(Context context) {
        return PrefUtils.readBooleanPref(context, R.string.pref_key_dummy_data, false);
    }

    public Api getApiVersion(Context context) {
        String apiKey = PrefUtils.readStringPref(context, R.string.pref_key_api_version, "0");
        int apiV = Integer.parseInt(apiKey);
        return Api.values()[apiV];
    }

    public String getIpAddress(Context context) {
        return PrefUtils.readStringPref(context, R.string.pref_key_ip_address, getApiVersion(context).DefaultIP());
    }

    /*CALCULATION*/

    private EvictingQueue<DataPoint> _dataPointsQueue;

    public void addDataPointToQueue(DataPoint dataPoint) {
        _dataPointsQueue.add(dataPoint);
    }

    private Collection<DataPoint> getValues() {
        return _dataPointsQueue;
    }

    public double getQueueEjectedVolume() {
        return MathUtils.calcAverageVolume(getValues(), SensorPurpose.EJECTION);
    }

    public double getQueueInjectedVolume() {
        return MathUtils.calcAverageVolume(getValues(), SensorPurpose.INJECTION);
    }

    public double getQueueReturnedVolume() {
        return MathUtils.calcAverageVolume(getValues(), SensorPurpose.RETURN);
    }


    public void calcArea(long runId) {
        final Run run = (Run) RealmHelper.findWhereId(Run.class, runId);

        final List<Location> dataLocations = new ArrayList<>();
        for (DataPoint d : run.getDataPoints()) {
            if (d.getLocation() != null) {
                dataLocations.add(Realm.getDefaultInstance().copyFromRealm(d.getLocation()));
            }
        }

        final RealmResults<Area> areas = Realm.getDefaultInstance().where(Area.class).findAll();
        Polygon polygon = new PolygonBuilder(dataLocations, Constants.UNIT_DEGREES).build();
        AcreControlService.scan(areas, polygon.centroid(), new ScannerTask.OnPolygonContainsPointsListener() {
            @Override
            public void onHit(final List<IPolygon> polygons) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Acre> acres = realm.where(Acre.class).findAll();
                        for (Acre a : acres) {
                            if (a.getArea() != null && a.getArea().equals(polygons.get(0))) {
                                run.setAcre(a);
                            }
                        }
                    }
                });
            }

            @Override
            public void onNothing() {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final List<Location> areaLocations = AcreControlService.calcQuickHullPolygonLocations(dataLocations);

                        Area area = Realm.getDefaultInstance().createObject(Area.class);
                        area.setLocations(areaLocations)
                                .setSize(LocationConverter.calcAreaInSquareMeter(areaLocations))
                                .setSizeUseful(LocationConverter.calcAreaInSquareMeter(areaLocations));

                        Acre acre = Realm.getDefaultInstance().createObject(Acre.class, RealmHelper.getNextId(Acre.class));
                        acre.setArea(area);
                        acre.setName("?");
                        acre = realm.copyToRealmOrUpdate(acre);
                        run.setAcre(acre);
                    }
                });
            }
        });
    }


    /*LISTENER*/

    private ArrayList<DataPoint.OnDataPointAddedListener> _onDataPointAddedListeners = new ArrayList<>();

    public void addOnDataPointAddedListener(DataPoint.OnDataPointAddedListener l) {
        if (!_onDataPointAddedListeners.contains(l)) this._onDataPointAddedListeners.add(l);
    }

    public void removeOnDataPointAddedListener(DataPoint.OnDataPointAddedListener l) {
        this._onDataPointAddedListeners.remove(l);
    }

    public void notifyDataPointAdded(long runId, DataPoint dataPoint) {
        for (DataPoint.OnDataPointAddedListener l : _onDataPointAddedListeners) {
            l.onDataPointAdded(runId, dataPoint);
        }
    }


    private ArrayList<State.OnStateChangedListener> _onStateChangedListeners = new ArrayList<>();

    public void addOnStateChangedListener(State.OnStateChangedListener l) {
        if (!_onStateChangedListeners.contains(l)) this._onStateChangedListeners.add(l);
    }

    public void removeOnStateChangedListener(State.OnStateChangedListener l) {
        this._onStateChangedListeners.remove(l);
    }

    public void notifySessionStarted() {
        for (State.OnStateChangedListener l : _onStateChangedListeners) {
            l.onStateChanged(State.ACTIVE);
        }
    }

    public void notifySessionStopped() {
        for (State.OnStateChangedListener l : _onStateChangedListeners) {
            l.onStateChanged(State.STOPPED);
        }
    }

    public void notifySessionPaused() {
        for (State.OnStateChangedListener l : _onStateChangedListeners) {
            l.onStateChanged(State.PAUSED);
        }
    }

    @Override
    public void onStateChanged(int state) {
        for (State.OnStateChangedListener l : _onStateChangedListeners) {
            l.onStateChanged(state);
        }
    }


    private List<OnConnectionResponseListener> _onConnectionResponseListeners = new ArrayList<>();

    public void addOnConnectionResponeListener(OnConnectionResponseListener l) {
        this._onConnectionResponseListeners.add(l);
    }

    public void removeOnConnectionResponeListener(OnConnectionResponseListener l) {
        this._onConnectionResponseListeners.remove(l);
    }

    private void notifyConnectionResponse(HttpResponse response) {
        for (OnConnectionResponseListener l : _onConnectionResponseListeners) {
            l.onConnectionResponse(response);
        }
    }

    @Override
    public void onConnectionResponse(HttpResponse httpResponse) {
        notifyConnectionResponse(httpResponse);
    }
}
