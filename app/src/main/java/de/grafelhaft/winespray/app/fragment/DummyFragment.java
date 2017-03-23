package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;

import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.grafellib.view.IFragmentTabTitleResProvider;
import de.grafelhaft.winespray.app.R;

/**
 * Created by Markus on 21.09.2016.
 */

public class DummyFragment extends BaseFragment implements IFragmentTabTitleResProvider {
    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_dummy;
    }

    @Override
    protected void init(Bundle bundle) {

    }

    @Override
    public int getFragmentTabTitle() {
        return R.string._placeholder_;
    }
}
