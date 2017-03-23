package de.grafelhaft.winespray.app.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by @author Markus Graf.
 */
public class PermissionUtils {

    public static final int REQUEST_READ_PHONE_STATE = 10;
    public static final int REQUEST_READ_CONTACTS = 11;
    public static final int REQUEST_RECORD_AUDIO = 12;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 13;
    public static final int REQUEST_CALL_PHONE = 14;

    public static int checkForPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission);
    }

    public static void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void requestPermission(Fragment fragment, String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    public static boolean handlePermission(Activity activity, String permission, int requestCode) {
        if (checkForPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(activity, new String[] {permission}, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean handlePermission(Fragment fragment, String permission, int requestCode) {
        if (checkForPermission(fragment.getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(fragment, new String[] {permission}, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean handlePermissions(Activity activity, String[] permissions, int requestCode) {
        boolean haveAllPermissions = true;

        for (String permission : permissions) {
            boolean b = (checkForPermission(activity, permission) == PackageManager.PERMISSION_GRANTED);
            if (!b) {
                requestPermission(activity, permissions, requestCode);
                haveAllPermissions = false;
            }
        }
        return haveAllPermissions;
    }

    public static boolean handlePermissions(Fragment fragment, String[] permissions, int requestCode) {
        boolean haveAllPermissions = true;

        for (String permission : permissions) {
            boolean b = (checkForPermission(fragment.getActivity(), permission) == PackageManager.PERMISSION_GRANTED);
            if (!b) {
                requestPermission(fragment, permissions, requestCode);
                haveAllPermissions = false;
            }
        }
        return haveAllPermissions;
    }

}
