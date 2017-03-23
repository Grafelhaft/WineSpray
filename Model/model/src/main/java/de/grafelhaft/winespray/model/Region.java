package de.grafelhaft.winespray.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Region extends RealmObject implements IModel<Region> {

    @PrimaryKey
    private Long id;

    private String name;

    private RealmList<Zone> zones = new RealmList<>();


    public Region() {
    }


    public Long getId() {
        return id;
    }

    public Region setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public String getName() {
        return name;
    }

    public Region setName(String name) {
        this.name = name;
        return this;
    }


    public List<Zone> getZones() {
        return zones;
    }

    public Region setZones(List<Zone> zones) {
        this.zones.addAll(zones);
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
