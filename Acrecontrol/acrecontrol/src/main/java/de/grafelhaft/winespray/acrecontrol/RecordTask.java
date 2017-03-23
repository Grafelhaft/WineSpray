package de.grafelhaft.winespray.acrecontrol;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author Markus Graf (Grafelhaft) on 30.10.2016.
 */

public abstract class RecordTask extends AsyncTask<Void, IPoint, List<? extends IPoint>> {

    private boolean _isRecording = true;
    private int _sleepTime = 1000;

    private List<OnPointRecordedListener> _onPointRecordedListeners = new ArrayList<>();
    private List<OnResultListener> _onResultListeners = new ArrayList<>();

    public RecordTask() {

    }

    public RecordTask(int sleepTime) {
        this._sleepTime = sleepTime;
    }

    @Override
    protected void onPostExecute(List<? extends IPoint> points) {
        super.onPostExecute(points);
        for (OnResultListener l : _onResultListeners) {
            l.onResult(points);
        }
    }

    @Override
    protected void onProgressUpdate(IPoint... values) {
        super.onProgressUpdate(values);
        for (OnPointRecordedListener l : _onPointRecordedListeners) {
            l.onPointRecorded(values[0]);
        }
    }

    public boolean isRecording() {
        return this._isRecording;
    }

    public int getSleepTime() {
        return this._sleepTime;
    }

    public void stop() {
        this._isRecording = false;
    }


    public interface OnPointRecordedListener {
        void onPointRecorded(IPoint point);
    }

    public void addOnPointRecordedListener(OnPointRecordedListener listener) {
        _onPointRecordedListeners.add(listener);
    }

    public void removeOnPointRecoredListener(OnPointRecordedListener listener) {
        _onPointRecordedListeners.remove(listener);
    }


    public interface OnResultListener {
        void onResult(List<? extends IPoint> result);
    }

    public void addOnResultListener(OnResultListener listener) {
        _onResultListeners.add(listener);
    }

    public void removeOnResultListener(OnResultListener listener) {
        _onResultListeners.remove(listener);
    }
}
