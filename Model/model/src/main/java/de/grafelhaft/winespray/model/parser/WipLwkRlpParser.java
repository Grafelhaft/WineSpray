package de.grafelhaft.winespray.model.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Area;
import de.grafelhaft.winespray.model.Company;
import de.grafelhaft.winespray.model.District;
import de.grafelhaft.winespray.model.EinzelLage;
import de.grafelhaft.winespray.model.Grape;
import de.grafelhaft.winespray.model.GrossLage;
import de.grafelhaft.winespray.model.Parcel;
import de.grafelhaft.winespray.model.Region;
import de.grafelhaft.winespray.model.Zone;


/**
 * Created by @author Markus Graf (Grafelhaft) on 10.10.2016.
 */


public class WipLwkRlpParser extends Parser<DataSet> {

    private final static String SEPARATOR = ";";

    private Map<String, String>[] data;

    private Map<Long, Acre> acreMap = new HashMap<>();
    private Map<String, Parcel> parcelMap = new HashMap<>();
    private Map<Long, Company> companyMap = new HashMap<>();
    private Map<Long, District> districtMap = new HashMap<>();
    private Map<Long, Region> regionMap = new HashMap<>();
    private Map<Long, Zone> zoneMap = new HashMap<>();
    private Map<Long, GrossLage> grossLageMap = new HashMap<>();
    private Map<Long, EinzelLage> einzelLageMap = new HashMap<>();
    private Map<Long, Grape> grapeMap = new HashMap<>();

    public WipLwkRlpParser(String s) {
        super(s);
    }

    @Override
    public DataSet parse() throws ParserException {
        try {
            String[] lines = parsable.contains("\r\n") ? parsable.split("\r\n") : parsable.split("\n");
            String[] headers = lines[0].split(SEPARATOR);
            data = new Map[lines.length - 1];

            for (int i = 1; i < lines.length; i++) {

                Map<String, String> lineData = new HashMap<>(headers.length);
                String[] values = lines[i].split(SEPARATOR);

                for (int j = 0; j < values.length; j++) {
                    lineData.put(headers[j], values[j]);
                }

                data[i - 1] = lineData;
            }

            for (Map<String, String> m : data) {
                createParcel(m);
                createRegion(m);
            }

            DataSet dataSet = new DataSet();
            dataSet.setAcres(new ArrayList<>(acreMap.values()));
            dataSet.setParcels(new ArrayList<>(parcelMap.values()));
            dataSet.setCompanies(new ArrayList<>(companyMap.values()));
            dataSet.setDistricts(new ArrayList<>(districtMap.values()));
            dataSet.setRegions(new ArrayList<>(regionMap.values()));
            dataSet.setZones(new ArrayList<>(zoneMap.values()));
            dataSet.setGrossLagen(new ArrayList<>(grossLageMap.values()));
            dataSet.setEinzelLagen(new ArrayList<>(einzelLageMap.values()));
            dataSet.setGrapes(new ArrayList<>(grapeMap.values()));
            return dataSet;
        }
        catch (Exception e) {
            throw new ParserException(e.getMessage());
        }
    }

    private Acre createAcre(Map<String, String> map) {
        long acreNumber;
        try {
            acreNumber = Long.parseLong(map.get(WipLwkRlpAttributes.ACRE_NUMBER));
        } catch (Exception e) {
            return null;
        }

        String districtName = map.get(WipLwkRlpAttributes.DISTRICT_NAME);

        Acre acre;
        if (!acreMap.containsKey(acreNumber)) {
            acre = new Acre()
                    .setId(acreNumber);

            acreMap.put(acreNumber, acre);
        } else {
            acre = acreMap.get(acreNumber);
        }
        return acre;
    }

