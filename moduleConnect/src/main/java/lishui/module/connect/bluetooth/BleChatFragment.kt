package lishui.module.connect.bluetooth

import android.bluetooth.BluetoothDevice
import android.lib.base.log.LogUtils
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lishui.module.connect.R
import lishui.module.connect.model.ChatMessage
import lishui.module.connect.ui.adapter.ChatMessageAdapter
import lishui.module.connect.viewmodel.ChatViewModel

/**
 * @author lishui.lin
 * Created it on 2021/5/27
 */
private const val TAG = "BleChatFragment"

class BleChatFragment : Fragment(R.layout.fragment_ble_chat) {

    private val chatAdapter = ChatMessageAdapter()

    private lateinit var chatList: RecyclerView
    private lateinit var pairInfoView: TextView
    private lateinit var msgInputView: EditText
    private lateinit var msgSendView: TextView

    private lateinit var viewModel: ChatViewModel

    private var isConnected = false

    private val deviceConnectionObserver = Observer<BleConnectionState> { state ->
        when (state) {
            is BleConnectionState.Connected -> {
                val device = state.device
                chatWith(device)
                isConnected = true
                LogUtils.d(TAG, "Gatt connection observer: have device $device")
            }
            is BleConnectionState.Disconnected -> {
                isConnected = false
            }
        }
    }

    private val connectionRequestObserver = Observer<BluetoothDevice> { device ->
        LogUtils.d(TAG, "Connection request observer: have device $device")
        viewModel.bleController.setCurrentChatConnection(device)
    }

    private val messageObserver = Observer<ChatMessage> { message ->
        LogUtils.d(TAG, "Have message ${message.text}")
        chatAdapter.addMessage(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatList = view.findViewById(R.id.ble_chat_list)
        pairInfoView = view.findViewById(R.id.ble_pair_info)
        msgInputView = view.findViewById(R.id.ble_msg_input)
        msgSendView = view.findViewById(R.id.ble_msg_send)

        chatList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        msgSendView.setOnClickListener {
            val message = msgInputView.text.toString()
            // only send message if it is not empty
            if (isConnected && message.isNotEmpty()) {
                viewModel.bleController.sendMessage(message)
                msgInputView.setText("")
            }
        }
        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.bleController.connectionRequestLiveData.observe(
            viewLifecycleOwner, connectionRequestObserver
        )
        viewModel.bleController.deviceConnectionLiveData.observe(
            viewLifecycleOwner, deviceConnectionObserver
        )
        viewModel.bleController.messagesLiveData.observe(viewLifecycleOwner, messageObserver)
    }

    private fun chatWith(device: BluetoothDevice) {
        val chattingWithString =
            resources.getString(R.string.ble_chatting_with_device, device.address)
        pairInfoView.text = chattingWithString
    }
}