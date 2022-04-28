package lishui.service.misc.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.NumberFormat;
import android.os.BatteryManager;
import android.os.Build;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;

import lishui.service.misc.R;
import lishui.service.misc.model.BatteryStatus;

/**
 * @author lishui.lin
 * Created it on 2021/5/25
 */
public class DeviceInfoUtils {

    /**
     * Returns true if Monkey is running.
     */
    public static boolean isMonkeyRunning() {
        return ActivityManager.isUserAMonkey();
    }

    public static boolean isDevelopmentSettingsEnabled(Context context) {
        final UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);
        final boolean settingEnabled = Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                Build.TYPE.equals("eng") ? 1 : 0) != 0;
        final boolean hasRestriction = um.hasUserRestriction(
                UserManager.DISALLOW_DEBUGGING_FEATURES);
        //final boolean isAdmin = um.isAdminUser();
        //return isAdmin && !hasRestriction && settingEnabled;
        return !hasRestriction && settingEnabled;
    }

    /**
     * Formats a double from 0.0..100.0 with an option to round
     **/
    public static String formatPercentage(double percentage, boolean round) {
        final int localPercentage = round ? Math.round((float) percentage) : (int) percentage;
        return formatPercentage(localPercentage);
    }

    /**
     * Formats the ratio of amount/total as a percentage.
     */
    public static String formatPercentage(long amount, long total) {
        return formatPercentage(((double) amount) / total);
    }

    /**
     * Formats an integer from 0..100 as a percentage.
     */
    public static String formatPercentage(int percentage) {
        return formatPercentage(((double) percentage) / 100.0);
    }

    /**
     * Formats a double from 0.0..1.0 as a percentage.
     */
    public static String formatPercentage(double percentage) {
        return NumberFormat.getPercentInstance().format(percentage);
    }

    public static String getBatteryPercentage(Intent batteryChangedIntent) {
        return formatPercentage(getBatteryLevel(batteryChangedIntent));
    }

    public static int getBatteryLevel(Intent batteryChangedIntent) {
        int level = batteryChangedIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = batteryChangedIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return (level * 100) / scale;
    }

    /**
     * Get battery status string
     */
    public static String getBatteryStatus(Context context, Intent batteryChangedIntent) {
        final int status = batteryChangedIntent.getIntExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN);
        final Resources res = context.getResources();

        String statusString = res.getString(R.string.battery_info_status_unknown);
        final BatteryStatus batteryStatus = new BatteryStatus(batteryChangedIntent);

        if (batteryStatus.isCharged()) {
            statusString = res.getString(R.string.battery_info_status_full);
        } else {
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                switch (batteryStatus.getChargingSpeed(context)) {
                    case BatteryStatus.CHARGING_FAST:
                        statusString = res.getString(R.string.battery_info_status_charging_fast);
                        break;
                    case BatteryStatus.CHARGING_SLOWLY:
                        statusString = res.getString(R.string.battery_info_status_charging_slow);
                        break;
                    default:
                        statusString = res.getString(R.string.battery_info_status_charging);
                        break;
                }

            } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                statusString = res.getString(R.string.battery_info_status_discharging);
            } else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                statusString = res.getString(R.string.battery_info_status_not_charging);
            }
        }

        return statusString;
    }

    /**
     * Format a phone number.
     *
     * @param subscriptionInfo {@link SubscriptionInfo} subscription information.
     * @return Returns formatted phone number.
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_NUMBERS)
    public static String getFormattedPhoneNumber(Context context,
                                                 SubscriptionInfo subscriptionInfo) {
        String formattedNumber = null;
        if (subscriptionInfo != null) {
            final TelephonyManager telephonyManager = context.getSystemService(
                    TelephonyManager.class);
            final String rawNumber = telephonyManager.createForSubscriptionId(
                    subscriptionInfo.getSubscriptionId()).getLine1Number();
            if (!TextUtils.isEmpty(rawNumber)) {
                formattedNumber = PhoneNumberUtils.formatNumber(rawNumber);
            }
        }
        return formattedNumber;
    }

    /**
     * To get the formatting text for display in a potentially opposite-directionality context
     * without garbling.
     *
     * @param subscriptionInfo {@link SubscriptionInfo} subscription information.
     * @return Returns phone number with Bidi format.
     */
    public static String getBidiFormattedPhoneNumber(Context context,
                                                     SubscriptionInfo subscriptionInfo) {
        final String phoneNumber = getFormattedPhoneNumber(context, subscriptionInfo);
        return BidiFormatter.getInstance().unicodeWrap(phoneNumber, TextDirectionHeuristics.LTR);
    }

}
