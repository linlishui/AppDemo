/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lishui.module.connect.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lishui.module.connect.R
import lishui.module.connect.model.ChatMessage
import lishui.module.connect.ui.viewholder.LocalMessageViewHolder
import lishui.module.connect.ui.viewholder.RemoteMessageViewHolder

private const val REMOTE_MESSAGE = 0
private const val LOCAL_MESSAGE = 1

class ChatMessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            REMOTE_MESSAGE -> {
                val view = inflater.inflate(R.layout.item_remote_ble_message, parent, false)
                RemoteMessageViewHolder(view)
            }
            LOCAL_MESSAGE -> {
                val view = inflater.inflate(R.layout.item_local_ble_message, parent, false)
                LocalMessageViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("Unknown MessageAdapter view type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val message = messages[position]) {
            is ChatMessage.RemoteChatMessage -> {
                (holder as RemoteMessageViewHolder).bind(message)
            }
            is ChatMessage.LocalChatMessage -> {
                (holder as LocalMessageViewHolder).bind(message)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position]) {
            is ChatMessage.RemoteChatMessage -> REMOTE_MESSAGE
            is ChatMessage.LocalChatMessage -> LOCAL_MESSAGE
        }
    }

    // Add messages to the top of the list so they're easy to see
    fun addMessage(message: ChatMessage) {
        messages.add(0, message)
        notifyDataSetChanged()
    }
}