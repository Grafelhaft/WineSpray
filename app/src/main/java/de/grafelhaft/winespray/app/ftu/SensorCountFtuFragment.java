package de.grafelhaft.winespray.app.ftu;

import android.os.Bundle;
import android.view.View;

import de.grafelhaft.grafellib.fragment.SetupFragment;
import de.grafelhaft.grafellib.util.PrefUtils;
import de.grafelhaft.winespray.app.R;

/**
 * Created by Markus on 21.09.2016.
 */

public class SensorCountFtuFragment extends SetupFragment implements View.OnClickListener {

    private int _sensorCount;

    @Override
    protected int inflateLayoutRes() {
        return R.layout.ftu_fragment_sensor_count;
    }

    @Override
    protected void init(Bundle bundle) {
        findViewById(R.id.button_sensor_1).setOnClickListener(this);
        findViewById(R.id.button_sensor_3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sensor_1:
                _sensorCount = 1;
                setReady(true);
                break;
            case R.id.button_sensor_3:
                _sensorCount = 3;
                setReady(true);
                break;
        }
        makeInvisible(_sensorCount);

        PrefUtils.writeKeyValue(getActivity(), R.string.pref_key_sensor_count, Integer.toString(_sensorCount));
    }

    private void makeInvisible(int sensorCount) {
        if (sensorCount >= 3) {
            findViewById(R.id.button_sensor_1).setVisibility(View.GONE);
        } else {
            findViewById(R.id.button_sensor_3).setVisibility(View.GONE);
        }
        findViewById(R.id.text_or).setVisibility(View.GONE);
    }
}
