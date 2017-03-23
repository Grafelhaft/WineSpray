package de.grafelhaft.winespray.api.tasks;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import de.grafelhaft.grafellib.async.http.HttpResponse;
import de.grafelhaft.winespray.api.Api;
import de.grafelhaft.winespray.api.Response;
import de.grafelhaft.winespray.api.json.JSONFactory;
import de.grafelhaft.winespray.api.util.ConnectionUtils;
import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.State;

/**
 * Created by Markus on 16.09.2016.
 */
public class HttpReadSensorDataAsyncTask extends AHttpGetAsyncTask<DataPoint, DataPoint> implements State.OnStateChangedListener {

    private Api _api;
    private int _state;
    private long _sleep;
    private String _ipAddress;

    public HttpReadSensorDataAsyncTask(Context context, Api api, long sleep) {
        this(context, api, sleep, api.DefaultIP());
    }

    public HttpReadSensorDataAsyncTask(Context context, Api api, long sleep, String ipAddress) {
        super(context, null);
        this._api = api;
        this._ipAddress = ipAddress;
        this._sleep = sleep;
        this.baseUrl = getUrl(api);
    }

    @Override
    protected Response doInBackground(Void... params) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        if (this.connect(baseUrl)) {
            try {
                while (_state == State.ACTIVE || _state == State.PAUSED) {

                    if (_state == State.ACTIVE) {
                        DataPoint dataPoint = convertResultToOutput(read());
                        publishProgress(dataPoint);
                    }

                    Thread.sleep(_sleep);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.disconnect();
            }
        } else {
            notifyConnectionResponse(new HttpResponse(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, ""));
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(DataPoint... values) {
        DataPoint dataPoint = values[0];
        onTaskOutput(dataPoint);
        Log.d("onProgressUpdate", "onProgressUpdate" + dataPoint.toString());
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(null);
    }

    @Override
    protected DataPoint convertResultToOutput(Response response) {
        if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
            JSONFactory jsonFactory = new JSONFactory(_api);
            try {
                return jsonFactory.build(new JSONObject(response.body()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getUrl(Api api) {
        String ipAddress = ConnectionUtils.isIpAddressValid(_ipAddress) ? _ipAddress : api.DefaultIP();
        return api.URL(ipAddress);
    }

    @Override
    public void onStateChanged(int state) {
        this._state = state;
    }
}
