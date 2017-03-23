package de.grafelhaft.winespray.api;

/**
 * Created by @author Markus Graf (Grafelhaft) on 03.02.2017.
 */

public class Response implements IResponse {

    private String _header, _body;
    private int _code;

    public Response(String header, String body, int code) {
        this._header = header;
        this._body = body;
        this._code = code;
    }

    @Override
    public String header() {
        return this._header;
    }

    @Override
    public String body() {
        return this._body;
    }

    @Override
    public int code() {
        return this._code;
    }
}
