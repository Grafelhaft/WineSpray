package de.grafelhaft.winespray.api;

import com.android.volley.Request;

import org.json.JSONObject;

/**
 * Created by @author Markus Graf (Grafelhaft) on 25.06.2017.
 */

public interface OnResponseListener extends OnErrorListener{
    void onResponse(JSONObject jsonObject);
    void onFinish(Request<JSONObject> request);
}
