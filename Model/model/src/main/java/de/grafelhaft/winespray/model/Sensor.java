package de.grafelhaft.winespray.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.grafelhaft.winespray.model.json.IJsonParsable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Sensor extends RealmObject implements IModel<Sensor>, IJsonParsable<Sensor> {

    @PrimaryKey
    private Long id;

    private String description;

    private String unit;
    private int purpose;


    public Sensor() {
    }

    public Sensor(int purpose, String unit) {
        this.unit = unit;
        this.purpose = purpose;
    }


    public Long getId() {
        return id;
    }

    public Sensor setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public Sensor setDescription(String description) {
        this.description = description;
        return this;
    }


    public String getUnit() {
        return unit;
    }

    public Sensor setUnit(String unit) {
        this.unit = unit;
        return this;
    }


    public int getPurpose() {
        return purpose;
    }

    public Sensor setPurpose(int purpose) {
        this.purpose = purpose;
        return this;
    }

    @Override
    public String toString() {
        return this.description;
    }


    @Override
    public Sensor fromJSON(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("id", this.id);
        jsonObject.putOpt("description", this.description);
        jsonObject.putOpt("purpose", this.purpose);
        jsonObject.putOpt("unit", this.unit);
        return jsonObject;
    }
}
