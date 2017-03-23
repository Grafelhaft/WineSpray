package de.grafelhaft.winespray.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.winespray.model.json.IJsonParsable;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Run extends RealmObject implements IModel<Run>, IJsonParsable<Run> {

    @PrimaryKey
    private Long id;

    private long startTime;
    private long endTime;

    private int state;
    private RunConfig runConfig;
    private Acre acre;

    private RealmList<DataPoint> dataPoints = new RealmList<>();


    public Run() {
        this.startTime = System.currentTimeMillis();
    }


    public Long getId() {
        return id;
    }

    public Run setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public long getStartTime() {
        return startTime;
    }

    public Run setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }


    public long getEndTime() {
        return endTime;
    }

    public Run setEndTime(long endTime) {
        this.endTime = endTime;
        return this;
    }


    public int getState() {
        return state;
    }

    public Run setState(int state) {
        this.state = state;
        return this;
    }

    public boolean isActive() {
        return getState() == State.ACTIVE;
    }

    public boolean isPaused() {
        return getState() == State.PAUSED;
    }

    public boolean isStopped() {
        return getState() == State.STOPPED;
    }


    public RunConfig getRunConfig() {
        return runConfig;
    }

    public Run setRunConfig(RunConfig runConfig) {
        this.runConfig = runConfig;
        return this;
    }


    public Acre getAcre() {
        return acre;
    }

    public Run setAcre(Acre acre) {
        this.acre = acre;
        return this;
    }


    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public Run setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints.addAll(dataPoints);
        return this;
    }

    public Run addDataPoint(DataPoint dataPoint) {
        this.dataPoints.add(dataPoint);
        return this;
    }

    public DataPoint getLastAddedDataPoint() {
        return this.dataPoints.get(dataPoints.size() -1);
    }

    public List<Location> getRoute() {
        List<Location> temp = new ArrayList<>(dataPoints.size());
        for (DataPoint d : dataPoints) {
            temp.add(d.getLocation());
        }
        return temp;
    }


    public boolean hasAcre() {
        return this.acre != null;
    }

    public boolean hasArea() {
        return hasAcre() && this.acre.getArea() != null;
    }


    /*STATE HANDLING*/

    public Run start() {
        this.state = State.ACTIVE;
        this.startTime = System.currentTimeMillis();
        return this;
    }

    public Run pause() {
        this.state = State.PAUSED;
        return this;
    }

    public Run stop() {
        this.state = State.STOPPED;
        this.endTime = System.currentTimeMillis();
        return this;
    }


    @Override
    public Run fromJSON(JSONObject jsonObject) {
        return null;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonRun = new JSONObject();
        jsonRun.putOpt("id", this.id);
        jsonRun.putOpt("start_time", this.startTime);
        jsonRun.putOpt("end_time", this.endTime);

        JSONObject jsonConfig = new JSONObject();
        jsonConfig.putOpt("id", runConfig.getId());
        jsonConfig.putOpt("interval", runConfig.getUpdateInterval());
        jsonConfig.putOpt("nozzle_count", runConfig.getNozzleCount());
        jsonConfig.putOpt("working_width", runConfig.getWorkingWidth());

        jsonRun.putOpt("config", jsonConfig);

        JSONArray jsonData = new JSONArray();
        for (DataPoint d : dataPoints) {
            jsonData.put(d.toJSON());
        }
        jsonRun.putOpt("data", jsonData);

        return jsonRun;
    }
}
