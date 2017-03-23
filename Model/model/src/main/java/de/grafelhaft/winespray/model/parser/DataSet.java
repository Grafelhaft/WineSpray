package de.grafelhaft.winespray.model.parser;

import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Company;
import de.grafelhaft.winespray.model.District;
import de.grafelhaft.winespray.model.EinzelLage;
import de.grafelhaft.winespray.model.Grape;
import de.grafelhaft.winespray.model.GrossLage;
import de.grafelhaft.winespray.model.Parcel;
import de.grafelhaft.winespray.model.Region;
import de.grafelhaft.winespray.model.Zone;

/**
 * Created by @author Markus Graf (Grafelhaft) on 13.10.2016.
 */

public class DataSet {

    private List<Acre> _acres = new ArrayList<>();
    private List<Parcel> _parcels = new ArrayList<>();
    private List<District> _districts = new ArrayList<>();
    private List<Region> _regions = new ArrayList<>();
    private List<Zone> _zones = new ArrayList<>();
    private List<GrossLage> _bigSites = new ArrayList<>();
    private List<EinzelLage> _sites = new ArrayList<>();
    private List<Company> _companies = new ArrayList<>();
    private List<Grape> _grapes = new ArrayList<>();

    public DataSet() {

    }


    public List<Acre> getAcres() {
        return _acres;
    }

    public DataSet setAcres(List<Acre> acres) {
        this._acres = acres;
        return this;
    }

    public List<Parcel> getParcels() {
        return _parcels;
    }

    public DataSet setParcels(List<Parcel> parcels) {
        this._parcels = parcels;
        return this;
    }

    public List<District> getDistricts() {
        return _districts;
    }

    public DataSet setDistricts(List<District> districts) {
        this._districts = districts;
        return this;
    }

    public List<Region> getRegions() {
        return _regions;
    }

    public DataSet setRegions(List<Region> regions) {
        this._regions = regions;
        return this;
    }

    public List<Zone> getZones() {
        return _zones;
    }

    public DataSet setZones(List<Zone> zones) {
        this._zones = zones;
        return this;
    }

    public List<GrossLage> getGrossLagen() {
        return _bigSites;
    }

    public DataSet setGrossLagen(List<GrossLage> bigSites) {
        this._bigSites = bigSites;
        return this;
    }

    public List<EinzelLage> getEinzelLage() {
        return _sites;
    }

    public DataSet setEinzelLagen(List<EinzelLage> sites) {
        this._sites = sites;
        return this;
    }

    public List<Company> getCompanies() {
        return _companies;
    }

    public DataSet setCompanies(List<Company> companies) {
        this._companies = companies;
        return this;
    }

    public List<Grape> getGrapes() {
        return _grapes;
    }

    public DataSet setGrapes(List<Grape> grapes) {
        this._grapes = grapes;
        return this;
    }
    
}
