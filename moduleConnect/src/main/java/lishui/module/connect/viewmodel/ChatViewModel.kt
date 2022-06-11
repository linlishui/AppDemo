package lishui.module.connect.viewmodel

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.pm.PackageManager
import android.lib.base.log.LogUtils
import android.lib.base.util.Utilities
import android.net.ConnectivityManager
import android.os.ParcelUuid
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lishui.module.connect.bluetooth.BleController
import lishui.module.connect.bluetooth.BleScanViewState
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.time.LocalTime

/**
 * @author lishui.lin
 * Created it on 21-5-25
 */

private const val TAG = "ChatViewModel"

// 30 second scan period
private const val SCAN_PERIOD = 30_000L

private const val SOCKET_END_FLAG = "#eof"
private const val SOCKET_LOCAL_PORT = 12345

class ChatViewModel(private val app: Application) : AndroidViewModel(app) {

    private var serverJob: Job? = null
    private var clientJob: Job? = null
    private var bleScanJob: Job? = null

    val bleController = BleController(app)

    private val _ipLiveData = MutableLiveData<String>()
    val socketIpLiveData = _ipLiveData as LiveData<String>

    private val _serverMsgLiveData = MutableLiveData<String>()
    val socketServerMsgLiveData = _serverMsgLiveData as LiveData<String>

    private val _clientMsgLiveData = MutableLiveData<String>()
    val socketClientMsgLiveData = _clientMsgLiveData as LiveData<String>

    private val _scanViewState = MutableLiveData<BleScanViewState>()
    val scanViewState = _scanViewState as LiveData<BleScanViewState>

    // Setup ble scan filters and settings
    private val scanFilters = buildScanFilters()
    private val scanSettings = buildScanSettings()
    private var scanCallback: DeviceScanCallback? = null

    // String key is the address of the bluetooth device
    private val scanResults = mutableMapOf<String, BluetoothDevice>()

    // This property will be null if bluetooth is not enabled
    private var scanner: BluetoothLeScanner? = null

    ////////////// SOCKET CHAT
    fun initChatServer() {
        if (app.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            val connectivityManager: ConnectivityManager =
                app.getSystemService(ConnectivityManager::class.java)
            connectivityManager.getLinkProperties(connectivityManager.activeNetwork)?.apply {

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

                _ipLiveData.postValue(addresses.toString())
            }
        }

        serverJob?.cancel()
        serverJob = viewModelScope.launch {
            runSocketServer()
        }
    }

    private suspend fun runSocketServer() = withContext(Dispatchers.Default) {

        var serverSocket: ServerSocket? = null
        try {
            serverSocket = ServerSocket(SOCKET_LOCAL_PORT)
        } catch (ex: Exception) {
            // no-op
        }

        while (isActive) {

            if (serverSocket == null) {
                break
            }
            var socket = serverSocket.accept()

            launch(Dispatchers.Default) {
                var bufferedReader: BufferedReader? = null
                var bufferedWriter: BufferedWriter? = null

                try {
                    LogUtils.d("Client socket: $socket")

                    // receive msg from client
                    bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    var index = -1
                    val clientStringBuild = StringBuilder()
                    val strIterator = bufferedReader.lineSequence().iterator()
                    while (strIterator.hasNext()) {
                        val line = strIterator.next()
                        index = line.indexOf(SOCKET_END_FLAG)
                        if (index != -1) {
                            clientStringBuild.append(line.substring(0, index))
                            break
                        }
                        clientStringBuild.append(line)
                    }
                    _serverMsgLiveData.postValue("$clientStringBuild")

                    // send msg from server
                    bufferedWriter =
                        BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                    bufferedWriter.write("Send msg by server ${LocalTime.now().toString()} $SOCKET_END_FLAG")
                    bufferedWriter.flush()

                } catch (ex: Exception) {
                    LogUtils.d("runSocketServer exception: $ex")
                } finally {
                    bufferedReader?.let {
                        Utilities.closeSilently(it)
                    }
                    bufferedWriter?.let {
                        Utilities.closeSilently(it)
                    }
                    socket?.let {
                        Utilities.closeSilently(it)
                    }
                }
            }
        }
    }

