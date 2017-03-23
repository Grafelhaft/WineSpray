package de.grafelhaft.winespray.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.grafelhaft.winespray.model.json.IJsonParsable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Grape extends RealmObject implements IModel<Grape>, IJsonParsable<Grape> {

    @PrimaryKey
    private Long id;

    private String name;
    private String color;


    public Grape() {
    }


    public Long getId() {
        return id;
    }

    public Grape setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public String getName() {
        return name;
    }

    public Grape setName(String name) {
        this.name = name;
        return this;
    }


    public String getColor() {
        return color;
    }

    public Grape setColor(String color) {
        this.color = color;
        return this;
    }


    @Override
    public String toString() {
        return this.name;
    }


    @Override
    public Grape fromJSON(JSONObject jsonObject) {
        long id = jsonObject.optLong("id");
        String name = jsonObject.optString("name");
        String color = jsonObject.optString("color");
        return new Grape()
                .setId(id)
                .setName(name)
                .setColor(color);
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("id", this.id);
        jsonObject.putOpt("name", this.name);
        jsonObject.putOpt("color", this.color);
        return jsonObject;
    }
}
