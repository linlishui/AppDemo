package lishui.service.misc.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Hashtable;

import lishui.lib.base.util.EnvironmentUtils;

/**
 * Created by lishui.lin on 20-9-28
 */
public class PermissionUtils {

    private static Hashtable<String, Integer> sPermissions = new Hashtable<>();

    public static boolean hasPermission(Context context, final String permission) {
        if (EnvironmentUtils.isAtLeastM()) {
            if (!sPermissions.containsKey(permission)
                    || sPermissions.get(permission) == PackageManager.PERMISSION_DENIED) {
                final int permissionState = context.checkSelfPermission(permission);
                sPermissions.put(permission, permissionState);
            }
            return sPermissions.get(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /**
     * Does the app have all the specified permissions
     */
    public static boolean hasPermissions(Context context, final String[] permissions) {
        for (final String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPhonePermission(Context context) {
        return hasPermission(context, Manifest.permission.READ_PHONE_STATE);
    }

    public static boolean hasSmsPermission(Context context) {
        return hasPermission(context, Manifest.permission.READ_SMS);
    }

    public static boolean hasStoragePermission(Context context) {
        // Note that READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE are granted or denied together.
        return hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public static String[] getMissingPermissions(Context context, final String[] permissions) {
        final ArrayList<String> missingList = new ArrayList<String>();
        for (final String permission : permissions) {
            if (!hasPermission(context, permission)) {
                missingList.add(permission);
            }
        }

        final String[] missingArray = new String[missingList.size()];
        missingList.toArray(missingArray);
        return missingArray;
    }

    private static String[] sRequiredPermissions = new String[]{
            // Required to read existing SMS threads
            Manifest.permission.READ_SMS,
            // Required for knowing the phone number, number of SIMs, etc.
            Manifest.permission.READ_PHONE_STATE,
            // This is not strictly required
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    /**
     * Does the app have the minimum set of permissions required to operate.
     */
    public static boolean hasRequiredPermissions(Context context) {
        return hasPermissions(context, sRequiredPermissions);
    }

    public static String[] getMissingRequiredPermissions(Context context) {
        return getMissingPermissions(context, sRequiredPermissions);
    }
}
