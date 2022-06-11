package lishui.module.main.ui.recyclerview.model

sealed class QuickEntry(
    val title: String
)

class ChatQuickEntry : QuickEntry("Chat")
class MediaQuickEntry : QuickEntry("Media")
class GiteeQuickEntry : QuickEntry("Gitee")
class ComposeQuickEntry : QuickEntry("Compose")
class FlutterQuickEntry : QuickEntry("Flutter")
class MyServerQuickEntry : QuickEntry("MyServer")
class WanAndroidQuickEntry : QuickEntry("WanAndroid")
