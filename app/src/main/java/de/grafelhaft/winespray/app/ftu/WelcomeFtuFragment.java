package de.grafelhaft.winespray.app.ftu;

import android.os.Bundle;

import de.grafelhaft.grafellib.fragment.SetupFragment;
import de.grafelhaft.winespray.app.R;

/**
 * Created by Markus on 21.09.2016.
 */

public class WelcomeFtuFragment extends SetupFragment {

    @Override
    protected int inflateLayoutRes() {
        return R.layout.ftu_fragment_welcome;
    }

    @Override
    protected void init(Bundle bundle) {
        setReady(true);
    }
}
