package de.grafelhaft.winespray.app.viewmodel;

import android.content.Intent;

/**
 * Created by Markus on 18.09.2016.
 */
public class ProfileItem {

    private String _title;
    private Intent _intent;
    private int _iconRes;

    public ProfileItem(String title, Intent intent) {
        this._title = title;
        this._intent = intent;
    }

    public String getTitle() {
        return this._title;
    }

    public Intent getIntent() {
        return this._intent;
    }


    public ProfileItem setIconRes(int iconRes) {
        this._iconRes = iconRes;
        return this;
    }

    public int getIconRes() {
        return this._iconRes;
    }
}
