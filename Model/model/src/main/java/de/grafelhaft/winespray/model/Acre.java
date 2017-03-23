package de.grafelhaft.winespray.model;

import java.util.List;

import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Acre extends RealmObject implements IModel<Acre> {

    @PrimaryKey
    private Long id;

    private String name;

    private Company company;
    private District district;
    private Area area;

    private RealmList<Parcel> parcels = new RealmList<>();

    public Acre() {
    }


    public Long getId() {
        return id;
    }

    public Acre setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public Company getCompany() {
        return company;
    }

    public Acre setCompany(Company company) {
        this.company = company;
        return this;
    }


    public District getDistrict() {
        return district;
    }

    public Acre setDistrict(District district) {
        this.district = district;
        return this;
    }


    public List<Parcel> getParcels() {
        return parcels;
    }

    public Acre setParcels(List<Parcel> parcels) {
        this.parcels.addAll(parcels);
        return this;
    }


    public Area getArea() {
        if (area != null) {
            return area;
        } else if (parcels.size() > 0){
            double size = 0;
            double sizeUseful = 0;
            for (Parcel p : parcels) {
                if (p.getArea() != null) {
                    size += p.getArea().getSize();
                    sizeUseful += p.getArea().getSizeUseful();
                }
            }
            return new Area()
                    .setSize(size)
                    .setSizeUseful(sizeUseful);
        } else {
            return null;
        }
    }

    public Acre setArea(Area area) {
        clearArea();
        this.area = area;
        return this;
    }

    public Acre clearArea() {
        if (this.isManaged() && this.area != null) {
            this.area.deleteFromRealm();
        }
        this.area = null;
        return this;
    }


    public String getName() {
        return name;
    }

    public Acre setName(String name) {
        this.name = name;
        return this;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
