package de.grafelhaft.winespray.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.grafelhaft.winespray.model.json.IJsonParsable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class EinzelLage extends RealmObject implements IModel<EinzelLage>, IJsonParsable<EinzelLage> {

    @PrimaryKey
    private Long id;

    private String name;


    public EinzelLage() {
    }


    public Long getId() {
        return id;
    }

    public EinzelLage setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public String getName() {
        return name;
    }

    public EinzelLage setName(String name) {
        this.name = name;
        return this;
    }


    @Override
    public String toString() {
        return this.name;
    }


    @Override
    public EinzelLage fromJSON(JSONObject jsonObject) {
        long id = jsonObject.optLong("id");
        String name = jsonObject.optString("name");
        return new EinzelLage()
                .setId(id)
                .setName(name);
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("id", this.id);
        jsonObject.putOpt("name", this.name);
        return jsonObject;
    }
}
