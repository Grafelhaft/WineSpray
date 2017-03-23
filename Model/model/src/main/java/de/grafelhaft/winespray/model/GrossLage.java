package de.grafelhaft.winespray.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class GrossLage extends RealmObject implements IModel<GrossLage> {

    @PrimaryKey
    private Long id;

    private String name;

    private RealmList<EinzelLage> einzelLagen = new RealmList<>();


    public GrossLage() {
    }


    public Long getId() {
        return id;
    }

    public GrossLage setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public String getName() {
        return name;
    }

    public GrossLage setName(String name) {
        this.name = name;
        return this;
    }


    public List<EinzelLage> getEinzelLagen() {
        return einzelLagen;
    }

    public GrossLage setEinzelLagen(List<EinzelLage> einzelLagen) {
        this.einzelLagen.addAll(einzelLagen);
        return this;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
