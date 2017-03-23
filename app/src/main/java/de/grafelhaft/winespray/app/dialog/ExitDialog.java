package de.grafelhaft.winespray.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import de.grafelhaft.winespray.app.R;

/**
 * Created by @author Markus Graf (Grafelhaft) on 13.10.2016.
 */

public class ExitDialog extends ADialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.exit))
                .setMessage(getString(R.string.exit_app))
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (_listener != null) {
                            _listener.onCallback(true, false, false);
                        }
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (_listener != null) {
                            _listener.onCallback(false, true, false);
                        }
                        dismiss();
                    }
                });
        return builder.create();
    }

}
