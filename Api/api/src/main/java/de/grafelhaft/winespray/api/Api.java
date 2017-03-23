package de.grafelhaft.winespray.api;

/**
 * Created by @author Markus Graf (Grafelhaft) on 13.10.2016.
 */

public enum Api {

    HsWormsSS16("192.168.2.4", "api/v1/data"),
    HsWormsWS1617("192.168.42.23", "api/v2/data");

    private String _defaultIP;
    private String _uri;

    Api(String ip, String uri) {
        this._defaultIP = ip;
        this._uri = uri;
    }

    public String DefaultIP() {
        return this._defaultIP;
    }

    public String URI() {
        return this._uri;
    }

    public String URL() {
        return "http://" + _defaultIP + "/" + _uri;
    }

    public String URL(String ipAddress) {
        return "http://" + ipAddress + "/" + _uri;
    }
}
