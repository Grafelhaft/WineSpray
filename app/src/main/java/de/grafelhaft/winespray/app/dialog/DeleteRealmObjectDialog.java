package de.grafelhaft.winespray.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import de.grafelhaft.winespray.app.R;
import de.grafelhaft.winespray.model.realm.RealmHelper;
import io.realm.RealmObject;

/**
 * Created by Markus on 16.09.2016.
 */
public class DeleteRealmObjectDialog extends ADialogFragment {

    private RealmObject _realmObject;

    public static DeleteRealmObjectDialog create(RealmObject object) {
        DeleteRealmObjectDialog dialog = new DeleteRealmObjectDialog();
        dialog._realmObject = object;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title_delete);
        builder.setMessage(R.string.dialog_text_delete);
        builder.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RealmHelper.remove(_realmObject);

                if (_listener != null) {
                    _listener.onCallback(true, false, false);
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
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