    private Parcel createParcel(Map<String, String> map) {
        long acreNumber = -1;
        try {
            acreNumber = Long.parseLong(map.get(WipLwkRlpAttributes.ACRE_NUMBER));
        } catch (Exception e) {
        }

        long numerator;
        try {
            numerator = Long.parseLong(map.get(WipLwkRlpAttributes.PARCEL_NUMBER));
        } catch (Exception e) {
            return null;
        }

        int denominator = -1;
        try {
            denominator = Integer.parseInt(map.get(WipLwkRlpAttributes.PARCEL_NUMBER_EXTRA));
        } catch (Exception e) {
        }

        long companyId = -1;
        try {
            companyId = Long.parseLong(map.get(WipLwkRlpAttributes.COMPANY_NUMBER));
        } catch (Exception e) {
        }

        int grapeId = -1;
        try {
            grapeId = Integer.parseInt(map.get(WipLwkRlpAttributes.GRAPE_ID));
        } catch (Exception e) {
        }

        long districtId = -1;
        try {
            districtId = Long.parseLong(map.get(WipLwkRlpAttributes.DISTRICT_NUMBER));
        } catch (Exception e) {
        }

        Area area = createArea(map);
        String fall = map.get(WipLwkRlpAttributes.PARCEL_FALL_TYPE).toUpperCase();
        Grape grape = createGrape(map);

        Parcel parcel = new Parcel()
                .setId(districtId * 10000 + numerator * 10 + denominator)
                .setNumerator(numerator)
                .setDenominator(denominator)
                .setArea(area)
                .setGrape(grape)
                .setFall(fall);


        Acre acre = createAcre(map);
        if (acre != null) {
            createCompany(map);
            acre.setCompany(companyMap.get(companyId));
            createDistrict(map);
            acre.setDistrict(districtMap.get(districtId));
            acre.setName(districtMap.get(districtId).getName());
        }
        acreMap.get(acreNumber).getParcels().add(parcel);

        parcelMap.put(parcel.getNumerator() + "/" + parcel.getDenominator(), parcel);
        return parcel;
    }

    private District createDistrict(Map<String, String> map) {
        long districtNumber;
        try {
            districtNumber = Long.parseLong(map.get(WipLwkRlpAttributes.DISTRICT_NUMBER));
        } catch (Exception e) {
            return null;
        }

        long regionNumber;
        try {
            regionNumber = Long.parseLong(map.get(WipLwkRlpAttributes.REGION_ID));
        } catch (Exception e) {
            return null;
        }

        District district;
        if (!districtMap.containsKey(districtNumber)) {
            String districtName = map.get(WipLwkRlpAttributes.DISTRICT_NAME);

            district = new District()
                    .setId(districtNumber)
                    .setName(districtName);

            districtMap.put(districtNumber, district);
        } else {
            district = districtMap.get(districtNumber);
        }

        createRegion(map);
        district.getRegions().add(regionMap.get(regionNumber));

        return district;
    }

    private Grape createGrape(Map<String, String> map) {
        long grapeId;
        try {
            grapeId = Long.parseLong(map.get(WipLwkRlpAttributes.GRAPE_ID));
        } catch (Exception e) {
            return null;
        }

        Grape grape;
        if (!grapeMap.containsKey(grapeId)) {
            String grapeName = map.get(WipLwkRlpAttributes.GRAPE_NAME);
            String color = map.get(WipLwkRlpAttributes.GRAPE_COLOR).toLowerCase();

            grape = new Grape()
                    .setId(grapeId)
                    .setName(grapeName)
                    .setColor(color);

            grapeMap.put(grapeId, grape);
        } else {
            grape = grapeMap.get(grapeId);
        }
        return grape;
    }

    private Company createCompany(Map<String, String> map) {
        long companyNumber;
        try {
            companyNumber = Long.parseLong(map.get(WipLwkRlpAttributes.COMPANY_NUMBER));
        } catch (Exception e) {
            return null;
        }

        Company company;
        if (!companyMap.containsKey(companyNumber)) {
            company = new Company()
                    .setId(companyNumber);

            companyMap.put(companyNumber, company);
        } else {
            company = companyMap.get(companyNumber);
        }
        return company;
    }

