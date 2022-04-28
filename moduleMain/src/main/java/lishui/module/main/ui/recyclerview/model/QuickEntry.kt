package lishui.module.main.ui.recyclerview.model

import lishui.android.ui.widget.list.RecyclerData

open class QuickEntry(
    val title: String
) : RecyclerData()

class ChatQuickEntry : QuickEntry("Chat")
class MediaQuickEntry : QuickEntry("Media")
class GiteeQuickEntry : QuickEntry("Gitee")
class ComposeQuickEntry : QuickEntry("Compose")
class FlutterQuickEntry : QuickEntry("Flutter")
class MyServerQuickEntry : QuickEntry("MyServer")
class WanAndroidQuickEntry : QuickEntry("WanAndroid")
