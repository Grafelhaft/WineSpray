package de.grafelhaft.winespray.app.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by @author Markus Graf (Grafelhaft) on 22.10.2016.
 */

public class DialogActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        MessageDialog dialog = MessageDialog.create(title, message);
        dialog.setOnDialogCallbackListener(new OnDialogCallbackListener() {
            @Override
            public void onCallback(boolean positive, boolean negative, boolean neutral) {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "MESSAGE_DIALOG");
    }
}
