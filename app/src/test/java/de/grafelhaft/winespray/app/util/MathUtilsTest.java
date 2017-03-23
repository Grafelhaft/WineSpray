package de.grafelhaft.winespray.app.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Sensor;
import de.grafelhaft.winespray.model.SensorData;
import de.grafelhaft.winespray.model.SensorPurpose;

import static org.junit.Assert.*;

/**
 * Created by @author Markus Graf (Grafelhaft) on 15.10.2016.
 */
public class MathUtilsTest {

    @Test
    public void calcSumVolumePerSquareMeter() throws Exception {
        double areaSize = 5;
        double volume = 20;

        List<DataPoint> data = new ArrayList<>();
        data.add(new DataPoint().addData(new SensorData().setSensor(new Sensor().setPurpose(SensorPurpose.EJECTION)).setValue(12)));
        data.add(new DataPoint().addData(new SensorData().setSensor(new Sensor().setPurpose(SensorPurpose.EJECTION)).setValue(5)));
        data.add(new DataPoint().addData(new SensorData().setSensor(new Sensor().setPurpose(SensorPurpose.EJECTION)).setValue(3)));

        assertEquals(MathUtils.calcVolumePerSquareMeter(areaSize, volume), 4, 0);
        assertEquals(MathUtils.calcSumVolumePerSquareMeter(areaSize, data, SensorPurpose.EJECTION, 1000), 4, 0);
    }

}