package de.grafelhaft.winespray.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Zone extends RealmObject implements IModel<Zone> {

    @PrimaryKey
    private Long id;

    private String name;

    private RealmList<GrossLage> grossLagen = new RealmList<>();


    public Zone() {
    }


    public Long getId() {
        return id;
    }

    public Zone setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public String getName() {
        return name;
    }

    public Zone setName(String name) {
        this.name = name;
        return this;
    }


    public List<GrossLage> getGrossLagen() {
        return grossLagen;
    }

    public Zone setGrossLagen(List<GrossLage> grossLagen) {
        this.grossLagen.addAll(grossLagen);
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
