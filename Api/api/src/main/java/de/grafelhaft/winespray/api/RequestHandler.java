package de.grafelhaft.winespray.api;

import android.content.Context;

import com.android.volley.*;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author Markus Graf (Grafelhaft) on 25.06.2017.
 */

public class RequestHandler implements Response.Listener<JSONObject>, Response.ErrorListener, RequestQueue.RequestFinishedListener<JSONObject> {

    private static RequestHandler _instance;

    public static synchronized RequestHandler getInstance(Context context) {
        if (_instance == null) {
            _instance = new RequestHandler(context);
        }
        return _instance;
    }

    private Api _api;
    private String _ipAddress;

    private RequestQueue _requestQueue;

    private List<OnResponseListener> _responseListeners = new ArrayList<>();

    private RequestHandler(Context context) {
        this._requestQueue = Volley.newRequestQueue(context);
        this._requestQueue.start();
        this._requestQueue.addRequestFinishedListener(this);
    }

    public void get(Api api, String ipAddress) {
        this._api = api;
        this._ipAddress = ipAddress;

        JsonObjectRequest request = RequestBuilder.build(api, ipAddress, this, this);
        this._requestQueue.add(request);
    }

    public RequestHandler addOnResponseListener(OnResponseListener l) {
        this._responseListeners.add(l);
        return this;
    }

    @Override
    public void onResponse(JSONObject response) {
        for (OnResponseListener l : _responseListeners) {
            l.onResponse(response);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        for (OnResponseListener l : _responseListeners) {
            l.onError(error);
        }
    }

    @Override
    public void onRequestFinished(Request<JSONObject> request) {
        for (OnResponseListener l : _responseListeners) {
            l.onFinish(request);
        }
    }
}
