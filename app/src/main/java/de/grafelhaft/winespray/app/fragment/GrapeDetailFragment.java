package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;

import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.app.util.IntentUtils;
import de.grafelhaft.winespray.model.Grape;
import de.grafelhaft.winespray.model.realm.RealmHelper;

/**
 * Created by Markus on 14.09.2016.
 */
public class GrapeDetailFragment extends BaseFragment {

    private Grape _grape;

    public static GrapeDetailFragment create(Grape grape) {
        GrapeDetailFragment fragment = new GrapeDetailFragment();
        fragment._grape = grape;
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_parcel_detail;
    }

    @Override
    protected void init(Bundle bundle) {
        if (_grape == null && bundle != null) {
            long id = bundle.getLong(IntentUtils.EXTRA_GRAPE_ID);
            _grape = (Grape) RealmHelper.findWhereId(Grape.class, id);
        }

        if (_grape != null) {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(IntentUtils.EXTRA_PARCEL_ID, _grape.getId());
    }
}
