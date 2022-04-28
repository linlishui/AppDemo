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

class SocketClientFragment : Fragment(R.layout.fragment_socket_client) {

    private lateinit var inputEditText: EditText
    private lateinit var bindTextView: TextView

    private lateinit var socketChatList: RecyclerView
    private lateinit var socketMsgInput: EditText
    private lateinit var socketMsgSend: TextView

    private lateinit var viewModel: ChatViewModel

    private val chatAdapter = ChatMessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputEditText = view.findViewById(R.id.socket_ip_input)
        bindTextView = view.findViewById(R.id.socket_connection)

        socketChatList = view.findViewById(R.id.socket_chat_list)
        socketMsgInput = view.findViewById(R.id.socket_msg_input)
        socketMsgSend = view.findViewById(R.id.socket_msg_send)

        socketChatList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        bindTextView.setOnClickListener {
            viewModel.initChatClient(inputEditText.text.toString())
        }

        subscribeViewModel()
    }

    private fun subscribeViewModel() {
        viewModel.socketClientMsgLiveData.observe(requireActivity()) {
            chatAdapter.addMessage(ChatMessage.LocalChatMessage(it))
        }
    }
}