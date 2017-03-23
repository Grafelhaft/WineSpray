package de.grafelhaft.winespray.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends BaseActivity {

    private String versionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
            ((TextView)findViewById(R.id.version)).setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        findViewById(R.id.button_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { getString(R.string.email_gruenewald), getString(R.string.email_grafelhaft) });
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " " + versionName + " Support/Feedback");

                try {
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.feedback)));
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
