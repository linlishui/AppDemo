package lishui.demo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.lib.base.log.LogUtils
import android.lib.base.util.Utilities
import android.net.ConnectivityManager
import android.net.Uri
import android.telecom.TelecomManager
import androidx.core.content.FileProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import lishui.demo.app.BuildConfig
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedOutputStream
import java.io.File
import java.time.LocalDateTime

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DemoInstrumentedTest {

    private lateinit var mContext: Context

    @Before
    fun startup() {
        mContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearUp() {

    }

    @Test
    fun testTelecom() {
        //val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("lishui.demo", mContext.packageName)

        val telecomManager = mContext.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        LogUtils.d(TAG, "DEFAULT DIALER PACKAGE=${telecomManager.defaultDialerPackage}")
    }

    @Test
    fun testSensor() {
        val sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val fullSensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        fullSensorList.forEach {
            LogUtils.d(
                TAG,
                "name=${it.name}, vendor=${it.vendor}, type=${it.type}, version=${it.version}"
            )
        }
    }

    @Test
    fun testConnection() {
        var isWifiConn: Boolean = false
        var isMobileConn: Boolean = false
        val connMgr = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connMgr.allNetworks.forEach { network ->
            connMgr.getNetworkInfo(network)?.apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }
        LogUtils.d(TAG, "Wifi connected: $isWifiConn")
        LogUtils.d(TAG, "Mobile connected: $isMobileConn")
    }

    @Test
    fun testFileProvider() {
        val fileProviderAuthority: String = BuildConfig.APPLICATION_ID + ".fileProvider"
        runBlocking {
            val docsPath = File(mContext.filesDir, "docs")
            docsPath.mkdirs()
            val docFile = File(docsPath, "default_doc.txt")
            val contentUri: Uri =
                FileProvider.getUriForFile(mContext, fileProviderAuthority, docFile)
            var bos: BufferedOutputStream? = null
            try {
                bos = BufferedOutputStream(mContext.contentResolver.openOutputStream(contentUri))
                val content = "Hello World! time=" + LocalDateTime.now().toString() + "\n"
                bos.write(content.toByteArray())
                bos.flush()
                LogUtils.d(TAG, "file flush")
            } catch (ex: Exception) {
                LogUtils.d(TAG, ex.toString())
            } finally {
                Utilities.closeSilently(bos)
            }
        }
    }

    companion object {
        const val TAG = "DemoInstrumentedTest"
    }
}