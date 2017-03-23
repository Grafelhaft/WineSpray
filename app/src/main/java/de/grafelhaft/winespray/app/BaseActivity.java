package de.grafelhaft.winespray.app;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionInflater;

/**
 * Created by Markus on 16.09.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTransitions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    protected void setupTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode enter = (Explode) TransitionInflater.from(this).inflateTransition(R.transition.activity_explode);
            //getWindow().setEnterTransition(enter);

            Fade exit = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
            //getWindow().setExitTransition(exit);
        }
    }

    public boolean isTwoPane() {
        if (findViewById(R.id.container_detail) != null) {
            return true;
        }
        return false;
    }
}
