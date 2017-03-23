package de.grafelhaft.winespray.model.parser;

/**
 * Created by Markus on 07.10.2016.
 */

public interface WipLwkRlpAttributes {
    String COMPANY_NUMBER = "BETRNR";

    String ACRE_NUMBER = "FLUR";

    String PARCEL_NUMBER = "Z";
    String PARCEL_NUMBER_EXTRA = "N";
    String PARCEL_AREA = "FLU_QM";
    String PARCEL_AREA_USEFUL = "FLA_QM";
    String PARCEL_FALL_TYPE = "WBR_H_F";
    String PARCEL_FALL_TYPE_FLAT = "F";
    String PARCEL_FALL_TYPE_PENDING = "H";
    String PARCEL_TYPE_OF_USE = "NUTZUNGSART";

    String DISTRICT_NUMBER = "GMK_NR";
    String DISTRICT_NAME = "GEMARKUNG";
    String DISTRICT_COMMUNITY_NAME = "GMK_GEMEINDE";

    String REGION_ID = "BA";
    String REGION_NAME = "BA_NAME";

    String ZONE_ID = "BER";
    String ZONE_NAME = "BER_NAME";

    String SITE_BIG_ID = "GL";
    String SITE_BIG_NAME = "GL_NAME";
    String SITE_ID = "EL";
    String SITE_NAME = "EL_NAME";

    String GRAPE_ID = "REB_ID";
    String GRAPE_NAME = "REB_NAME";
    String GRAPE_BASE_ID = "UNL_ID";
    String GRAPE_BASE_NAME = "UNTERLAGE";
    String GRAPE_COLOR = "FARBE";
    String GRAPE_COLOR_WHITE = "weiß";
    String GRAPE_COLOR_RED = "rot";

}
