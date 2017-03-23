package de.grafelhaft.winespray.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class RunConfig extends RealmObject implements IModel<RunConfig> {

    @PrimaryKey
    private Long id;

    private int updateInterval;
    private int nozzleCount;
    private double workingWidth;

    public RunConfig() {

    }

    public RunConfig(int updateInterval, int nozzleCount, double workingWidth) {
        this.updateInterval = updateInterval;
        this.nozzleCount = nozzleCount;
        this.workingWidth = workingWidth;
    }


    public Long getId() {
        return id;
    }

    public RunConfig setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public int getUpdateInterval() {
        return updateInterval;
    }

    public RunConfig setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
        return this;
    }


    public int getNozzleCount() {
        return nozzleCount;
    }

    public RunConfig setNozzleCount(int nozzleCount) {
        this.nozzleCount = nozzleCount;
        return this;
    }


    public double getWorkingWidth() {
        return workingWidth;
    }

    public RunConfig setWorkingWidth(double workingWidth) {
        this.workingWidth = workingWidth;
        return this;
    }

}
