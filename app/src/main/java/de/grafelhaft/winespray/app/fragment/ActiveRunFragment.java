package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import de.grafelhaft.grafellib.async.http.HttpResponse;
import de.grafelhaft.grafellib.async.http.OnConnectionResponseListener;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.api.OnErrorListener;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.controller.RunController;
import de.grafelhaft.winespray.app.util.MathUtils;
import de.grafelhaft.winespray.model.DataPoint;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.SensorPurpose;
import de.grafelhaft.winespray.model.State;
import de.grafelhaft.winespray.model.realm.RealmHelper;

/**
 * Created by Markus on 16.09.2016.
 */
public class ActiveRunFragment extends BaseFragment implements View.OnClickListener, DataPoint.OnDataPointAddedListener, State.OnStateChangedListener, OnErrorListener {

    public static final int MODE_LIVE = 1;
    public static final int MODE_TOTAL = 2;

    private int _mode = MODE_LIVE;

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_run_active;
    }

    @Override
    protected void init(Bundle bundle) {
        findViewById(R.id.button_pause).setOnClickListener(this);
        findViewById(R.id.button_stop).setOnClickListener(this);
    }

    @Override
    public void onDataPointAdded(long runId, DataPoint dataPoint) {
        RunController.getInstance().addDataPointToQueue(dataPoint);

        if (_mode == MODE_LIVE) {

            double workingWidth = RunController.getInstance().getWorkingWidth();
            int nozzleCount = RunController.getInstance().getNozzleCount();
            double speed = 0;
            if (dataPoint.getLocation() != null) speed = dataPoint.getLocation().getSpeed();

            double ejected = RunController.getInstance().getQueueEjectedVolume();
            double injected = RunController.getInstance().getQueueInjectedVolume();
            double returned = RunController.getInstance().getQueueReturnedVolume();
            double rate = MathUtils.calcRecycleRate(ejected, injected, returned);

            try {
                double used = MathUtils.calcUsedVolume(ejected, injected, returned);
                int value = (int) MathUtils.calcLiterPerHectare(used, speed, workingWidth, nozzleCount);

                ((TextView) findViewById(R.id.text_area_volume_used)).setText(Integer.toString(value));

            } catch (Exception e) {

            }

            try {
                int value = (int) MathUtils.calcLiterPerHectare(ejected, speed, workingWidth, nozzleCount);

                ((TextView) findViewById(R.id.text_area_volume_application)).setText(Integer.toString(value));
            } catch (Exception e) {

            }

            ((TextView) findViewById(R.id.text_volume_injection)).setText(Integer.toString((int) injected));

            ((TextView) findViewById(R.id.text_recycling_percent)).setText(MathUtils.formatDecimalToPercent(rate));

        } else if (_mode == MODE_TOTAL) {
            Run run = (Run) RealmHelper.findWhereId(Run.class, runId);

            int ejectedSum = (int) MathUtils.calcSumVolume(run, SensorPurpose.EJECTION, RunController.getInstance().getUpdateInterval());
            int injectedSum = (int) MathUtils.calcSumVolume(run, SensorPurpose.INJECTION, RunController.getInstance().getUpdateInterval());
            int returnedSum = (int) MathUtils.calcSumVolume(run, SensorPurpose.RETURN, RunController.getInstance().getUpdateInterval());
            int usedSum = (int) MathUtils.calcUsedVolume(ejectedSum, injectedSum, returnedSum);
            double rate = MathUtils.calcRecycleRate(ejectedSum, injectedSum, returnedSum);

            ((TextView) findViewById(R.id.text_area_volume_used)).setText(usedSum+"");
            ((TextView) findViewById(R.id.text_area_volume_application)).setText(ejectedSum+"");
            ((TextView) findViewById(R.id.text_volume_injection)).setText(injectedSum+"");
            ((TextView) findViewById(R.id.text_recycling_percent)).setText(MathUtils.formatDecimalToPercent(rate));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RunController.getInstance().addOnDataPointAddedListener(this);
        RunController.getInstance().addOnStateChangedListener(this);
        RunController.getInstance().addOnErrorListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        RunController.getInstance().removeOnDataPointAddedListener(this);
        RunController.getInstance().removeOnStateChangedListener(this);
        RunController.getInstance().removeOnErrorListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_pause:
                RunController.getInstance().pause(getActivity());
                break;
            case R.id.button_stop:
                RunController.getInstance().stop(getActivity());
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onStateChanged(int state) {
        switch (state) {
            case State.ACTIVE:
                ((Button) findViewById(R.id.button_pause)).setText(R.string.pause);
                break;
            case State.PAUSED:
                ((Button) findViewById(R.id.button_pause)).setText(R.string.continuee);
                break;
        }
    }

    public ActiveRunFragment setMode(int mode) {
        this._mode = mode;
        return this;
    }

    @Override
    public void onError(VolleyError error) {
        new Handler(getActivity().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.text_area_volume_used)).setText(R.string.disconnected);
            }
        });
    }
}
