package de.grafelhaft.winespray.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

import de.grafelhaft.winespray.app.R;

/**
 * Created by Markus on 18.09.2016.
 */
public class RenameDialog extends ADialogFragment {

    private String _oldName;
    private EditText _editName;
    private Pattern _pattern;
    private OnNameChangedListener _nameChangeListener;

    public static RenameDialog create (String oldName) {
        return create(oldName, null);
    }

    public static RenameDialog create (String oldName, Pattern pattern) {
        RenameDialog dialog = new RenameDialog();
        dialog._oldName = oldName;
        dialog._pattern = pattern;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_rename, null);
        _editName = (EditText) view.findViewById(R.id.text_name);

        if (_oldName != null) {
            _editName.append(_oldName);
        }

        if (_pattern != null) {
            _editName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int length = s.length();

                    if (length > 0 && !_pattern.matcher(s.toString()).matches()) {
                        s.delete(length-1, length);
                    }
                }
            });
        }

        builder.setView(view)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (_nameChangeListener != null) {
                            _nameChangeListener.onNameChanged(_editName.getText().toString());
                        }
                        if (_listener != null) {
                            _listener.onCallback(true, false, false);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (_listener != null) {
                            _listener.onCallback(false, true, false);
                        }
                    }
                });
        return builder.create();
    }

    public interface OnNameChangedListener {
        void onNameChanged(String name);
    }

    public void setOnNameChangedListener(OnNameChangedListener l) {
        this._nameChangeListener = l;
    }
}
