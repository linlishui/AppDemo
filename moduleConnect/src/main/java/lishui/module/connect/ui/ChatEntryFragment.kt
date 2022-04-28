package lishui.module.connect.ui

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import lishui.module.connect.R
import lishui.module.connect.bluetooth.BluetoothPairFragment

class ChatEntryFragment : Fragment(R.layout.fragment_chat_entry) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.chat_server_page).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(Window.ID_ANDROID_CONTENT, SocketServerFragment())
                .commit()
        }

        view.findViewById<View>(R.id.chat_client_page).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(Window.ID_ANDROID_CONTENT, SocketClientFragment())
                .commit()
        }

        view.findViewById<View>(R.id.bluetooth_pair_page).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(Window.ID_ANDROID_CONTENT, BluetoothPairFragment())
                .commit()
        }
    }
}