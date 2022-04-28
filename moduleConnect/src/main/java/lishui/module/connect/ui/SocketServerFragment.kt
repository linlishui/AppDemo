package lishui.module.connect.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lishui.module.connect.R
import lishui.module.connect.model.ChatMessage
import lishui.module.connect.ui.adapter.ChatMessageAdapter
import lishui.module.connect.viewmodel.ChatViewModel

class SocketServerFragment : Fragment(R.layout.fragment_socket_server) {

    private lateinit var viewModel: ChatViewModel

    private lateinit var socketIpTv: TextView
    private lateinit var socketChatList: RecyclerView
    private lateinit var socketMsgInput: EditText
    private lateinit var socketMsgSend: TextView

    private val chatAdapter = ChatMessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
        viewModel.initChatServer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        socketIpTv = view.findViewById(R.id.socket_server_ip)
        socketChatList = view.findViewById(R.id.socket_chat_list)
        socketMsgInput = view.findViewById(R.id.socket_msg_input)
        socketMsgSend = view.findViewById(R.id.socket_msg_send)

        socketChatList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        subscribeViewModel()
    }

    private fun subscribeViewModel() {
        viewModel.socketIpLiveData.observe(requireActivity()) {
            socketIpTv.text = it
        }
        viewModel.socketServerMsgLiveData.observe(requireActivity()) {
            chatAdapter.addMessage(ChatMessage.RemoteChatMessage(it))
        }
    }
}