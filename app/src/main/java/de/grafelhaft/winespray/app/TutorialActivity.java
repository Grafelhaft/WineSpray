package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import de.grafelhaft.grafellib.adapter.ViewStatePagerAdapter;
import de.grafelhaft.grafellib.fragment.OnStepReadyListener;
import de.grafelhaft.grafellib.fragment.SetupFragment;
import de.grafelhaft.grafellib.util.PrefUtils;
import de.grafelhaft.grafellib.view.ExtendedViewPager;
import de.grafelhaft.grafellib.view.TabLayout;
import de.grafelhaft.winespray.app.ftu.SensorCountFtuFragment;
import de.grafelhaft.winespray.app.ftu.WelcomeFtuFragment;

public class TutorialActivity extends BaseActivity implements OnStepReadyListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    private ExtendedViewPager _pager;
    private TabLayout _tabs;
    private SetupFragment[] _fragments;
    private Pair[] _text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        init();
    }

    private void init() {
        _fragments = new SetupFragment[]{
                new WelcomeFtuFragment().setOnStepReadyListener(this),
                new SensorCountFtuFragment().setOnStepReadyListener(this),
        };

        _text = new Pair[]{
                new Pair<>(R.string.ftu_title_welcome, R.string.ftu_description_welcome),
                new Pair<>(R.string.ftu_title_sensor_count, R.string.ftu_description_sensor_count),
        };

        _pager = (ExtendedViewPager) findViewById(R.id.pager);
        _pager.setPagingEnabled(false);
        _pager.addOnPageChangeListener(this);
        _pager.setAdapter(new ViewStatePagerAdapter(getSupportFragmentManager(), _fragments));

        _tabs = (TabLayout) findViewById(R.id.tabs);
        _tabs.setupWithViewPager(_pager);

        findViewById(R.id.fab).setOnClickListener(this);

        onPageSelected(0);
    }

    @Override
    public void onStepReady(boolean b) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((TextView) findViewById(R.id.title)).setText((Integer) _text[position].first);
        ((TextView) findViewById(R.id.description)).setText((Integer) _text[position].second);

        if (position >= _fragments.length-1) {
            ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(R.drawable.ic_check_white_48dp);
        } else {
            ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(R.drawable.ic_chevron_right_white_48dp);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                boolean ready = _fragments[_pager.getCurrentItem()].isReady();
                if (ready) {
                    if (!_pager.next()) {
                        startActivity(new Intent(this, MainActivity.class));
                        PrefUtils.writeKeyValue(this, R.string.pref_key_ftu, false);
                        finish();
                    }
                }
                break;
        }
    }
}
