package de.grafelhaft.winespray.api;

import com.android.volley.*;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import de.grafelhaft.winespray.api.util.ConnectionUtils;

/**
 * Created by @author Markus Graf (Grafelhaft) on 25.06.2017.
 */

class RequestBuilder {

    public static JsonObjectRequest build(Api api, String ipAddress, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        String url = getUrl(api, ipAddress);

        return new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response,
                error
        );
    }

    private static String getUrl(Api api, String ipAddress) {
        String url = ConnectionUtils.isIpAddressValid(ipAddress) ? ipAddress : api.DefaultIP();
        return api.URL(url);
    }
}
