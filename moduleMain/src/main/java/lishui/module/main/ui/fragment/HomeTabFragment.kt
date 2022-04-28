package lishui.module.main.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import lishui.lib.router.core.Router
import lishui.module.main.R
import lishui.module.main.ui.recyclerview.adapter.MainEntryListAdapter
import lishui.module.main.ui.recyclerview.model.*
import lishui.service.core.router.RouterPath


class HomeTabFragment : Fragment(R.layout.fragment_home_tab_page), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.main_entry_list).also {
            it.adapter = MainEntryListAdapter().also { mainEntryListAdapter ->
                mainEntryListAdapter.itemClickListener = this
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.tag) {
            is ChatQuickEntry -> {
                Router.getInstance().build(RouterPath.Connect.CHAT).navigation()
            }
            is WanAndroidQuickEntry -> {
                Router.getInstance().build(RouterPath.Wan.ANDROID).navigation()
            }
            is ComposeQuickEntry -> {
                Router.getInstance().build(RouterPath.Compose.ENTRY).navigation()
            }
            is FlutterQuickEntry -> {
                Router.getInstance().build(RouterPath.Flutter.ENTRY).navigation()
            }
            is MediaQuickEntry -> {
                Router.getInstance().build(RouterPath.Media.ENTRY).navigation()
            }
            is MyServerQuickEntry -> {
                Router.getInstance().build(RouterPath.MyServer.ENTRY).navigation()
            }
            is GiteeQuickEntry -> {
                Router.getInstance().build(RouterPath.Gitee.ENTRY).navigation()
            }
        }
    }
}