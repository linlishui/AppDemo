package lishui.module.connect.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import lishui.module.connect.viewmodel.ChatViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        supportFragmentManager.beginTransaction()
            .replace(Window.ID_ANDROID_CONTENT, ChatEntryFragment(), CHAT_ENTRY_TAG)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        viewModel.bleController.startServer()
    }

    override fun onStop() {
        super.onStop()
        viewModel.bleController.stopServer()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag(CHAT_ENTRY_TAG) == null) {
            supportFragmentManager.beginTransaction()
                .replace(Window.ID_ANDROID_CONTENT, ChatEntryFragment(), CHAT_ENTRY_TAG)
                .commit()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val CHAT_ENTRY_TAG = "chat_entry_tag"
    }
}