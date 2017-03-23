package de.grafelhaft.winespray.app.util;

import java.util.Collection;
import java.util.Locale;

import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.SensorData;
import de.grafelhaft.winespray.model.Session;

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


    public static double calcAverageSpeed(Session session) {
        double value = 0;
        for (Run r : session.getRuns()) {
            value += calcAverageSpeed(r.getDataPoints());
        }
        if (session.getRuns().size() > 0) {
            return value / session.getRuns().size();
        }
        return 0;
    }

    public static double calcAverageSpeed(Run run) {
        return calcAverageSpeed(run.getDataPoints());
    }

    public static double calcAverageSpeed(Collection<DataPoint> dataPoints) {
        double speed = 0.0;
        int i = 0;
        for (DataPoint d : dataPoints) {
            if (d.getLocation() != null) {
                speed += d.getLocation().getSpeed();
                i++;
            }
        }
        if (i > 0) {
            return speed / i;
        }
        return 0;
    }


    public static double calcAverageVolume(Session session, int sensorPurpose) {
        if (session.getRuns().size() > 0) {
            double value = 0;
            for (Run r : session.getRuns()) {
                value += calcAverageVolume(r, sensorPurpose);
            }
            return value / session.getRuns().size();
        }
        return 0;
    }

    public static double calcAverageVolume(Run run, int sensorPurpose) {
        return calcAverageVolume(run.getDataPoints(), sensorPurpose);
    }

    public static double calcAverageVolume(Collection<DataPoint> dataPoints, int sensorPurpose) {
        if (dataPoints.size() > 0) {

            double value = 0;
            int sensorCount = 0;

            for (DataPoint d : dataPoints) {

                for (SensorData sd : d.getData()) {

                    if (sd.getSensor().getPurpose() == sensorPurpose) {
                        sensorCount++;
                        value += sd.getValue();
                    }

                }

            }

            if (sensorCount > 0) {
                return value / sensorCount;
            }
        }
        return 0;
    }


    public static double calcSumVolume(Session session, int sensorPurpose, int interval) {
        double value = 0;
        for (Run r : session.getRuns()) {
            value += calcSumVolume(r, sensorPurpose, interval);
        }
        return value;
    }

    public static double calcSumVolume(Run run, int sensorPurpose, int interval) {
        return calcSumVolume(run.getDataPoints(), sensorPurpose, interval);
    }

    public static double calcSumVolume(Collection<DataPoint> dataPoints, int sensorPurpose, int interval) {
        double value = 0;
        for (DataPoint d : dataPoints) {

            for (SensorData sd : d.getData()) {

                if (sd.getSensor().getPurpose() == sensorPurpose) {
                    value += sd.getValue();
                }

            }

        }
        return value * (interval / 1000); //because milliseconds
    }


    public static double calcSumVolumePerSquareMeter(double areaSize, Session session, int sensorPurpose, int interval) {
        double value = 0;
        for (Run r : session.getRuns()) {
            value += calcSumVolumePerSquareMeter(areaSize, r, sensorPurpose, interval);
        }
        return value;
    }

    public static double calcSumVolumePerSquareMeter(double areaSize, Run run, int sensorPurpose, int interval) {
        return calcSumVolumePerSquareMeter(areaSize, run.getDataPoints(), sensorPurpose, interval);
    }

    public static double calcSumVolumePerSquareMeter(double areaSize, Collection<DataPoint> dataPoints, int sensorPurpose, int interval) {
        double sum = calcSumVolume(dataPoints, sensorPurpose, interval);
        double perSqMeter = 0;
        if (areaSize > 0) {
            perSqMeter = sum / areaSize;
        }
        return perSqMeter;
    }

    public static double calcVolumePerSquareMeter(double areaSize, double volume) {
        return volume / areaSize;
    }


    public static double calcSumPerHectare(double areaSize, Session session, int sensorPurpose, int interval) {
        return calcSumVolumePerSquareMeter(areaSize, session, sensorPurpose, interval) * 10000;
    }

    public static double calcSumPerHectare(double areaSize, Run run, int sensorPurpose, int interval) {
        return calcSumVolumePerSquareMeter(areaSize, run, sensorPurpose, interval) * 10000;
    }

    public static double calcSumPerHectare(double areaSize, Collection<DataPoint> dataPoints, int sensorPurpose, int interval) {
        return calcSumVolumePerSquareMeter(areaSize, dataPoints, sensorPurpose, interval) * 10000;
    }

    public static double calcVolumePerHectare(double areaSize, double volume) {
        return calcVolumePerSquareMeter(areaSize, volume) * 10000;
    }
}
