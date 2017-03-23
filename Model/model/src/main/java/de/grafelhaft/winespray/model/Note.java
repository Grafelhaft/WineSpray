package de.grafelhaft.winespray.model;

import org.json.JSONException;
import org.json.JSONObject;

import de.grafelhaft.winespray.model.json.IJsonParsable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by @author Markus Graf (Grafelhaft) on 12.10.2016.
 */

public class Note extends RealmObject implements IModel<Note>, IJsonParsable<Note> {

    @PrimaryKey
    private Long id;

    private String content;
    private long timeStamp;
    private boolean isChecked;

    private Location location;


    public Note() {
        this.timeStamp = System.currentTimeMillis();
    }


    public Long getId() {
        return id;
    }

    public Note setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public Location getLocation() {
        return location;
    }

    public Note setLocation(Location location) {
        this.location = location;
        return this;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public Note setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public Note setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }


    public String getContent() {
        return content;
    }

    public Note setContent(String content) {
        this.content = content;
        return this;
    }


    @Override
    public Note fromJSON(JSONObject jsonObject) {
        long id = jsonObject.optLong("id");
        String content = jsonObject.optString("content");
        long timeStamp = jsonObject.optLong("timeStamp");
        double lat = jsonObject.optDouble("latitude");
        double lng = jsonObject.optDouble("longitude");

        return new Note()
                .setId(id)
                .setContent(content)
                .setTimeStamp(timeStamp)
                .setLocation(new Location()
                        .setLatitude(lat)
                        .setLongitude(lng));
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("id", this.id);
        jsonObject.putOpt("content", this.content);
        jsonObject.putOpt("timeStamp", this.timeStamp);
        jsonObject.putOpt("latitude", this.location.getLatitude());
        jsonObject.putOpt("longitude", this.location.getLongitude());
        return jsonObject;
    }
}
