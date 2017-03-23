package de.grafelhaft.winespray.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class District extends RealmObject implements IModel<District> {

    @PrimaryKey
    private Long id;

    private String name;

    private RealmList<Region> regions = new RealmList<>();


    public District() {
    }


    public Long getId() {
        return id;
    }

    public District setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public String getName() {
        return name;
    }

    public District setName(String name) {
        this.name = name;
        return this;
    }


    public RealmList<Region> getRegions() {
        return regions;
    }

    public District setRegions(RealmList<Region> regions) {
        this.regions = regions;
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
