package de.grafelhaft.winespray.api.util;

import java.util.regex.Pattern;

/**
 * Created by Markus on 23.09.2016.
 */

public class ConnectionUtils {

    public static boolean isIpAddressValid(String ipAddress) {
        return  Pattern.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", ipAddress);
    }

}
