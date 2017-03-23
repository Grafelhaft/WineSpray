package de.grafelhaft.winespray.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Markus on 09.10.2016.
 */

public class Parcel extends RealmObject implements IModel<Parcel> {

    @PrimaryKey
    private Long id;

    private Long numerator;
    private Integer denominator;

    private Area area;

    private long rpDate;

    private int type;
    private String fall;
    private Grape grape;


    public Parcel() {
    }


    public Long getId() {
        return id;
    }

    public Parcel setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return this;
    }


    public Long getNumerator() {
        return numerator;
    }

    public Parcel setNumerator(Long numerator) {
        this.numerator = numerator;
        return this;
    }


    public Integer getDenominator() {
        return denominator;
    }

    public Parcel setDenominator(Integer denominator) {
        this.denominator = denominator;
        return this;
    }


    public Area getArea() {
        return area;
    }

    public Parcel setArea(Area area) {
        this.area = area;
        return this;
    }


    public int getType() {
        return type;
    }

    public Parcel setType(int type) {
        this.type = type;
        return this;
    }


    public String getFall() {
        return fall;
    }

    public Parcel setFall(String fall) {
        this.fall = fall;
        return this;
    }


    public Grape getGrape() {
        return grape;
    }

    public Parcel setGrape(Grape grape) {
        this.grape = grape;
        return this;
    }


    public long getRpDate() {
        return rpDate;
    }

    public Parcel setRpDate(long rpDate) {
        this.rpDate = rpDate;
        return this;
    }


    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }

}
