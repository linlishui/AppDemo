package lishui.service.misc.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.lib.base.util.EnvironmentUtils
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.text.BidiFormatter
import android.text.TextUtils
import java.net.InetAddress


/**
 * @author lishui.lin
 * Created it on 2021/5/25
 */
class DeviceInfoController(private val mContext: Context) {

    private val mLocationManager: LocationManager =
        mContext.getSystemService(LocationManager::class.java)

    private val mSubscriptionManager: SubscriptionManager =
        mContext.getSystemService(SubscriptionManager::class.java)

    private val mConnectivityManager: ConnectivityManager =
        mContext.getSystemService(ConnectivityManager::class.java)


    private val deviceUnknownStr = "Unknown"

    var deviceName: String = ""
        private set

    private fun initializeDeviceName() {
        deviceName = Settings.Global.getString(
            mContext.contentResolver,
            Settings.Global.DEVICE_NAME
        ) ?: Build.MODEL
    }

    private fun initializePhoneNumber() {
        if (isPermissionGranted(context = mContext, Manifest.permission.READ_PHONE_STATE)) {
            val subscriptionInfoList = mSubscriptionManager.activeSubscriptionInfoList
            if (subscriptionInfoList == null || subscriptionInfoList.isEmpty()) {
                return
            }

            for (subscriptionInfo in subscriptionInfoList) {
                getFormattedPhoneNumber(subscriptionInfo)?.apply {
                    val phoneNumber = subscriptionInfo.subscriptionId
                }
            }
        }
    }

    private fun getFormattedPhoneNumber(subscriptionInfo: SubscriptionInfo?): CharSequence {
        val phoneNumber: String = DeviceInfoUtils.getBidiFormattedPhoneNumber(
            mContext,
            subscriptionInfo
        )
        return if (TextUtils.isEmpty(phoneNumber)) deviceUnknownStr else phoneNumber
    }

    private fun initializeDeviceVersion() {
        if (EnvironmentUtils.isAtLeastR()) {
            Build.VERSION.RELEASE_OR_CODENAME
        } else {
            "${Build.VERSION.RELEASE}_${Build.VERSION.CODENAME}"
        }
    }

    private fun initializeLocation() {
        try {
            mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.apply {
                val location = "${this.latitude}_${this.longitude}"   // 维度_经度
            }
        } catch (ex: Exception) {
            // no-op
        }
    }

    private fun initializeIpAddress() {
        if (isPermissionGranted(mContext, Manifest.permission.ACCESS_NETWORK_STATE)) {
            mConnectivityManager.getLinkProperties(mConnectivityManager.activeNetwork)?.apply {

                val iter: Iterator<InetAddress>
                try {
                    iter = (this::class.java.getMethod("getAllAddresses")
                        .invoke(this) as List<InetAddress>).iterator()
                } catch (ex: Exception) {
                    // no-op
                    return@apply
                }

                val addresses = StringBuilder()
                while (iter.hasNext()) {
                    addresses.append(iter.next().hostAddress)
                    if (iter.hasNext()) addresses.append("\n")
                }

                if (addresses.isNotEmpty()) {
                    val ipAddress = addresses.toString()
                }
            }
        }
    }

    private fun initializeBluetoothAddress() {
        if (isPermissionGranted(mContext, Manifest.permission.BLUETOOTH)) {
            val bluetooth = BluetoothAdapter.getDefaultAdapter()
            if (bluetooth != null && bluetooth.isEnabled) {
                var btAddress = if (bluetooth.address.isEmpty()) {
                    deviceUnknownStr
                } else {
                    bluetooth.address.toLowerCase()
                }
            }
        }
    }

    private fun initializeBuildNumber() {
        val buildNumber = BidiFormatter.getInstance().unicodeWrap(Build.DISPLAY)
    }

    private fun initializeBattery() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
        val intent = mContext.registerReceiver(null, intentFilter)
        val batteryLevel = DeviceInfoUtils.getBatteryPercentage(intent)
        val batteryStatus = DeviceInfoUtils.getBatteryStatus(mContext, intent)
        val battery = "Status: $batteryStatus, Level: $batteryLevel"
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

}