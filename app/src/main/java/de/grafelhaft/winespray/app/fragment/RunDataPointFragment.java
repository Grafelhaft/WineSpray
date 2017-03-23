package de.grafelhaft.winespray.app.fragment;

import android.os.Bundle;

import de.grafelhaft.grafellib.fragment.BaseFragment;
import de.grafelhaft.winespray.model.Run;

/**
 * Created by Markus on 21.09.2016.
 */

public class RunDataPointFragment extends BaseFragment {

    private Run _run;

    public static RunDataPointFragment create(Run run) {
        RunDataPointFragment fragment = new RunDataPointFragment();
        fragment._run = run;
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return 0;
    }

    @Override
    protected void init(Bundle bundle) {

    }
}
