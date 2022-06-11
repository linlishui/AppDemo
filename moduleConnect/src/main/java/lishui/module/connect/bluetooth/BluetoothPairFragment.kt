package lishui.module.connect.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.lib.base.log.LogUtils
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lishui.android.ui.extensions.hidden
import lishui.module.connect.R
import lishui.module.connect.viewmodel.ChatViewModel

/**
 * @author lishui.lin
 * Created it on 2021/5/26
 */
private const val REQUEST_ENABLE_BT = 1
private const val TAG = "BluetoothPairFragment"

class BluetoothPairFragment : Fragment(R.layout.fragment_ble_pair) {

    private lateinit var tipTextView: TextView
    private lateinit var actionTextView: TextView
    private lateinit var errorMsgTextView: TextView
    private lateinit var noDevicesTextView: TextView
    private lateinit var bleDeviceList: RecyclerView
    private lateinit var loadingView: ContentLoadingProgressBar

    private var isBtEnable = false

    private lateinit var viewModel: ChatViewModel

    private val deviceScanAdapter by lazy {
        BleDeviceScanAdapter(onDeviceSelected)
    }

    private val viewStateObserver = Observer<BleScanViewState> { state ->
        LogUtils.d(TAG, "BLE scan state=$state")
        when (state) {
            is BleScanViewState.ActiveScan -> showLoading()
            is BleScanViewState.ScanResults -> showResults(state.scanResults)
            is BleScanViewState.ScanError -> showError(state.message)
            is BleScanViewState.AdvertisementNotSupported -> showAdvertisingError()
        }
    }

    private val onDeviceSelected: (BluetoothDevice) -> Unit = { device ->
        viewModel.bleController.setCurrentChatConnection(device)
        // navigate to chat fragment
        parentFragmentManager.beginTransaction()
            .replace(Window.ID_ANDROID_CONTENT, BleChatFragment())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
        viewModel.bleController.checkBluetoothEnable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tipTextView = view.findViewById(R.id.ble_pair_tip)
        actionTextView = view.findViewById(R.id.ble_enable_or_pair)
        errorMsgTextView = view.findViewById(R.id.ble_error_msg)
        noDevicesTextView = view.findViewById(R.id.ble_no_devices)
        loadingView = view.findViewById(R.id.ble_scan_loading)
        bleDeviceList = view.findViewById(R.id.ble_device_list)

        actionTextView.setOnClickListener {
            if (isBtEnable) {
                viewModel.startScan()
            } else {
                // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }

        bleDeviceList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = deviceScanAdapter
        }

        subscribeViewModel()
    }

    private fun subscribeViewModel() {
        viewModel.bleController.btEnableLiveData.observe(requireActivity()) {
            isBtEnable = it
            if (it) {
                tipTextView.setText(R.string.bt_no_connected_device_message)
                actionTextView.setText(R.string.bt_connect_device_prompt)
            } else {
                tipTextView.setText(R.string.bt_not_enabled)
                actionTextView.setText(R.string.bt_enable_prompt)
            }
        }

        viewModel.scanViewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            viewModel.bleController.checkBluetoothEnable()
            viewModel.bleController.startServer()
        }
    }

    private fun showLoading() {
        loadingView.show()

        tipTextView.hidden = true
        actionTextView.hidden = true
        bleDeviceList.hidden = true
        errorMsgTextView.hidden = true
        noDevicesTextView.hidden = true
    }

    private fun showError(message: String) {
        errorMsgTextView.hidden = false
        errorMsgTextView.text = message

        loadingView.hide()
        tipTextView.hidden = true
        actionTextView.hidden = true
        bleDeviceList.hidden = true
        noDevicesTextView.hidden = true
    }

    private fun showAdvertisingError() {
        errorMsgTextView.hidden = false
        errorMsgTextView.text = "BLE advertising is not supported on this device"

        loadingView.hide()
        tipTextView.hidden = true
        actionTextView.hidden = true
        bleDeviceList.hidden = true
        noDevicesTextView.hidden = true
    }

    private fun showResults(scanResults: Map<String, BluetoothDevice>) {

        if (scanResults.isNotEmpty()) {
            bleDeviceList.hidden = false
            deviceScanAdapter.updateItems(scanResults.values.toList())

            noDevicesTextView.hidden = true
        } else {
            noDevicesTextView.hidden = false
            bleDeviceList.hidden = true
        }

        loadingView.hide()
        tipTextView.hidden = true
        actionTextView.hidden = true
        errorMsgTextView.hidden = true

    }
}