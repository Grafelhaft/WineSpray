package de.grafelhaft.winespray.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import de.grafelhaft.grafellib.async.OnTaskOutputListener;
import de.grafelhaft.grafellib.async.http.HttpResponse;
import de.grafelhaft.grafellib.async.http.OnConnectionResponseListener;
import de.grafelhaft.grafellib.util.TimeUtils;
import de.grafelhaft.winespray.api.tasks.HttpReadSensorDataAsyncTask;
import de.grafelhaft.winespray.app.ActiveRunActivity;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.controller.RunController;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.SensorPurpose;
import de.grafelhaft.winespray.model.Session;
import de.grafelhaft.winespray.model.State;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

/**
 * Created by Markus on 11.09.2016.
 */
public class DataCrawlerService extends Service implements OnTaskOutputListener<DataPoint>, OnConnectionResponseListener {

    private static final String LOG_TAG = DataCrawlerService.class.getName();

    private Run _run;
    private long _sessionId;
    private long _runId;
    private int _state;

    private Timer _debugTimer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ServiceConstants.ACTION.START_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");

            //Get session id
            _sessionId = intent.getLongExtra(IntentUtils.EXTRA_SESSION_ID, -1);

            if (_sessionId >= 0) {

                //Create new run
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        _run = new Run();
                        _runId = RealmHelper.setAutoId(_run, Run.class);
                        _run.setRunConfig(RunController.getInstance().getRunConfig());
                        _run.start();

                        _run = realm.copyToRealmOrUpdate(_run);
                    }
                });

                //Start GPS service
                GpsLocationService.getInstance().start(this);

                //Start http-get task
                startUpdateTask();

                //Change state to ACTIVE
                _state = State.ACTIVE;
                RunController.getInstance().notifySessionStarted();

                //Build foreground notification
                Notification notification = buildNotification();
                startForeground(ServiceConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

            } else {
                stopSelf();
            }

        } else if (intent.getAction().equals(ServiceConstants.ACTION.PAUSE_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Pause Foreground Intent ");

            //Pause or unpause
            if (!_run.isStopped()) {
                if (_state == State.ACTIVE) {
                    _state = State.PAUSED;
                    RunController.getInstance().notifySessionPaused();
                } else {
                    _state = State.ACTIVE;
                    RunController.getInstance().notifySessionStarted();
                }
            }

            updateNotification();

        } else if (intent.getAction().equals(ServiceConstants.ACTION.STOP_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");

            _state = State.STOPPED;
            RunController.getInstance().notifySessionStopped();

            //Attach run to session
            if (_run != null) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        _run.stop();

                        RunController.getInstance().calcArea(_runId);

                        Session session = (Session) RealmHelper.findWhereId(Session.class, _sessionId);
                        session.getRuns().add(_run);
                        realm.copyToRealmOrUpdate(session);
                    }
                });
            }

            if (_debugTimer != null) {
                _debugTimer.cancel();
            }

            stopForeground(true);
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GpsLocationService.getInstance().stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private Notification buildNotification() {
        Intent mainIntent = new Intent(this, ActiveRunActivity.class);
        mainIntent.setAction(ServiceConstants.ACTION.MAIN_ACTION);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.putExtra(IntentUtils.EXTRA_SESSION_ID, _sessionId);
        mainIntent.putExtra(IntentUtils.EXTRA_STATE_ID, _state);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ActiveRunActivity.class);
        stackBuilder.addNextIntent(mainIntent);
        //PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
        PendingIntent mainPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent(this, DataCrawlerService.class);
        stopIntent.setAction(ServiceConstants.ACTION.STOP_FOREGROUND_ACTION);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, 0);

        Intent pauseIntent = new Intent(this, DataCrawlerService.class);
        pauseIntent.setAction(ServiceConstants.ACTION.PAUSE_FOREGROUND_ACTION);
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        String pauseText = getString(R.string.pause);
        int pauseRes = R.drawable.ic_pause_black_48dp;
        if (_state == State.PAUSED) {
            pauseText = getString(R.string.continuee);
            pauseRes = R.drawable.ic_play_arrow_black_48dp;
        }

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_logo);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.session_running))
                .setContentText(TimeUtils.formatDate(TimeUtils.convertTime(System.currentTimeMillis()), "dd.MM.yyyy"))
                .setSmallIcon(R.drawable.ic_play_arrow_white_48dp)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLargeIcon(icon)
                .setOngoing(true)
                .setContentIntent(mainPendingIntent)
                //.addAction(pauseRes, pauseText, pausePendingIntent)
                .addAction(R.drawable.ic_stop_black_48dp, getString(R.string.stop), stopPendingIntent)
                .build();

        return notification;
    }

    private void updateNotification() {
        Notification notification = buildNotification();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ServiceConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onConnectionResponse(HttpResponse httpResponse) {
        if ( (_state == State.ACTIVE | _state == State.PAUSED)
                && httpResponse.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
            Log.d(LOG_TAG, "reconnect");
            startUpdateTask();
        }
    }

    @Override
    public void onTaskOutput(final DataPoint dataPoint) {
        if (dataPoint != null) {

            final Run run = (Run) RealmHelper.findWhereId(Run.class, _runId);

            //Do not add useless DataPoints
            if (dataPoint.getDataByPurpose(SensorPurpose.EJECTION).getValue() <= 0.0) {
                return;
            }

            if (run != null && run.isActive() && _state == State.ACTIVE) {
                if (RunController.getInstance().isPhoneGps(this)) {

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Location location = new Location(GpsLocationService.getInstance().getCurrentLocation());
                            if (location.getLatitude() != 0 & location.getLongitude() != 0) {
                                RealmHelper.setAutoId(location, Location.class);
                                location = realm.copyToRealm(location);
                                dataPoint.setLocation(location);
                            }
                        }
                    });

                }

                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmHelper.setAutoId(dataPoint, DataPoint.class);
                        run.addDataPoint(dataPoint);
                    }
                });

                RunController.getInstance().notifyDataPointAdded(_runId, dataPoint);

            }
        }
    }

    private void startUpdateTask() {
        if (RunController.getInstance().isDebugSession(this)) {
            _debugTimer = new Timer();
            _debugTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new Handler(DataCrawlerService.this.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onTaskOutput(DataPoint.Dummy.create());
                        }
                    });
                }
            }, 0, RunController.getInstance().getUpdateInterval());
        } else {
            HttpReadSensorDataAsyncTask task = new HttpReadSensorDataAsyncTask(
                    this,
                    RunController.getInstance().getApiVersion(this),
                    RunController.getInstance().getUpdateInterval(),
                    RunController.getInstance().getIpAddress(this)
            );
            task.addOnTaskOutputListener(this);
            task.addOnConnectionResponseListener(this);
            task.addOnConnectionResponseListener(RunController.getInstance());
            RunController.getInstance().addOnStateChangedListener(task);
            task.execute();
            Log.d(LOG_TAG, "AsyncTask started");
        }
    }

}