    fun initChatClient(ip: String) {
        clientJob?.cancel()
        clientJob = viewModelScope.launch {
            runSocketClient(ip)
        }
    }

    private suspend fun runSocketClient(ip: String) = withContext(Dispatchers.Default) {

        var socket: Socket? = null
        var bufferedReader: BufferedReader? = null
        var bufferedWriter: BufferedWriter? = null

        try {
            socket = Socket(ip, SOCKET_LOCAL_PORT)
            LogUtils.d("Server socket: ${socket.remoteSocketAddress}")

            // send msg from client
            bufferedWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
            bufferedWriter.write("Send msg by client ${LocalTime.now().toString()} $SOCKET_END_FLAG")
            bufferedWriter.flush()
            socket.shutdownOutput()

            // receive msg from server
            bufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
            var index = -1
            val serverStringBuild = StringBuilder()
            val strIterator = bufferedReader.lineSequence().iterator()
            while (strIterator.hasNext()) {
                val line = strIterator.next()
                index = line.indexOf(SOCKET_END_FLAG)
                if (index != -1) {
                    serverStringBuild.append(line.substring(0, index))
                    break
                }
                serverStringBuild.append(line)
            }
            _clientMsgLiveData.postValue("$serverStringBuild")

        } catch (ex: Exception) {
            LogUtils.d("runSocketClient exception: $ex")
        } finally {
            bufferedReader?.let {
                Utilities.closeSilently(it)
            }
            bufferedWriter?.let {
                Utilities.closeSilently(it)
            }
            socket?.let {
                Utilities.closeSilently(it)
            }
        }
    }

    /////////////// BLE SCAN
    fun startScan() {
        // If advertisement is not supported on this device then other devices will not be able to
        // discover and connect to it.
        if (!bleController.adapter.isMultipleAdvertisementSupported) {
            _scanViewState.value = BleScanViewState.AdvertisementNotSupported
            return
        }

        if (scanCallback == null) {
            scanner = bleController.adapter.bluetoothLeScanner
            LogUtils.d(TAG, "Start BLE Scanning")
            // Update the UI to indicate an active scan is starting
            _scanViewState.value = BleScanViewState.ActiveScan

            // Stop scanning after the scan period
            bleScanJob = viewModelScope.launch {
                delay(SCAN_PERIOD)
                stopScanning()
            }

            // Kick off a new scan
            scanCallback = DeviceScanCallback()
            scanner?.startScan(scanFilters, scanSettings, scanCallback)
        } else {
            LogUtils.d(TAG, "Already BLE scanning")
        }
    }

    private fun stopScanning() {
        LogUtils.d(TAG, "Stopping BLE Scanning")
        bleScanJob?.cancel()
        scanner?.stopScan(scanCallback)
        scanCallback = null
        _scanViewState.value = BleScanViewState.ScanResults(scanResults)
    }

    /**
     * Return a List of [ScanFilter] objects to filter by Service UUID.
     */
    private fun buildScanFilters(): List<ScanFilter> {
        val builder = ScanFilter.Builder()
        // Comment out the below line to see all BLE devices around you
        builder.setServiceUuid(ParcelUuid(BleController.SERVICE_UUID))
        val filter = builder.build()
        return listOf(filter)
    }

    /**
     * Return a [ScanSettings] object set to use low power (to preserve battery life).
     */
    private fun buildScanSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()
    }

    /**
     * Custom ScanCallback object - adds found devices to list on success, displays error on failure.
     */
    private inner class DeviceScanCallback : ScanCallback() {
        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            for (item in results) {
                item.device?.let { device ->
                    scanResults[device.address] = device
                }
            }
            _scanViewState.value = BleScanViewState.ScanResults(scanResults)
        }

        override fun onScanResult(
            callbackType: Int,
            result: ScanResult
        ) {
            super.onScanResult(callbackType, result)
            result.device?.let { device ->
                scanResults[device.address] = device
            }
            _scanViewState.value = BleScanViewState.ScanResults(scanResults)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            // Send error state to the fragment to display
            val errorMessage = "Scan failed with error: $errorCode"
            _scanViewState.value = BleScanViewState.ScanError(errorMessage)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopScanning()
    }
}