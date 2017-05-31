package de.grafelhaft.winespray.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.grafelhaft.grafellib.adapter.ViewPagerAdapter;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.grafellib.view.ExtendedViewPager;
import de.grafelhaft.grafellib.view.TabLayout;
import de.grafelhaft.winespray.acrecontrol.Polygon;
import de.grafelhaft.winespray.acrecontrol.PolygonBuilder;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.service.AcreControlService;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.app.util.MathUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.District;
import de.grafelhaft.winespray.model.Location;
import de.grafelhaft.winespray.model.Run;
import de.grafelhaft.winespray.model.SensorPurpose;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Markus on 14.09.2016.
 */
public class RunDetailFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private Run _run;

    private Spinner _spinAcre;
    private TextView _textAcreSize, _textAcreSizeUseful;
    private TextView _textUsed, _textEjected, _textRate;
    private TextView _textSumUsed, _textSumEjected;

    public static RunDetailFragment create(Run run) {
        RunDetailFragment fragment = new RunDetailFragment();
        fragment._run = run;
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_run_details;
    }

    @Override
    protected void init(Bundle bundle) {
        if (_run == null && bundle != null) {
            long id = bundle.getLong(IntentUtils.EXTRA_RUN_ID);
            _run = (Run) RealmHelper.findWhereId(Run.class, id);
        }

        if (_run != null) {
            _spinAcre = (Spinner) findViewById(R.id.spinner_acre);

            _textAcreSize = (TextView) findViewById(R.id.title_acre_size);
            _textAcreSizeUseful = (TextView) findViewById(R.id.title_acre_size_useful);
            _textUsed = (TextView) findViewById(R.id.title_volume_used);
            _textEjected = (TextView) findViewById(R.id.title_volume_ejected);
            _textRate = (TextView) findViewById(R.id.title_rate_recycle);
            _textSumUsed = (TextView) findViewById(R.id.title_sum_used);
            _textSumEjected = (TextView) findViewById(R.id.title_sum_ejected);

            bind(_run);
        }

    }

    private void bind(Run run) {
        if (run != null) {

            RealmResults<Acre> results = Realm.getDefaultInstance().where(Acre.class).findAll().sort("id", Sort.ASCENDING);
            ArrayAdapter<Acre> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_spinner, results);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            _spinAcre.setAdapter(adapter);
            _spinAcre.setOnItemSelectedListener(this);

            if (run.hasAcre()) {
                int position = adapter.getPosition(run.getAcre());
                _spinAcre.setSelection(position);
            } else {
                setText(run);
            }
        }
    }

    private void setText(final Run run) {
        if (_run != null) {

            if (run.hasArea()) {
                double size = run.getAcre().getArea().getSizeUseful();
                double sizeUseful = run.getAcre().getArea().getSizeUseful();
                _textAcreSize.setText(getString(R.string.format_size_hectare, MathUtils.format2Decimals(size / 10000, 4)));
            }

            final Polygon polygon = AcreControlService.createPolygon(run);
            //_textAcreSizeUseful.setText(MathUtils.format2Decimals(sizeUseful/10000, 4));
            _textAcreSizeUseful.setText(getString(R.string.format_size_hectare, MathUtils.format2Decimals(polygon.area() / 10000, 4)));


            final long runId = _run.getId();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Run run = (Run) RealmHelper.findWhereId(Run.class, runId);

                    final double ejected = MathUtils.calcSumPerHectare(polygon.area(), run, SensorPurpose.EJECTION, run.getRunConfig().getUpdateInterval());
                    double injected = MathUtils.calcSumPerHectare(polygon.area(), run, SensorPurpose.INJECTION, run.getRunConfig().getUpdateInterval());
                    double returned = MathUtils.calcSumPerHectare(polygon.area(), run, SensorPurpose.RETURN, run.getRunConfig().getUpdateInterval());
                    final double used = MathUtils.calcUsedVolume(ejected, injected, returned);
                    final double rate = MathUtils.calcRecycleRate(ejected, injected, returned);

                    final double sumEjected = MathUtils.calcSumVolume(run, SensorPurpose.EJECTION, run.getRunConfig().getUpdateInterval());
                    final double sumInjected = MathUtils.calcSumVolume(run, SensorPurpose.INJECTION, run.getRunConfig().getUpdateInterval());
                    final double sumReturned = MathUtils.calcSumVolume(run, SensorPurpose.RETURN, run.getRunConfig().getUpdateInterval());
                    final double sumUsed = MathUtils.calcUsedVolume(sumEjected, sumInjected, sumReturned);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (RunDetailFragment.this.isAdded()) {
                                _textUsed.setText(getString(R.string.format_volume_liter_hectare, MathUtils.format2Decimals(used, 0)));
                                _textEjected.setText(getString(R.string.format_volume_liter_hectare, MathUtils.format2Decimals(ejected, 0)));
                                _textRate.setText(getString(R.string.format_volume_quote, MathUtils.formatDecimalToPercent(rate)));
                                _textSumUsed.setText(getString(R.string.format_volume_liter, MathUtils.format2Decimals(sumUsed, 0)));
                                _textSumEjected.setText(getString(R.string.format_volume_liter, MathUtils.format2Decimals(sumEjected, 0)));
                            }
                        }
                    });
                }
            };
            new Thread(runnable).start();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAcreChangedListener) {
            _listeners.add((OnAcreChangedListener) context);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(IntentUtils.EXTRA_RUN_ID, _run.getId());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_acre:
                final Acre acre = (Acre) _spinAcre.getItemAtPosition(position);

                if (acre != null && acre.getArea() != null) {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Acre oldAcre = _run.getAcre();
                            _run.setAcre(acre);
                            if (_run.getAcre().getArea().getLocations().size() <= 0) {
                                _run.getAcre().getArea().setLocations(oldAcre.getArea().getLocations());
                            }
                        }
                    });

                    setText(_run);

                    for (OnAcreChangedListener l : _listeners) {
                        l.onAcreChanged(acre);
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //LISTENER//////////////////////////////////////////////////////////////////////////////////////
    private List<OnAcreChangedListener> _listeners = new ArrayList<>();

    public interface OnAcreChangedListener {
        void onAcreChanged(Acre acre);
    }

}
