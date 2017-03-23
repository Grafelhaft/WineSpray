package de.grafelhaft.winespray.app.dialog;

import android.support.v4.app.DialogFragment;

/**
 * Created by Markus on 18.09.2016.
 */
public abstract class ADialogFragment extends DialogFragment {

    protected OnDialogCallbackListener _listener;

    public void setOnDialogCallbackListener(OnDialogCallbackListener l) {
        this._listener = l;
    }
}
