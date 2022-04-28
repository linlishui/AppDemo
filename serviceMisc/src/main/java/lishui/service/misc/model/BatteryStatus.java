package lishui.service.misc.model;

import static android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN;
import static android.os.BatteryManager.BATTERY_STATUS_FULL;
import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;
import static android.os.BatteryManager.EXTRA_HEALTH;
import static android.os.BatteryManager.EXTRA_LEVEL;
import static android.os.BatteryManager.EXTRA_PLUGGED;
import static android.os.BatteryManager.EXTRA_STATUS;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import lishui.service.misc.R;


/**
 * Stores and computes some battery information.
 */
public class BatteryStatus {

    public static final String EXTRA_MAX_CHARGING_CURRENT = "max_charging_current";
    public static final String EXTRA_MAX_CHARGING_VOLTAGE = "max_charging_voltage";

    private static final int LOW_BATTERY_THRESHOLD = 20;
    private static final int DEFAULT_CHARGING_VOLTAGE_MICRO_VOLT = 5000000;

    public static final int CHARGING_UNKNOWN = -1;
    public static final int CHARGING_SLOWLY = 0;
    public static final int CHARGING_REGULAR = 1;
    public static final int CHARGING_FAST = 2;

    public final int status;
    public final int level;
    public final int plugged;
    public final int health;
    public final int maxChargingWattage;

    public BatteryStatus(int status, int level, int plugged, int health,
                         int maxChargingWattage) {
        this.status = status;
        this.level = level;
        this.plugged = plugged;
        this.health = health;
        this.maxChargingWattage = maxChargingWattage;
    }

    public BatteryStatus(Intent batteryChangedIntent) {
        status = batteryChangedIntent.getIntExtra(EXTRA_STATUS, BATTERY_STATUS_UNKNOWN);
        plugged = batteryChangedIntent.getIntExtra(EXTRA_PLUGGED, 0);
        level = batteryChangedIntent.getIntExtra(EXTRA_LEVEL, 0);
        health = batteryChangedIntent.getIntExtra(EXTRA_HEALTH, BATTERY_HEALTH_UNKNOWN);

        final int maxChargingMicroAmp = batteryChangedIntent.getIntExtra(EXTRA_MAX_CHARGING_CURRENT,
                -1);
        int maxChargingMicroVolt = batteryChangedIntent.getIntExtra(EXTRA_MAX_CHARGING_VOLTAGE, -1);

        if (maxChargingMicroVolt <= 0) {
            maxChargingMicroVolt = DEFAULT_CHARGING_VOLTAGE_MICRO_VOLT;
        }
        if (maxChargingMicroAmp > 0) {
            // Calculating muW = muA * muV / (10^6 mu^2 / mu); splitting up the divisor
            // to maintain precision equally on both factors.
            maxChargingWattage = (maxChargingMicroAmp / 1000)
                    * (maxChargingMicroVolt / 1000);
        } else {
            maxChargingWattage = -1;
        }
    }

    /**
     * Determine whether the device is plugged in (USB, power, or wireless).
     *
     * @return true if the device is plugged in.
     */
    public boolean isPluggedIn() {
        return plugged == BatteryManager.BATTERY_PLUGGED_AC
                || plugged == BatteryManager.BATTERY_PLUGGED_USB
                || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
    }

    /**
     * Determine whether the device is plugged in (USB, power).
     *
     * @return true if the device is plugged in wired (as opposed to wireless)
     */
    public boolean isPluggedInWired() {
        return plugged == BatteryManager.BATTERY_PLUGGED_AC
                || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    /**
     * Whether or not the device is charged. Note that some devices never return 100% for
     * battery level, so this allows either battery level or status to determine if the
     * battery is charged.
     *
     * @return true if the device is charged
     */
    public boolean isCharged() {
        return status == BATTERY_STATUS_FULL || level >= 100;
    }

    /**
     * Whether battery is low and needs to be charged.
     *
     * @return true if battery is low
     */
    public boolean isBatteryLow() {
        return level < LOW_BATTERY_THRESHOLD;
    }

    /**
     * Return current chargin speed is fast, slow or normal.
     *
     * @return the charing speed
     */
    public final int getChargingSpeed(Context context) {
        final int slowThreshold = context.getResources().getInteger(
                R.integer.config_chargingSlowlyThreshold);
        final int fastThreshold = context.getResources().getInteger(
                R.integer.config_chargingFastThreshold);
        return maxChargingWattage <= 0 ? CHARGING_UNKNOWN :
                maxChargingWattage < slowThreshold ? CHARGING_SLOWLY :
                        maxChargingWattage > fastThreshold ? CHARGING_FAST :
                                CHARGING_REGULAR;
    }

    @Override
    public String toString() {
        return "BatteryStatus{status=" + status + ",level=" + level + ",plugged=" + plugged
                + ",health=" + health + ",maxChargingWattage=" + maxChargingWattage + "}";
    }
}