package lishui.module.connect.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lishui.module.connect.R
import lishui.module.connect.model.ChatMessage

class LocalMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val messageText = itemView.findViewById<TextView>(R.id.message_text)

    fun bind(message: ChatMessage.LocalChatMessage) {
        messageText.text = message.text
    }
}