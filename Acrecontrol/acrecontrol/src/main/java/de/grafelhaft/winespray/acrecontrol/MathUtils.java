package de.grafelhaft.winespray.acrecontrol;

import java.util.Locale;

/**
 * Created by @author Markus Graf.
 */

public class MathUtils {

    public static String format2Decimals(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    public static String format2Decimals(double value, int decimals) {
        return String.format(Locale.US, "%." + decimals + "f", value);
    }

    public static String formatDecimalToPercent(double value) {
        return Integer.toString((int) (value * 100));
    }

    /**
     * Calculates the recycled volume.
     *
     * @param injectedVolume Volume used for the injection pump system.
     * @param returnedVolume Volume that is returned into the tank.
     * @return
     */
    public static double calcRecycledVolume(double injectedVolume, double returnedVolume) {
        return returnedVolume - injectedVolume;
    }

    /**
     * Calculates the used volume.
     *
     * @param ejectedVolume  Volume that is ejected by the nozzles.
     * @param injectedVolume Volume used for the injection pump system.
     * @param returnedVolume Volume that is returned into the tank.
     * @return
     */
    public static double calcUsedVolume(double ejectedVolume, double injectedVolume, double returnedVolume) {
        return (ejectedVolume - (returnedVolume - injectedVolume));
    }

    /**
     * Calculates the recycle rate.
     *
     * @param ejectedVolume  Volume that is ejected by the nozzles.
     * @param injectedVolume Volume used for the injection pump system.
     * @param returnedVolume Volume that is returned into the tank.
     * @return
     */
    public static double calcRecycleRate(double ejectedVolume, double injectedVolume, double returnedVolume) {
        if (ejectedVolume > 0) {
            return (calcRecycledVolume(injectedVolume, returnedVolume) / ejectedVolume);
        }
        return 0;
    }


    public static double calcLiterPerSquareMeter(double literPerSecond, double speedMeterPerSecond, double workingWidth) {
        if (speedMeterPerSecond > 0 && workingWidth > 0) {
            return (literPerSecond / (speedMeterPerSecond * workingWidth));
        }
        return 0;
    }

    public static double calcLiterPerSquareMeter(double literPerSecond, double speedMeterPerSecond, double workingWidth, int nozzleCount) {
        if (speedMeterPerSecond > 0 && workingWidth > 0) {
            return calcLiterPerSquareMeter(literPerSecond, speedMeterPerSecond, workingWidth * nozzleCount);
        }
        return 0;
    }

    public static double calcLiterPerHectare(double literPerSecond, double speedMeterPerSecond, double workingWidth) {
        double value = calcLiterPerSquareMeter(literPerSecond, speedMeterPerSecond, workingWidth) * 10000; //because 1 hectare == 10.000 meters
        return value > 0 ? value : 0;
    }

    public static double calcLiterPerHectare(double literPerSecond, double speedMeterPerSecond, double workingWidth, int nozzleCount) {
        double value = calcLiterPerSquareMeter(literPerSecond, speedMeterPerSecond, workingWidth, nozzleCount) * 10000; //because 1 hectare == 10.000 meters
        return value > 0 ? value : 0;
    }

    public static double calcLiterPerSecond(double literPerSquareMeter, double speedMeterPerSecond, double workingWidth) {
        return (literPerSquareMeter * speedMeterPerSecond * workingWidth);
    }


    public static double calcSingleNozzleEjection(double fullNozzleEjection, int nozzleCount) {
        return (fullNozzleEjection / nozzleCount);
    }



    public static double calcVolumePerSquareMeter(double areaSize, double volume) {
        return volume / areaSize;
    }

    public static double calcVolumePerHectare(double areaSize, double volume) {
        return calcVolumePerSquareMeter(areaSize, volume) * 10000;
    }
}
