package de.grafelhaft.winespray.app.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import de.grafelhaft.winespray.app.R;


/**
 * Created by Markus on 18.09.2016.
 */
public class MessageDialog extends ADialogFragment {

    private String _title, _message;

    public static MessageDialog create(Context context, int titleRes, String message) {
        return create(context.getString(titleRes), message);
    }

    public static MessageDialog create(Context context, int titleRes, int messageRes) {
        return create(context.getString(titleRes), context.getString(messageRes));
    }

    public static MessageDialog create(String title, String message) {
        MessageDialog dialog = new MessageDialog();
        dialog._title = title;
        dialog._message = message;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(_title)
                .setMessage(_message)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (_listener != null) {
                            _listener.onCallback(true, false, false);
                        }
                        dismiss();
                    }
                });
        return builder.create();
    }
}