    private Region createRegion(Map<String, String> map) {
        long number;
        try {
            number = Long.parseLong(map.get(WipLwkRlpAttributes.REGION_ID));
        } catch (Exception e) {
            return null;
        }

        long zoneNumber = -1;
        try {
            zoneNumber = Long.parseLong(map.get(WipLwkRlpAttributes.ZONE_ID));
        } catch (Exception e) {
        }

        Region region;
        if (!regionMap.containsKey(number)) {
            String name = map.get(WipLwkRlpAttributes.REGION_NAME);

            region = new Region()
                    .setName(name)
                    .setId(number);

            regionMap.put(number, region);
        } else {
            region = regionMap.get(number);
        }

        createZone(map);
        region.getZones().add(zoneMap.get(zoneNumber));

        return region;
    }

    private Zone createZone(Map<String, String> map) {
        long zoneNumber;
        try {
            zoneNumber = Long.parseLong(map.get(WipLwkRlpAttributes.ZONE_ID));
        } catch (Exception e) {
            return null;
        }

        long grossLageNumber = -1;
        try {
            grossLageNumber = Long.parseLong(map.get(WipLwkRlpAttributes.SITE_BIG_ID));
        } catch (Exception e) {
        }

        Zone zone;
        if (!zoneMap.containsKey(zoneNumber)) {

            String name = map.get(WipLwkRlpAttributes.ZONE_NAME);

            zone = new Zone()
                    .setId(zoneNumber)
                    .setName(name);

            zoneMap.put(zoneNumber, zone);
        } else {
            zone = zoneMap.get(zoneNumber);
        }

        createGrossLage(map);
        zone.getGrossLagen().add(grossLageMap.get(grossLageNumber));

        return zone;
    }

    private GrossLage createGrossLage(Map<String, String> map) {
        long grossLageNumber;
        try {
            grossLageNumber = Long.parseLong(map.get(WipLwkRlpAttributes.SITE_BIG_ID));
        } catch (Exception e) {
            return null;
        }

        long einzelLageNumber = -1;
        try {
            einzelLageNumber = Long.parseLong(map.get(WipLwkRlpAttributes.SITE_ID));
        } catch (Exception e) {
        }

        GrossLage grossLage;
        if (!grossLageMap.containsKey(grossLageNumber)) {

            String name = map.get(WipLwkRlpAttributes.SITE_BIG_NAME);

            grossLage = new GrossLage().setId(grossLageNumber)
                    .setName(name);

            grossLageMap.put(grossLageNumber, grossLage);
        } else {
            grossLage = grossLageMap.get(grossLageNumber);
        }

        createEinzelLage(map);
        grossLage.getEinzelLagen().add(einzelLageMap.get(einzelLageNumber));

        return grossLage;
    }

    private EinzelLage createEinzelLage(Map<String, String> map) {
        long einzelLageNumber;
        try {
            einzelLageNumber = Long.parseLong(map.get(WipLwkRlpAttributes.SITE_ID));
        } catch (Exception e) {
            return null;
        }

        EinzelLage einzelLage;
        if (!einzelLageMap.containsKey(einzelLageNumber)) {
            String name = map.get(WipLwkRlpAttributes.SITE_NAME);

            einzelLage = new EinzelLage()
                    .setId(einzelLageNumber)
                    .setName(name);

            einzelLageMap.put(einzelLageNumber, einzelLage);
        } else {
            einzelLage = einzelLageMap.get(einzelLageNumber);
        }
        return einzelLage;
    }

    private Area createArea(Map<String, String> map) {
        double area = Double.parseDouble(map.get(WipLwkRlpAttributes.PARCEL_AREA));
        double areaUseful = Double.parseDouble(map.get(WipLwkRlpAttributes.PARCEL_AREA_USEFUL));

        return new Area()
                .setSize(area)
                .setSizeUseful(areaUseful);
    }

}

