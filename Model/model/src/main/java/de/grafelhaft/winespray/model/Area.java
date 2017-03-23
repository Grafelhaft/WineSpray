package de.grafelhaft.winespray.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.winespray.acrecontrol.Constants;
import de.grafelhaft.winespray.acrecontrol.IPoint;
import de.grafelhaft.winespray.acrecontrol.IPolygon;
import de.grafelhaft.winespray.acrecontrol.Point;
import de.grafelhaft.winespray.acrecontrol.Polygon;
import de.grafelhaft.winespray.acrecontrol.PolygonBuilder;
import de.grafelhaft.winespray.model.json.IJsonParsable;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by @author Markus Graf (Grafelhaft) on 11.10.2016.
 */

@RealmClass
public class Area extends RealmObject implements IModel<Area>, IJsonParsable<Area>, IPolygon<Location> {

    private Long id;

    private double size;
    private double sizeUseful;

    private RealmList<Location> locations = new RealmList<>();


    public Area() {

    }


    public Long getId() {
        return id;
    }

    public Area setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public List<Location> getLocations() {
        return locations;
    }

    public Area setLocations(List<Location> locations) {
        clearLocations();
        this.locations.addAll(locations);
        return this;
    }

    public Area clearLocations() {
        if (this.isManaged() && this.locations.size() > 0) {
            for (Location l : this.locations) {
                l.deleteFromRealm();
            }
        }
        this.locations.clear();
        return this;
    }


    public double getSize() {
        if (this.size <= 0 && this.locations.size() > 0) {
            /*Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    size = new PolygonBuilder(locations, Constants.UNIT_DEGREES).setOutput(Constants.UNIT_METER).build().area();
                }
            });*/
            return new PolygonBuilder(locations, Constants.UNIT_DEGREES).setOutput(Constants.UNIT_METER).build().area();
        }
        return size;
    }

    public Area setSize(double area) {
        this.size = area;
        return this;
    }


    public double getSizeUseful() {
        if (this.sizeUseful <= 0 && this.locations.size() > 0) {
            /*Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    sizeUseful = new PolygonBuilder(locations, Constants.UNIT_DEGREES).setOutput(Constants.UNIT_METER).build().area();
                }
            });*/
            return new PolygonBuilder(locations, Constants.UNIT_DEGREES).setOutput(Constants.UNIT_METER).build().area();
        }
        return sizeUseful;
    }

    public Area setSizeUseful(double areaUseful) {
        this.sizeUseful = areaUseful;
        return this;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Area) {
            Area area = (Area) obj;
            if (this.getLocations().size() > 0 && area.getLocations().size() > 0) {
                boolean equal = false;
                for (int i = 0; i < this.locations.size() & i < area.getLocations().size(); i++) {
                    if (this.getLocations().get(i).getLatitude() == area.getLocations().get(i).getLatitude() &
                            this.getLocations().get(i).getLongitude() == area.getLocations().get(i).getLongitude()) {
                        equal = true;
                    } else {
                        equal = false;
                    }
                }
                return equal;
            }
        }
        return super.equals(obj);
    }


    @Override
    public Area fromJSON(JSONObject jsonObject) {
        double size = jsonObject.optDouble("size");
        double size_u = jsonObject.optDouble("size_useful");

        List<Location> locations = new ArrayList<>();
        JSONArray locs = jsonObject.optJSONArray("locations");
        if (locs != null) {
            for (int i = 0; i < locs.length(); i++) {
                try {
                    locations.add(new Location().fromJSON(locs.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return new Area()
                .setSize(size)
                .setSizeUseful(size_u)
                .setLocations(locations);
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt("size", this.size);
        jsonObject.putOpt("size_useful", this.sizeUseful);

        JSONArray locs = new JSONArray();
        for (Location l : locations) {
            locs.put(l.toJSON());
        }
        jsonObject.putOpt("locations", locs);
        return jsonObject;
    }


    @Override
    public Location[] getPoints() {
        return this.locations.toArray(new Location[this.locations.size()]);
    }

    @Override
    public double area() {
        return this.sizeUseful;
    }

    @Override
    public boolean contains(IPoint point) {
        return Polygon.contains(this, point);
    }

    @Override
    public Location centroid() {
        Point point = (Point) Polygon.centroid(this);
        return new Location(point.x(), point.y());
    }

    @Override
    public void scale(double scale) {
        for (Location l : this.locations) {
            IPoint point = Polygon.scale(centroid(), l, scale);
            l.setLatitude(point.x());
            l.setLongitude(point.y());
        }
    }

}
