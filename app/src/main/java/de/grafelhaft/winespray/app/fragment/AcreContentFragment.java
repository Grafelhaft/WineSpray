package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.grafelhaft.grafellib.adapter.ViewStatePagerAdapter;
import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.grafellib.view.ExtendedViewPager;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Acre;
import de.grafelhaft.winespray.model.Area;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.Realm;

/**
 * Created by @author Markus Graf (Grafelhaft) on 26.10.2016.
 */

public class AcreContentFragment extends BaseFragment {

    private Acre _acre;

    public static AcreContentFragment create(Acre acre) {
        AcreContentFragment fragment = new AcreContentFragment();
        fragment._acre = acre;
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_acre_content;
    }

    @Override
    protected void init(Bundle bundle) {
        setHasOptionsMenu(true);

        if (_acre == null && bundle != null) {
            long acreId = bundle.getLong(IntentUtils.EXTRA_ACRE_ID);
            _acre = (Acre) RealmHelper.findWhereId(Acre.class, acreId);
        }

        if (_acre == null) {
            _acre = new Acre();
            RealmHelper.setAutoId(_acre, Acre.class);
        }

        if (_acre != null) {
            ViewStatePagerAdapter adapter = new ViewStatePagerAdapter(getChildFragmentManager(), new Fragment[]{
                    AcreDetailFragment.create(_acre),
                    AcreRunListFragment.create(_acre)
            });

            ExtendedViewPager pager = (ExtendedViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareMap();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(IntentUtils.EXTRA_ACRE_ID, _acre.getId());
    }

    private void prepareMap() {
        if (_acre.getArea() != null && _acre.getArea().getLocations().size() > 0) {
            MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
            mapFragment.enableMyLocation(false);
            mapFragment.clear();
            mapFragment.addPolygon(Realm.getDefaultInstance().copyFromRealm(_acre.getArea().getLocations()));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_acre, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
