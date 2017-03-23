package de.grafelhaft.winespray.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Session extends RealmObject implements IModel<Session> {

    @PrimaryKey
    private Long id;

    private long startTime;
    private long endTime;

    private String name;

    private int state;

    private RealmList<Run> runs = new RealmList<>();


    public Session() {
        this.startTime = System.currentTimeMillis();
    }


    public Long getId() {
        return id;
    }

    public Session setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public long getStartTime() {
        return startTime;
    }

    public Session setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }


    public long getEndTime() {
        return endTime;
    }

    public Session setEndTime(long endTime) {
        this.endTime = endTime;
        return this;
    }


    public String getName() {
        return name;
    }

    public Session setName(String name) {
        this.name = name;
        return this;
    }


    public List<Run> getRuns() {
        return runs;
    }

    public Session setRuns(List<Run> runs) {
        this.runs.addAll(runs);
        return this;
    }


    public int getSstatate() {
        return state;
    }

    public Session setState(int state) {
        this.state = state;
        return this;
    }
}
