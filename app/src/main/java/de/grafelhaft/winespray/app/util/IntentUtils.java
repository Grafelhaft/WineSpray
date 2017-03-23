package de.grafelhaft.winespray.app.util;

/**
 * Created by Markus on 15.09.2016.
 */
public class IntentUtils {
    private static final String PACKAGE_NAME = "de.grafelhaft.winespray.app";

    public static final String EXTRA_SESSION_ID = IntentUtils.class.getName() + "EXTRA_SESSION_ID";
    public static final String EXTRA_RUN_ID = IntentUtils.class.getName() + "EXTRA_RUN_ID";
    public static final String EXTRA_ACRE_ID = IntentUtils.class.getName() + "EXTRA_ACRE_ID";
    public static final String EXTRA_PARCEL_ID = IntentUtils.class.getName() + "EXTRA_PARCEL_ID";
    public static final String EXTRA_GRAPE_ID = IntentUtils.class.getName() + "EXTRA_GRAPE_ID";
    public static final String EXTRA_STATE_ID = IntentUtils.class.getName() + "EXTRA_STATE_ID";

    public static final String FILTER_IMPORT = PACKAGE_NAME + ".IMPORT";
    public static final String FILTER_EXPORT = PACKAGE_NAME + ".EXPORT";
    public static final String FILTER_CLEAR = PACKAGE_NAME + ".CLEAR";
}
