package de.grafelhaft.winespray.model.json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Markus on 09.10.2016.
 */

public interface IJsonParsable<Type> {

    Type fromJSON(JSONObject jsonObject);

    JSONObject toJSON() throws JSONException;

}
